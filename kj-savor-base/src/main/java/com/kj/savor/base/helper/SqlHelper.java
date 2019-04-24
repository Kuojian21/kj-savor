package com.kj.savor.base.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.kj.savor.base.helper.ModelHelper;
import com.kj.savor.base.helper.ModelHelper.Model;
import com.kj.savor.base.helper.ModelHelper.Property;

import lombok.Data;

/**
 * @author kuojian21
 */
public class SqlHelper {

	public static <T> SqlModel insert(ModelHelper.Model model, String table, List<T> objs) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ")
						.append(Strings.isNullOrEmpty(table) ? model.getTable() : table)
						.append(" (")
						.append(Joiner.on(",")
										.join(model.getInsertProperties().stream().map(ModelHelper.Property::getColumn)
														.collect(Collectors.toList())))
						.append(") ").append("values")
						.append(Joiner.on(",").join(
										IntStream.range(0, objs.size()).boxed()
														.map(i -> {
															StringBuilder values = new StringBuilder();
															values.append("(")
																			.append(Joiner.on(",").join(model
																							.getInsertProperties()
																							.stream()
																							.map(p -> ":" + p.getName()
																											+ i)
																							.collect(Collectors
																											.toList())))
																			.append(")");
															return values.toString();
														})
														.collect(Collectors.toList())));
		Map<String, Object> params = Maps.newHashMap();
		IntStream.range(0, objs.size()).boxed().forEach(i -> {
			model.getInsertProperties().stream().forEach(p -> {
				params.put(p.getName() + i, p.get(objs.get(i)));
			});
		});
		return SqlModel.model(sql, params);
	}

	public static <T> SqlModel upsert(Model model, String table, T obj, List<String> exprs) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ")
						.append(Strings.isNullOrEmpty(table) ? model.getTable() : table)
						.append(" (")
						.append(Joiner.on(",")
										.join(model.getInsertProperties().stream().map(Property::getColumn)
														.collect(Collectors.toList())))
						.append(") values(")
						.append(Joiner.on(",")
										.join(model.getInsertProperties().stream().map(e -> ":" + e.getName())
														.collect(Collectors.toList())))
						.append(")").append("  on duplicate key update ")
						.append(Joiner.on(",").join(exprs));
		Map<String, Object> params = Maps.newHashMap();
		return SqlModel.model(sql, params);
	}

	public static SqlModel delete(ModelHelper.Model model, String table, Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ")
						.append(Strings.isNullOrEmpty(table) ? model.getTable() : table)
						.append(SqlHelper.where(model, params));
		return SqlModel.model(sql, params);
	}

	public static SqlModel update(ModelHelper.Model model, String table, Map<String, Object> newValues,
					Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		params = Maps.newHashMap(params);
		sql.append("update ")
						.append(Strings.isNullOrEmpty(table) ? model.getTable() : table)
						.append(" set ")
						.append(Joiner.on(",").join(newValues.entrySet().stream().map(e -> {
							ModelHelper.Property p = model.getProperty(e.getKey());
							return p.getColumn() + "=:$newValuePrefix$" + p.getName();
						}).collect(Collectors.toList())))
						.append(SqlHelper.where(model, params));
		params.putAll(newValues.entrySet().stream()
						.collect(Collectors.toMap(e -> "$newValuePrefix$" + e.getKey(), e -> e.getValue())));
		return SqlModel.model(sql, params);
	}

	public static SqlModel select(ModelHelper.Model model, String table, List<String> names,
					Map<String, Object> params, List<String> orderExprs, Integer offset, Integer limit) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		if (names == null || names.isEmpty()) {
			sql.append("*");
		} else {
			sql.append(Joiner.on(",").join(names.stream().map(n -> {
				ModelHelper.Property property = model.getProperty(n);
				if (property == null) {
					return n;
				}
				return property.getColumn();
			}).collect(Collectors.toList())));
		}
		sql.append(" from ")
						.append(Strings.isNullOrEmpty(table) ? model.getTable() : table)
						.append(SqlHelper.where(model, params));

		if (orderExprs != null && !orderExprs.isEmpty()) {
			sql.append(orderExprs != null
							? " order by " + Joiner.on(",").join(orderExprs)
							: "");
		}
		if (offset != null) {
			sql.append(" limit " + offset + "," + (limit == null ? 1 : limit));
		} else if (limit != null) {
			sql.append(" limit " + limit);
		}
		return SqlModel.model(sql, params);
	}

	public static String where(ModelHelper.Model model, Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return "";
		}
		return " where " + Joiner.on(" and ").join(params.entrySet().stream().map(cond -> {
			ModelHelper.Property p = model.getProperty(cond.getKey());
			if (cond.getValue() instanceof Collection) {
				return p.getColumn() + "in(:" + p.getName() + ")";
			}
			return p.getColumn() + "=:" + p.getName();
		}).collect(Collectors.toList()));
	}

	@Data
	public static class SqlModel {
		private StringBuilder sql;
		private Map<String, Object> params;

		public static SqlModel model(StringBuilder sql, Map<String, Object> params) {
			SqlModel model = new SqlModel();
			model.sql = sql;
			model.params = params;
			return model;
		}
	}

}

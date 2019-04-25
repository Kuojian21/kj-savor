package com.kj.savor.base.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.google.common.collect.Lists;
import com.kj.savor.base.helper.ModelHelper;
import com.kj.savor.base.helper.ModelHelper.Model;
import com.kj.savor.base.helper.SqlHelper;
import com.kj.savor.base.helper.SqlHelper.SqlModel;

/**
 * 
 * @author kuojian21
 *
 */
public abstract class Savor<T> {

	private Class<T> clazz;
	private RowMapper<T> rowMapper;

	protected Savor() {
		Type superClass = this.getClass().getGenericSuperclass();
		if (superClass instanceof ParameterizedType) {
			Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
			if (type instanceof Class) {
				clazz = (Class<T>) type;
				rowMapper = new BeanPropertyRowMapper<T>(clazz);
			}
		}
	}

	public int insert(List<T> objs) {
		return this.insert("", objs);
	}

	public int upsert(T obj, List<String> updateExprs) {
		return this.upsert(obj, updateExprs);
	}

	public int delete(Map<String, Object> params) {
		return this.delete("", params);
	}

	public int update(Map<String, Object> newValues, Map<String, Object> params) {
		return this.update("", newValues, params);
	}

	public int insert(String table, List<T> objs) {
		try {
			SqlModel sqlModel = SqlHelper.insert(getModel(), table, objs);
			return this.getWriter().update(sqlModel.getSql().toString(), sqlModel.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int upsert(String table, T obj, List<String> updateExprs) {
		try {
			SqlModel sqlModel = SqlHelper.upsert(getModel(), table, obj, updateExprs);
			return this.getWriter().update(sqlModel.getSql().toString(), sqlModel.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int delete(String table, Map<String, Object> params) {
		try {
			SqlModel sqlModel = SqlHelper.delete(getModel(), table, params);
			return this.getWriter().update(sqlModel.getSql().toString(), sqlModel.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int update(String table, Map<String, Object> newValues, Map<String, Object> params) {
		try {
			SqlModel sqlModel = SqlHelper.update(getModel(), table, newValues, params);
			return this.getWriter().update(sqlModel.getSql().toString(), sqlModel.getParams());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public List<T> select(Map<String, Object> params) {
		return this.select("", null, params, null, null, null);
	}

	public List<T> select(String table, List<String> names,
					Map<String, Object> params, List<String> orderExprs, Integer offset, Integer limit) {
		return this.select(table, names, params, orderExprs, offset, limit, this.getRowMapper());
	}

	public <R> List<R> select(String table, List<String> names,
					Map<String, Object> params, List<String> orderExprs, Integer offset, Integer limit,
					RowMapper<R> rowMapper) {
		try {
			SqlModel sqlModel = SqlHelper.select(getModel(), table, names, params, orderExprs,
							offset, limit);
			return this.getWriter().query(sqlModel.getSql().toString(), sqlModel.getParams(), rowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			return Lists.newArrayList();
		}
	}

	public Model getModel() {
		return ModelHelper.model(getClazz());
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public RowMapper<T> getRowMapper() {
		return rowMapper;
	}

	public abstract NamedParameterJdbcTemplate getReader();

	public abstract NamedParameterJdbcTemplate getWriter();

}

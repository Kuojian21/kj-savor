package com.kj.savor.base.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * 
 * @author kuojian21
 *
 */
public class ModelHelper {

	private static final ConcurrentMap<Class<?>, Model> models = Maps.newConcurrentMap();

	public static Model model(Class<?> clazz) {
		return models.computeIfAbsent(clazz, k -> {
			List<Property> properties = Arrays.stream(k.getDeclaredFields())
							.filter(f -> !Modifier.isStatic(f.getModifiers())
											&& !Modifier.isFinal(f.getModifiers()))
							.map(f -> {
								f.setAccessible(true);
								return new Property(f.getName(),
												CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, f.getName()),
												f,
												f.getAnnotation(PrimaryKey.class) != null,
												f.getAnnotation(PrimaryKey.class) == null
																|| f.getAnnotation(PrimaryKey.class).insert());
							}).collect(Collectors.toList());
			return new Model(clazz.getSimpleName(),
							CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName()), properties);
		});
	}

	/**
	 * 
	 * @author kuojian21
	 *
	 */
	@Data
	public static class Model {
		private final String name;
		private final String table;
		private final List<Property> properties;
		private final Map<String, Property> propertyMap;
		private final List<Property> insertProperties;

		public Model(String name, String table, List<Property> properties) {
			super();
			this.name = name;
			this.table = table;
			this.properties = Collections.unmodifiableList(properties);
			this.propertyMap = Collections
							.unmodifiableMap(properties.stream().collect(Collectors.toMap(p -> p.getName(), p -> p)));
			this.insertProperties = Collections
							.unmodifiableList(
											properties.stream().filter(p -> p.isInsert()).collect(Collectors.toList()));
		}

		public Property getProperty(String name) {
			return this.propertyMap.get(name);
		}

		public List<Property> getProperties(List<String> names) {
			return names.stream().map(name -> this.propertyMap.get(name)).collect(Collectors.toList());
		}

		public String getTable(String suffix) {
			return this.table + suffix;
		}

	}

	/**
	 * 
	 * @author kuojian21
	 *
	 */
	@Data
	public static class Property {

		private final String name;
		private final String column;
		private final String type;
		private final Field field;
		private final boolean primaryKey;
		private final boolean insert;
		private final String comment;

		public Property(String name, String column, Field field, boolean primaryKey, boolean insert) {
			this(name, column, null, field, primaryKey, insert, null);
		}

		public Property(String name, String column, String type, boolean primaryKey, boolean insert, String comment) {
			this(name, column, type, null, primaryKey, insert, comment);
		}

		public Property(String name, String column, String type, Field field, boolean primaryKey, boolean insert,
						String comment) {
			super();
			this.name = name;
			this.column = column;
			this.type = type;
			this.field = field;
			this.primaryKey = primaryKey;
			this.insert = insert;
			this.comment = comment;
		}

		public Object get(Object obj) {
			try {
				return this.field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * 
	 * @author kuojian21
	 *
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface PrimaryKey {
		boolean insert() default false;
	}

}

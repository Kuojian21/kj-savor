package com.kj.savor.base.test;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kj.savor.base.helper.ModelHelper;
import com.kj.savor.base.helper.SqlHelper;

public class SqlBuilderTest {

	public static void main(String[] args) throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> newValue = Maps.newHashMap();
		System.out.println(SqlHelper.insert(ModelHelper.model(A.class), null, Lists.newArrayList(new A(), new A())));
		System.out.println(SqlHelper.upsert(ModelHelper.model(A.class), null, new A(),
						Lists.newArrayList("name=value(name)")));
		System.out.println(SqlHelper.delete(ModelHelper.model(A.class), null, params));
		System.out.println(SqlHelper.update(ModelHelper.model(A.class), null, newValue, params));
		System.out.println(SqlHelper.select(ModelHelper.model(A.class), null, null, params, null, null, null));
		params.put("userId", 100);
		newValue.put("name", "kj");
		System.out.println(SqlHelper.insert(ModelHelper.model(A.class), null, Lists.newArrayList(new A(), new A())));
		System.out.println(SqlHelper.upsert(ModelHelper.model(A.class), null, new A(),
						Lists.newArrayList("name=value(name)")));
		System.out.println(SqlHelper.delete(ModelHelper.model(A.class), null, params));
		System.out.println(SqlHelper.update(ModelHelper.model(A.class), null, newValue, params));
		System.out.println(SqlHelper.select(ModelHelper.model(A.class), null, null, params, null, null, null));
	}

	public static class A {
		private String name;
		private String value;
		private long userId;
		private String userName;
		private int val = ThreadLocalRandom.current().nextInt(2);

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public long getUserId() {
			return userId;
		}

		public void setUserId(long userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public int getVal() {
			return val;
		}

		public void setVal(int val) {
			this.val = val;
		}

	}

}

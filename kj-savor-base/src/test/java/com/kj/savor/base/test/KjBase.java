package com.kj.savor.base.test;

import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Lists;
import com.kj.savor.base.helper.ParamsHelper;
import com.kj.savor.base.helper.ModelHelper;
import com.kj.savor.base.helper.SqlHelper;

public class KjBase {

	public static void main(String[] args) throws Exception {

		System.out.println(SqlHelper.insert(ModelHelper.model(A.class), null, Lists.newArrayList(new A(), new A())));
		System.out.println(SqlHelper.upsert(ModelHelper.model(A.class), null, new A(),
						Lists.newArrayList("name=value(name)")));
		System.out.println(SqlHelper.delete(ModelHelper.model(A.class), null, ParamsHelper.newHashMap()));
		System.out.println(SqlHelper.update(ModelHelper.model(A.class), null, ParamsHelper.newHashMap(),
						ParamsHelper.newHashMap()));
		System.out.println(SqlHelper.select(ModelHelper.model(A.class), null, null, ParamsHelper.newHashMap(), null, null,
						null));

		System.out.println(SqlHelper.insert(ModelHelper.model(A.class), null, Lists.newArrayList(new A(), new A())));
		System.out.println(SqlHelper.upsert(ModelHelper.model(A.class), null, new A(),
						Lists.newArrayList("name=value(name)")));
		System.out.println(SqlHelper.delete(ModelHelper.model(A.class), null, ParamsHelper.newHashMap("userId", 100)));
		System.out.println(SqlHelper.update(ModelHelper.model(A.class), null, ParamsHelper.newHashMap("name", "kj"),
						ParamsHelper.newHashMap("userId", 100)));
		System.out.println(SqlHelper.select(ModelHelper.model(A.class), null, null, ParamsHelper.newHashMap("userId", 100),
						null, null, null));
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

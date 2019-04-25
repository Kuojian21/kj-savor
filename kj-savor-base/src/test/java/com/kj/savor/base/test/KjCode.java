package com.kj.savor.base.test;

import java.sql.SQLException;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import com.kj.savor.base.helper.CodeHelper;
import com.kj.savor.base.helper.ModelHelper.Model;
import com.mysql.cj.jdbc.Driver;

public class KjCode {

	/**
	 * create table savor_base_test( id bigint(20) unsigned not null primary key
	 * comment '自增主键', hash_key varchar(64) comment 'key', value varchar(128)
	 * comment 'value', name varchar(64) comment 'name', sex varchar(64) comment
	 * 'sex', age tinyint comment 'age', create_time bigint(20) comment '创建时间',
	 * update_time bigint(20) comment '创建时间' )ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;
	 */
	public static void main(String[] args) throws SQLException {
		/*
		 * -DsocksProxyHost= -DsocksProxyPort=8088
		 */
//		System.setProperty("socksProxyHost", "127.0.0.1");
//		System.setProperty("socksProxyPort", "8088");

		Model model = CodeHelper.mysql(new SimpleDriverDataSource(new Driver(), args[0], args[1], args[2]), args[3]);

		CodeHelper.code(model);
	}

}

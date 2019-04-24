package com.kj.savor.base.test;

import java.sql.SQLException;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import com.kj.savor.base.helper.CodeHelper;
import com.kj.savor.base.helper.ModelHelper.Model;
import com.mysql.cj.jdbc.Driver;

public class KjCode {

	public static void main(String[] args) throws SQLException {
		/*
		 * -DsocksProxyHost= -DsocksProxyPort=8088
		 */
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "8088");

		Model model = CodeHelper.mysql(new SimpleDriverDataSource(new Driver(), args[0], args[1], args[2]), args[3]);

		CodeHelper.code(model);
	}

}

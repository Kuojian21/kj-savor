package com.kj.savor.base.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.base.CaseFormat;

import com.kj.savor.base.helper.ModelHelper.Model;
import com.kj.savor.base.helper.ModelHelper.Property;

/**
 * 
 * @author kuojian21
 *
 */
public class CodeHelper {

	public static Model mysql(DataSource dataSource, String table) {
		List<Property> properties = new JdbcTemplate(dataSource)
						.query("show full columns from " + table, new RowMapper<Property>() {
							@Override
							public Property mapRow(ResultSet rs, int rowNum) throws SQLException {
								String type = "";
								if (rs.getString("Type").startsWith("tinyint")
												|| rs.getString("Type").startsWith("int")) {
									type = "Integer";
								} else if (rs.getString("Type").startsWith("bigint")) {
									type = "Long";
								} else if (rs.getString("Type").startsWith("varchar")
												|| rs.getString("Type").endsWith("text")) {
									type = "String";
								}

								return new Property(
												CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,
																rs.getString("Field")),
												rs.getString("Field"),
												type,
												"PRI".equals(rs.getString("Key")),
												"auto_increment".equals(rs.getString("Extra")),
												rs.getString("Comment"));
							}
						});
		return new Model(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, table), table, properties);
	}

	public static void code(Model model) {
		System.out.println("/**");
		System.out.println(" * @author kj");
		System.out.println(" */");
		System.out.println("public class " + model.getName() + "{");
		for (Property property : model.getProperties()) {
			System.out.println("\t/*" + property.getComment() + "*/");
			if (property.isPrimaryKey()) {
				System.out.print("\t@PrimaryKey");
				if (property.isInsert()) {
					System.out.print("(insert=true)");
				}
				System.out.println();
			}
			System.out.println("\tprivate " + property.getType() + " " + property.getName() + ";");
		}
		System.out.println("}");
	}

}

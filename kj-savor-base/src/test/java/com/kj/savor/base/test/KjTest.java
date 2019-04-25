package com.kj.savor.base.test;

import java.sql.SQLException;
import java.util.Random;
import java.util.stream.IntStream;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kj.savor.base.dao.Savor;
import com.kj.savor.base.helper.ModelHelper.PrimaryKey;
import com.kj.savor.base.helper.ParamsHelper;
import com.mysql.cj.jdbc.Driver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KjTest {

	public static void main(String[] args) throws SQLException {
		SavorBaseTestDao dao = new SavorBaseTestDao(
						new SimpleDriverDataSource(new Driver(), args[0], args[1], args[2]));
		IntStream.range(0, 100).boxed().forEach(i -> insert(dao));
		dao.delete(ParamsHelper.newHashMap("id", 1));
		IntStream.range(0, 10).boxed()
						.forEach(i -> dao.update(ParamsHelper.newHashMap("name", "kj"),
										ParamsHelper.newHashMap("id", new Random().nextInt(100))));
		log.info("{}", dao.select(ParamsHelper.newHashMap("name", "kj")));
		log.info("{}", dao.select(ParamsHelper.newHashMap("id#le", 10)));
		log.info("{}", dao.select(ParamsHelper.newHashMap("id#ge", 90)));
		dao.update(ParamsHelper.newHashMap("id#sub", 1), ParamsHelper.newHashMap("id#LE", "100"));
	}

	public static void insert(SavorBaseTestDao dao) {
		SavorBaseTest test = new SavorBaseTest();
		test.setCreateTime(System.currentTimeMillis());
		test.setUpdateTime(System.currentTimeMillis());
		dao.insert(Lists.newArrayList(test));
	}

	public static class SavorBaseTestDao extends Savor<SavorBaseTest> {

		private final DataSource dataSource;

		public SavorBaseTestDao(DataSource dataSource) {
			super();
			this.dataSource = dataSource;
		}

		@Override
		public NamedParameterJdbcTemplate getReader() {
			return new NamedParameterJdbcTemplate(this.dataSource);
		}

		@Override
		public NamedParameterJdbcTemplate getWriter() {
			return new NamedParameterJdbcTemplate(this.dataSource);
		}

	}

	/**
	 * @author kj
	 */
	@Data
	public static class SavorBaseTest {
		/* 自增主键 */
		@PrimaryKey
		private Long id;
		/* key */
		private String hashKey;
		/* value */
		private String value;
		/* name */
		private String name;
		/* sex */
		private String sex;
		/* age */
		private Integer age;
		/* 创建时间 */
		private Long createTime;
		/* 创建时间 */
		private Long updateTime;

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}

}

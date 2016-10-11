package com.briup.server;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.briup.common.ConnectionFactory;
import com.briup.mapper.BIDRMapper;
import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.server.DBStore;

public class DBStoreImpl implements DBStore, ConfigurationAWare {
	private Logger logger;

	@Override
	public void init(Properties properties) {
	}

	@Override
	public void saveToDB(Collection<BIDR> collection) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		List<BIDR> list = (List<BIDR>) collection;

		conn = ConnectionFactory.getConnection();
		conn.setAutoCommit(false);

		logger.info("开始导入数据库...");
		for (int i = 0; i < list.size(); i++) {
			// 通过登录时间来确定存入哪张表
			String Login_date = list.get(i).getLogin_date().toString();
			String[] strings = Login_date.split(" ");
			String[] split = strings[0].toString().split("-");
			int day = Integer.parseInt(split[2]);
			String tableName = "t_detail_" + day;

			String sql = "insert into " + tableName
					+ "(aaa_login_name,login_ip,login_date,logout_date,nas_ip,time_duration) values(?,?,?,?,?,?)";

			ps = conn.prepareStatement(sql);

			ps.setString(1, list.get(i).getAAA_login_name());
			ps.setString(2, list.get(i).getLogin_ip());
			ps.setTimestamp(3, list.get(i).getLogin_date());
			ps.setTimestamp(4, list.get(i).getLogout_date());
			ps.setString(5, list.get(i).getNAS_ip());
			ps.setInt(6, list.get(i).getTime_deration());

			ps.execute();
			ps.close();
		}
		conn.commit();
		logger.info("数据导入完成!");
		ConnectionFactory.close(ps, conn);

	}

	public void saveToDBByMyBatis(Collection<BIDR> collection) {
		List<BIDR> list = (List<BIDR>) collection;
		BIDR bidr = list.get(0);

		SqlSession sqlSession = null;

		try {
			InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			sqlSession = sqlSessionFactory.openSession();
			BIDRMapper bidrMapper = sqlSession.getMapper(BIDRMapper.class);

			String Login_date = bidr.getLogin_date().toString();
			String[] strings = Login_date.split(" ");
			String[] split = strings[0].toString().split("-");
			int day = Integer.parseInt(split[2]);

			String tableName = "t_detail_" + day;

			bidrMapper.insertBIDR(tableName, bidr);

			sqlSession.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (sqlSession != null)
				sqlSession.close();
		}
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		try {
			logger = configuration.getLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

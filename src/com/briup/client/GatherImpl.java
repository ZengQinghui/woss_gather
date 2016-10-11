package com.briup.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Gather;

public class GatherImpl implements Gather, ConfigurationAWare {
	// 字符读取流
	private BufferedReader br;
	// 状态码，7代表在线，8代表下线
	private int stateCode;
	// 登录名
	private String AAA_login_name;
	// 登录ip
	private String login_ip;
	// 时间
	private Timestamp date;
	// 登录时间
	private Timestamp login_date;
	// 离线时间
	private Timestamp logout_date;
	// NAS ip
	private String NAS_ip;
	// 在线时长
	private Integer time_deration;
	// 用来临时存储BIDR对象
	private Map<String, BIDR> map;
	// 存储所有的BIDR对象
	private Collection<BIDR> collection;
	// 声明BIDR对象
	private BIDR bidr;
	// 日志对象
	private Logger logger;
	// 备份对象
	private BackUP backUp;
	// 采集文件路径
	private String filePath;

	@Override
	public void init(Properties properties) {
		filePath = properties.getProperty("AAA-filePath");
		NAS_ip = properties.getProperty("nasIp");
	}

	// 采集数据
	@SuppressWarnings("unchecked")
	@Override
	public Collection<BIDR> gather() throws Exception {
		map = new HashMap<String, BIDR>();
		collection = new ArrayList<BIDR>();
		br = null;

		logger.info("加载备份文件");
		map = (Map<String, BIDR>) backUp.load("backup.txt", false);
		logger.info("加载备份文件完成");

		logger.info("信息开始采集...");
		// 读取文件,从xml文件
		br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
		while (br.ready()) {
			String line = br.readLine();

			// 分割读取的每一行
			String[] strings = line.split("[|]");

			// 为成员变量赋值
			AAA_login_name = strings[0].substring(1, strings[0].length());
			//NAS_ip = strings[1];
			stateCode = Integer.parseInt(strings[2]);
			date = new Timestamp((Long.parseLong(strings[3])) * 1000);
			login_ip = strings[4];

			// 当它的状态码为8时,那么肯定已经有一个和它对应的状态码7，此时根据状态码8的登录ip,可以从map中拿出与之对应的对象，
			// 这时候就可以通过状态码为8的信息把对象的logout_date和time_deration属性设置出来
			if (stateCode == 8) {
				bidr = map.get(login_ip);
				logout_date = date;
				if ((logout_date.getTime() - bidr.getLogin_date().getTime()) / (60 * 1000) == 0) {
					time_deration = (int) ((logout_date.getTime() - bidr.getLogin_date().getTime()) / (60 * 1000));
				} else {
					time_deration = (int) ((logout_date.getTime() - bidr.getLogin_date().getTime()) / (60 * 1000)) + 1;
				}
				bidr.setLogout_date(logout_date);
				bidr.setTime_deration(time_deration);
				collection.add(bidr);
				// 删除有上线和下线的对象,然后集合里面剩下的都是只有上线的对象，就可以进行备份了
				map.remove(login_ip);
			}
			// 当它的状态码为7时，new一个BIDR对象，设置属性，这时候BIDR的logout_date和time_deration属性先设置为空，然后放到map集合里面
			else {
				bidr = new BIDR();
				login_date = date;
				bidr.setAAA_login_name(AAA_login_name);
				bidr.setLogin_date(login_date);
				bidr.setLogin_ip(login_ip);
				bidr.setLogout_date(null);
				bidr.setNAS_ip(NAS_ip);
				bidr.setTime_deration(null);
				map.put(login_ip, bidr);
			}
		}

		if (br != null)
			br.close();

		// 数据备份
		logger.info("数据开始备份...");

		backUp.store("backup.txt", map, true);

		logger.info("数据备份完毕!");

		logger.info("信息采集完毕!");

		return collection;

	}

	@Override
	public void setConfiguration(Configuration configuration) {
		try {
			logger = configuration.getLogger();
			backUp = configuration.getBackup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

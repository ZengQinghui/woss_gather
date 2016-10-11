package com.briup.main;

import com.briup.common.ConfigurationImpl;
import com.briup.util.Configuration;
import com.briup.woss.server.Server;

public class ServerTest {
	public static void main(String[] args) {
		try {
			Configuration configuration = new ConfigurationImpl();
			Server server = configuration.getServer();
			server.revicer();
		} catch (Exception e) {
			System.out.println("服务器已关闭");
			System.exit(0);
		}
	}
}

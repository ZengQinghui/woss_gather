package com.briup.client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Client;

public class ClientImpl implements Client, ConfigurationAWare {
	private int port;
	private String ip;
	private Socket client;
	private Logger logger;
	private ObjectOutputStream oos;

	@Override
	public void init(Properties properties) {
		port = Integer.parseInt(properties.getProperty("port"));
		ip = properties.getProperty("ip");
	}

	@Override
	public void send(Collection<BIDR> collection) throws Exception {
		oos = null;
		client = new Socket(ip, port);
		logger.info("客户端连接服务器成功");

		// 客户端发送数据给服务器
		logger.info("客户端开始发送数据...");
		oos = new ObjectOutputStream(client.getOutputStream());
		oos.writeObject(collection);
		oos.flush();
		logger.info("客户端发送数据完毕");

		if (client != null)
			client.close();
		if (oos != null)
			oos.close();
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

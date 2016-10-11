package com.briup.server;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;

public class ServerImpl implements Server, ConfigurationAWare {
	private int port;
	private Socket client;
	private ServerSocket server;
	private Logger logger;
	private DBStore dbStore;
	// 计时器来关闭服务器
	private Timer timer;
	// 定时器任务
	private TimerTask timeTask;

	@Override
	public void init(Properties properties) {
		port = Integer.parseInt(properties.getProperty("serverport"));
	}

	@SuppressWarnings("all")
	@Override
	public Collection<BIDR> revicer() throws Exception {
		timer = new Timer();
		timeTask = new TimerTask() {
			@Override
			public void run() {
				shutdown();
			}
		};

		server = new ServerSocket(port);
		logger.info("服务器已启动,等待客户端连接...");
		while (true) {
			client = server.accept();
			logger.info("服务器收到客户端连接" + client);
			new MyThread(client, dbStore, logger).start();
			timer.schedule(timeTask, 30000);
		}
	}

	@Override
	public void shutdown() {
		try {
			server.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		try {
			logger = configuration.getLogger();
			dbStore = configuration.getDBStore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class MyThread extends Thread {
	private Socket client;
	private ObjectInputStream ois;
	private DBStore dbStore;
	private Logger logger;

	public MyThread(Socket client, DBStore dbStore, Logger logger) {
		this.client = client;
		this.dbStore = dbStore;
		this.logger = logger;
	}

	@SuppressWarnings("all")
	@Override
	public void run() {
		ois = null;
		try {
			logger.info("服务器开始接收数据...");
			ois = new ObjectInputStream(client.getInputStream());
			Collection<BIDR> collection = (Collection<BIDR>) ois.readObject();
			logger.info("服务器接收数据完成!");
			dbStore.saveToDB(collection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

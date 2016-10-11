package com.briup.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.client.GatherImpl;
import com.briup.client.ClientImpl;
import com.briup.server.DBStoreImpl;
import com.briup.server.ServerImpl;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.WossModule;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;

@SuppressWarnings("all")
public class ConfigurationImpl implements Configuration {
	private BackUP backUP;
	private Client client;
	private DBStore dbStore;
	private Gather gather;
	private Logger logger;
	private Server server;

	public ConfigurationImpl() throws Exception {
		this("src/com/briup/file/conf.xml");
	}

	public ConfigurationImpl(String path) throws Exception {
		SAXReader reader = new SAXReader();
		File file = new File(path);
		Document document = reader.read(file);
		Element rootElement = document.getRootElement();
		List<Element> elements = rootElement.elements();
		Map<String, WossModule> map = new HashMap<String, WossModule>();
		for (Element e : elements) {
			WossModule woss = (WossModule) Class.forName(e.attributeValue("class")).newInstance();
			map.put(e.getName(), woss);
			Properties properties = new Properties();
			List<Element> elements2 = e.elements();
			for (Element e2 : elements2) {
				properties.setProperty(e2.getName(), e2.getText());
			}
			woss.init(properties);
		}

		if (map.containsKey("gather")) {
			gather = (Gather) map.get("gather");
		}
		if (map.containsKey("logger")) {
			logger = (Logger) map.get("logger");
		}
		if (map.containsKey("Back")) {
			backUP = (BackUP) map.get("Back");
		}
		if (map.containsKey("client")) {
			client = (Client) map.get("client");
		}
		if (map.containsKey("server")) {
			server = (Server) map.get("server");
		}
		if (map.containsKey("dbStore")) {
			dbStore = (DBStore) map.get("dbStore");
		}

		for (WossModule w : map.values()) {
			if (w instanceof ConfigurationAWare) {
				ConfigurationAWare c = (ConfigurationAWare) w;
				c.setConfiguration(this);
			}
		}
	}

	@Override
	public BackUP getBackup() throws Exception {

		return backUP;
	}

	@Override
	public Client getClient() throws Exception {

		return client;
	}

	@Override
	public DBStore getDBStore() throws Exception {

		return dbStore;
	}

	@Override
	public Gather getGather() throws Exception {

		return gather;
	}

	@Override
	public Logger getLogger() throws Exception {

		return logger;
	}

	@Override
	public Server getServer() throws Exception {

		return server;
	}

}

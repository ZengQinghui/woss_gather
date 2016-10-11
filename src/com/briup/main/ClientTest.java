package com.briup.main;

import com.briup.common.ConfigurationImpl;
import com.briup.util.Configuration;
import com.briup.woss.client.Client;

public class ClientTest {
	public static void main(String[] args) {
		try {
			Configuration configuration = new ConfigurationImpl();
			Client client = configuration.getClient();
			client.send(configuration.getGather().gather());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

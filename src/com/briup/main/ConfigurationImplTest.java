package com.briup.main;

import com.briup.common.ConfigurationImpl;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;

public class ConfigurationImplTest {
	public static void main(String[] args) {

		try {
			Configuration c = new ConfigurationImpl();
			Client client = c.getClient();
			BackUP backup = c.getBackup();
			DBStore dbStore = c.getDBStore();
			Gather gather = c.getGather();
			Logger logger = c.getLogger();
			Server server = c.getServer();
			System.out.println(client);
			System.out.println(backup);
			System.out.println(dbStore);
			System.out.println(gather);
			System.out.println(logger);
			System.out.println(server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

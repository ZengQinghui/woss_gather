package com.briup.main;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;

import com.briup.client.GatherImpl;
import com.briup.common.ConfigurationImpl;
import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.woss.client.Gather;

public class GatherImplTest {
	public static void main(String[] args) {
		//Gather gi = new GatherImpl();
		 PrintWriter pw;
		try {
			Configuration c = new ConfigurationImpl();
			Gather gather = c.getGather();
			/*pw = null;
			pw = new PrintWriter(new FileWriter("src/com/briup/file/B.txt", true));
			Collection<BIDR> collection = gi.gather();
			for(BIDR bidr : collection){
				pw.println(bidr.getTime_deration());				
			}
			pw.flush();*/
			gather.gather();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

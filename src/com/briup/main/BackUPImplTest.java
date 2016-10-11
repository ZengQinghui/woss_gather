package com.briup.main;

import java.util.List;
import java.util.Map;

import com.briup.common.BackUPImpl;
import com.briup.util.BIDR;
import com.briup.util.BackUP;

public class BackUPImplTest {
	public static void main(String[] args) {
		BackUP bup = new BackUPImpl();
		try {
			Object object = bup.load("src/com/briup/file/backup.txt", false);
			Map<String,BIDR> map = (Map<String,BIDR>)object;
			for(String key : map.keySet()){
				System.out.println(map.get(key).getAAA_login_name());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

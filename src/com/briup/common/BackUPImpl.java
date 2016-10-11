package com.briup.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;

public class BackUPImpl implements BackUP, ConfigurationAWare {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Logger logger;
	private String path;

	@Override
	public void init(Properties properties) {
		path = properties.getProperty("back");
	}

	@Override
	public Object load(String fileName, boolean flag) throws Exception {
		ois = null;
		File file = new File(path + fileName);
		
		if(!file.exists()){
			return null;
		}
		
		ois = new ObjectInputStream(new FileInputStream(file));
		Object object = ois.readObject();

		if (ois != null)
			ois.close();

		if (file.exists()) {
			// 如果文件存在，通过传入的flag来决定加载之后的备份文件要不要删除掉
			if (flag) {
				file.delete();
				logger.info("备份文件删除成功!");
			}
		}

		return object;
	}

	@Override
	public void store(String fileName, Object object, boolean flag) throws Exception {
		oos = null;
		File file = new File(path + fileName);
		if (file.exists()) {
			// 如果文件存在，通过传入的flag来决定要不要先删除这个文件
			if (flag) {
				file.delete();
				logger.info("备份文件删除成功!");
			}
		}
		oos = new ObjectOutputStream(new FileOutputStream(file, true));
		oos.writeObject(object);
		oos.flush();

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

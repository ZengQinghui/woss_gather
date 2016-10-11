package com.briup.common;

import java.util.Properties;
import com.briup.util.Logger;

public class LoggerImpl implements Logger {
	private static org.apache.log4j.Logger logger;
	
	static {
		// 读取配置文件log4j.properties
		logger = org.apache.log4j.Logger.getRootLogger();
	}

	@Override
	public void init(Properties properties) {
	}

	@Override
	public void debug(String string) {
		logger.debug(string);
	}

	@Override
	public void error(String string) {
		logger.error(string);
	}

	@Override
	public void fatal(String string) {
		logger.fatal(string);
	}

	@Override
	public void info(String string) {
		logger.info(string);
	}

	@Override
	public void warn(String string) {
		logger.warn(string);
	}

}

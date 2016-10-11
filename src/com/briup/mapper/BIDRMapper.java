package com.briup.mapper;

import org.apache.ibatis.annotations.Param;

import com.briup.util.BIDR;

public interface BIDRMapper {
	public void insertBIDR(@Param("tableName")String tableName,@Param("bidr")BIDR bidr);
}

package com.ibm.kstar.message.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.xsnake.web.utils.MD5Util;

import com.ibm.kstar.log.LogServiceImpl;

public class Test {

	public static void main(String[] args) throws IOException, TimeoutException {
		LogServiceImpl l = new LogServiceImpl();
		l.messageConfig = new MessageConfig();
		l.messageConfig.setHost("10.2.1.57");
		l.messageConfig.setPort(5673);
		l.messageConfig.setPassword("guest");
		l.messageConfig.setUsername("guest");
		l.messageConfig.setEnv("SIT");
		l.init();
		System.out.println("=======");
////		select * from dba_directories;
//		expdp kcrm/kstar schemas=kcrm dumpfile=kcrm.dmp directory=dump_dir ;
//		expdp kcrm/kstar schemas=kcrm dumpfile=kcrm20170604.dmp directory=dump_dir ;
//		impdp kcrm/kstar directory=dump_dir dumpfile=kcrm.dmp schemas=kcrm;
//		impdp kcrm/kstar directory=dump_dir dumpfile=kcrm20170527.dmp schemas=kcrm table_exists_action=replace
		
	}
}

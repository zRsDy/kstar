package com.ibm.kstar.cache;

import java.io.IOException;


public class TestCache {
	
	public static void main(String[] args) throws IOException {
		CacheData t = new CacheData();
		t.setEnv("SIT");
		t.setConf("10.2.1.59:11211");
		t.initMemCached();
		
		t.set("aa", "aaa");
		System.out.println(t.get("aa"));
		t.delete("aa");
		
//		long start = System.currentTimeMillis();
//		
//		String filePath = "C:\\Users\\Administrator\\Desktop\\KSTAR_CRM_TECH_技术方案设计_V0.1_20161230.docx";
//		File file = new File(filePath);
//		byte[] data = Files.readAllBytes(file.toPath());
//		
//		t.set("28ECC8F156E64EE8B75AB15D5D335A57", data);
//		
//		long start2 = System.currentTimeMillis();
//		
//		System.out.println(start2 - start);
//		
//		byte[] data2 = (byte[])t.get("28ECC8F156E64EE8B75AB15D5D335A57");
//		
//		System.out.println("======================="+data.equals(data2));
//		System.out.println("======================="+data.length);
//		System.out.println("======================="+data2.length);
//		
//		long start3 = System.currentTimeMillis();
//		
//		System.out.println(start3 - start2);
//		
//		Files.write(new File("C:\\Users\\Administrator\\Desktop\\KSTAR_CRM_TECH_技术方案设计_V0.22_20161230.docx").toPath(), data2);
//		
//		System.out.println(System.currentTimeMillis() - start3);
		
	}

}


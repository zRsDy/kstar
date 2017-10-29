package com.ibm.kstar.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xsnake.web.utils.StringUtil;

public class LinkedCacheData implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final int MAX_LENG = 1024 * 800;
	
	List<String> keys = new ArrayList<String>();
	transient Map<String,byte[]> datas = new HashMap<String,byte[]>();
	
	Map<String, byte[]> getDatasMap() {
		if(datas == null){
			datas = new HashMap<String,byte[]>();
		}
		return datas;
	}

	public LinkedCacheData(final byte[] data){
		int size = data.length / MAX_LENG;
		size = size + (data.length % MAX_LENG == 0 ? 0 : 1);
		for(int i=0;i<size;i++){
			String uuid = StringUtil.getUUID();
			keys.add(uuid);
			if(i<(size-1)){
				datas.put(uuid, Arrays.copyOfRange(data, i * MAX_LENG, i * MAX_LENG + MAX_LENG));
			}else{
				datas.put(uuid, Arrays.copyOfRange(data, i * MAX_LENG, data.length));
			}
		}
	}
	
	public byte[] getData(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for(String key : keys){
			try {
				baos.write(datas.get(key));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}
}

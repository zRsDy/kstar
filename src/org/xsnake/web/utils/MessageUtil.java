package org.xsnake.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MessageUtil {

	public static <T> byte[] objectToBytes(T object) throws IOException{
		if(!(object instanceof Serializable)){
			throw new UnsupportedOperationException(object.getClass().getName() + " 非Serializable类型");
		}
		ObjectOutputStream out = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			out = new ObjectOutputStream(os);
			out.writeObject(object);
		}finally{
			out.close();
			os.close();
		}
		return os.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T bytesToObject(byte[] bytes) throws IOException{
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(is);
		try {
			T t = (T) in.readObject();
			return t;
		} catch (ClassNotFoundException e) {
			throw new IOException(" Class Not Found :"+e.getMessage());
		}
	}
	
}

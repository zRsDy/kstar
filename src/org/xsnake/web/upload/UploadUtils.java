package org.xsnake.web.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.xsnake.web.context.MessageContext;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.conf.Configuration;

public class UploadUtils {

	public static List<IUploadFile> uploadFile(HttpServletRequest request,String path){
		List<IUploadFile> files = uploadToDisk(request, path); 
		try {
			uploadToServer(path, files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}

	private static List<IUploadFile> uploadToServer(String path,List<IUploadFile> files) throws IOException{
		Configuration configuration = (Configuration)MessageContext.getBean("configuration");
		BaseDao baseDao = (BaseDao)MessageContext.getBean("baseDao");
		for(IUploadFile file : files){
			File f = new File(file.getRealPath());
			byte[] buff = Files.readAllBytes(Paths.get(f.getPath()));
			String uuid = StringUtil.getUUID();
			CacheData.getInstance().set(uuid, buff);
			((UploadFileImpl)file).setWebPath(configuration.getFileDownloadUrl()+"/"+uuid+".html");
			((UploadFileImpl)file).setPath(path);
			List<Object> args = new ArrayList<>();
			args.add(StringUtil.getUUID());
			args.add(path);
			args.add(uuid);
			args.add(file.getName());
			args.add("0");
			args.add(new Date());
			baseDao.executeSQL(" insert into sys_t_files (ROW_ID,PATH,MD5_KEY,FILE_NAME,STATUS,UPLOAD_DT) values(?, ?, ?, ?, ?, ?)",args.toArray());
		}
		return files;
	}
	
	private static List<IUploadFile> uploadToDisk(HttpServletRequest request, String path) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   
		Map<String, List<MultipartFile>>  map = multipartRequest.getMultiFileMap();
		Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		List<IUploadFile> result = new ArrayList<IUploadFile>();
		while (it.hasNext()) {
			String filedName = it.next();
			List<MultipartFile> list = (List<MultipartFile>) map.get(filedName);
			for(MultipartFile file:list) {
				UploadFileImpl temp = uploadFileToDisk(file, multipartRequest, path, result, filedName);
				if(temp!=null) {
					result.add(temp);
				}
			}
			
		}
		return result;
	}
	
	private static UploadFileImpl uploadFileToDisk(MultipartFile file,HttpServletRequest request, String path,List<IUploadFile> result,String filedName){
		if(file.getSize()>0){
			String fileName = file.getOriginalFilename();
			String fileType = "";
			if(fileName.lastIndexOf(".") > 0){
				fileType = fileName.substring(fileName.lastIndexOf("."));
			}
			String newName = new StringBuffer(path).append("/").append(UUID.randomUUID().toString()).append(fileType).toString(); 
			File dir = new File(request.getSession().getServletContext().getRealPath("/")+path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File f = new File(request.getSession().getServletContext().getRealPath("/")+newName);
			if(!f.exists()){
				OutputStream out = null;
				try {
					f.createNewFile();
					out = new BufferedOutputStream(new FileOutputStream(f));
					out.write(file.getBytes());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			UploadFileImpl uf = new UploadFileImpl();
			uf.setName(file.getOriginalFilename());
			uf.setFieldName(filedName);
			uf.setRealPath(f.getPath());
			uf.setSize(file.getSize());
			uf.setWebPath(newName);
			uf.setContentType(file.getContentType());
			uf.setSuffix(fileType);
			return uf;
		}
		return null;
	}
}

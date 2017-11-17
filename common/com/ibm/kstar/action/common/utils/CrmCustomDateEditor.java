package com.ibm.kstar.action.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.StringUtils;

public class CrmCustomDateEditor extends CustomDateEditor{


	public CrmCustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException{
        if(!StringUtils.hasText(text)){
            setValue(null);
        }else{
        	String formate = null;
    		if(text.matches("^\\d{4}-\\d{1,2}$")){ 
    			formate= "yyyy-MM";
    		}else if(text.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
    			formate= "yyyy-MM-dd";
    		}else if(text.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
    			formate= "yyyy-MM-dd HH:mm";
    		}else if(text.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
    			formate= "yyyy-MM-dd HH:mm:ss";
    		}else {
    			throw new IllegalArgumentException("Invalid boolean value '" + text + "'");
    		}
        	try{
            	if(formate != null){
        			setValue(parseDate(text,formate));
        		}
            }catch(Exception ex){
                throw new IllegalArgumentException((new StringBuilder()).append("Could not parse date: ").append(ex.getMessage()).toString(), ex);
            }
        }
    }
	
	@Override
    public String getAsText(){
        Date value = (Date)getValue();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return value == null ? "" : dateFormat.format(value);
    }
	
	private  Date parseDate(String dateStr, String format) throws Exception {
		Date date=null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			date = (Date) dateFormat.parse(dateStr);
		} catch (Exception e) {
			throw e;
		}
		return date;
	}

}

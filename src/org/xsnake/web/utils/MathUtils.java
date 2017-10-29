package org.xsnake.web.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MathUtils {
	
	
	/**
	 * 加法
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double add(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		BigDecimal cc = aa.add(bb);
		
		return cc.doubleValue();
		
	}
	
	/**
	 * 减法
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double sub(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		BigDecimal cc = aa.subtract(bb);
		
		return cc.doubleValue();
		
	}
	
	/**
	 * 乘法
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double mul(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		BigDecimal cc = aa.multiply(bb);
		
		return cc.doubleValue();
		
	}
	
	/**
	 * 乘法
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal mul2(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		return aa.multiply(bb);
		
	}
	
	/**
	 * 除法
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double div(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		BigDecimal cc = aa.divide(bb,2,BigDecimal.ROUND_FLOOR);
		
		return cc.doubleValue();
		
	}
	
	
	/**
	 * 除法
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal div2(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		return aa.divide(bb,2,BigDecimal.ROUND_FLOOR);
		
	}
	
	
	/**
	 * 
	 * 方法描述：获取百分比
	 *
	 * @param a 被除数
	 * @param b 除数
	 * @return
	 * @author WangYuTao
	 */
	public static String divStr(Double a,Double b){
		
		if(a==null){
			a = 1d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		BigDecimal cc = bb.divide(aa, 4 ,BigDecimal.ROUND_UP);
		
		return cc.doubleValue()*100+"%";
		
	}
	
	
	
	/**
	 * 除法
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double div4(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		return aa.divide(bb,4,BigDecimal.ROUND_FLOOR).doubleValue();
	}
	
	
	public static Double div6(Double a,Double b){
		if(a==null){
			a = 0d;
		}
		if(b==null){
			b = 0d;
		}
		BigDecimal aa = new BigDecimal(Double.toString(a));
		
		BigDecimal bb = new BigDecimal(Double.toString(b));
		
		return aa.divide(bb,6,BigDecimal.ROUND_FLOOR).doubleValue();
	}
	
	
	public static Double format(Double d){
		
		DecimalFormat df=new DecimalFormat("#.00");
		
		String r = df.format(d);
		
		return Double.valueOf(r);
	}
	
	
	public static String format2(BigDecimal d){
		
		DecimalFormat formatWithoutFraction = new DecimalFormat("###");
        DecimalFormat formatWithFraction = new DecimalFormat("###.###");
        if (new BigDecimal(d.intValue()).compareTo(d) == 0) {
            return formatWithoutFraction.format(d);
        }
        return formatWithFraction.format(d);
	}
	
	
	public static Double getPrecision(Double d,Integer size){
		if(size==null)
			size = 1;
		if(size==0)
			return getInt(d);
		else if(size==1)
			return getPrecision1(d);
		else
			return getPrecision2(d);
	}
	
	/**
	 * 
	 * @return 获取整数部分，只舍不入
	 */
	public static Double getInt(Double d){
		
		String r = Double.toString(d);
		
		if(r.indexOf(".") > -1){
			
			return Double.parseDouble(r.substring(0, r.indexOf(".")));
		}
		
		return d;
	}
	
	/**
	 * 获取小数点后1位，只舍不入
	 * @param d
	 * @return
	 */
	public static Double getPrecision1(Double d){
		
		String r = Double.toString(d);
		
		if(r.indexOf(".") > -1){
			
			r = r+"00";
			
			return Double.parseDouble(r.substring(0, (r.indexOf(".")+2) ));
		}
		
		return d;
	}
	
	/**
	 * 获取小数点后2位，只舍不入
	 * @param d
	 * @return
	 */
	public static Double getPrecision2(Double d){
		
		String r = Double.toString(d);
		
		if(r.indexOf(".") > -1){
			
			r = r+"00";
			
			return Double.parseDouble(r.substring(0, (r.indexOf(".")+3) ));
		}
		
		return d;
	}
	
	public static void main(String[] args) {
		//System.out.println(getPrecision1(1235654.19343));
		//System.out.println(MathUtils.m2(MathUtils.div(25d, 325d).doubleValue(), 2));
		
		System.out.println(divStr(4d,1.1d));
	}

}

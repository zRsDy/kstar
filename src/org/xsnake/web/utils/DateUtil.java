package org.xsnake.web.utils;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wangchao on 2017/3/21.
 */
public class DateUtil {
    /**
     * 日期格式:yyyy-MM-dd
     */
    public static final String DATE_FORMAT_TEN="yyyy-MM-dd";

    /**
     * 日期格式:yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_FORMAT_NINETEEN="yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式:yyyyMMdd
     */
    public static final String DATE_FORMAT_EIGHT="yyyyMMdd";

    /**
     * 日期格式:yyyy-MM
     */
    public static final String DATE_FORMAT_SEVEN="yyyy-MM";

    /**
     * 日期格式:yyyyMM
     */
    public static final String DATE_FORMAT_SIX="yyyyMM";

    /**
     * 日期格式:yyMMdd
     */
    public static final String DATE_FORMAT_SIX_2="yyMMdd";

    /**
     * 日期格式:HHmmss
     */
    public static final String DATE_FORMAT_SIX_3="HHmmss";

    /**
     * 获取系统当前时间
     *
     * @return 系统当前时间
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 根据指定的日期,获取其相对天数days之前的日期 eg: data=2013-09-09 days = 3 返回=2013-09-06
     *
     * @param date
     *            指定的日期
     * @param days
     *            相对天数
     * @return 日期
     */
    public static Date getDateBefore(Date date, int days) {
        if (date == null)
            date = now();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        return now.getTime();
    }

    /**
     * 根据指定的日期,获取其相对月数months之前的日期 eg: data=2013-09-09 month = 1 返回=2013-08-10
     *
     * @param date
     *            指定的日期
     * @param months
     *            相对月数
     * @return 日期
     */
    public static Date getDateBeforeMonth(Date date, int months) {
        if (date == null)
            date = now();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) - months);
        return now.getTime();
    }

    /**
     * 根据指定的日期,获取其相对月数months之后的日期 eg: data=2013-09-09 month = 1 返回=2013-10-10
     *
     * @param date
     *            指定的日期
     * @param months
     *            相对月数
     * @return 日期
     */
    public static Date getDateAfterMonth(Date date, int months) {
        if (date == null)
            date = now();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + months);
        return now.getTime();
    }

    /**
     * 根据指定的日期,获取其相对天数days之后的日期 eg: data=2013-09-09 days = 3 返回=2013-09-12
     *
     * @param date
     *            指定的日期
     * @param days
     *            相对天数
     * @return 日期
     */
    public static Date getDateAfter(Date date, int days) {
        if (date == null)
            date = now();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    /**
     * 根据指定的日期,获取其相对年数years之后的日期 eg: data=2013-09-09 month = 1 返回=2014-09-08
     *
     * @param date
     *            指定的日期
     * @param year
     *            相对年数
     * @return 日期
     */
    public static Date getDateAfterYear(Date date, int year) {
        if (date == null)
            date = now();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.YEAR, now.get(Calendar.YEAR) + year);
        return now.getTime();
    }

    // public static void main(String[] args) {
    // System.out.println(getDate(new Date(),"YYMMdd"));
    // }
    //
    public static String getDate(Date date, String format) {
        if (date == null)
            date = now();
        return new SimpleDateFormat(format).format(date);
    }

    public static String getCurrentDate(String format) {
        return getDate(new Date(), format);
    }

    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd");
    }

    /**
     * 获取当前时间的YYYY-MM-DD简单时间格式
     * @return Date
     */
    public static Date getCurrentSimpleDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return formDate(dateString, "yyyy-MM-dd");
    }

    /**
     * 自定义格式化时间
     *
     * @param date
     *            时间
     * @param formatStr
     *            格式化字符串，默认 MM/dd/yyyy
     * @return 格式化的时间字符串
     */
    public static String formartDate(Date date, String formatStr) {
        if (date == null)
            date = now();
        if (StringUtil.isEmpty(formatStr)) {
            formatStr = "MM/dd/yyyy";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    public static String formartDate2(Date date, String formatStr) {
        if (date == null)
            return null;
        if (StringUtil.isEmpty(formatStr)) {
            formatStr = "MM/dd/yyyy";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 将带时间格式字符转换成时间类型
     *
     * @param dateStr
     *            带时间格式的字符串
     * @param pattern
     *            格式化字符串
     * @return 转换后时间类型数据
     */
    public static Date formDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当天开始时间
     *
     * @return
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        // return todayStart.getTime().getTime();
        return todayStart.getTime();
    }

    /**
     * 获取当天结束时间
     *
     * @return
     */
    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.YEAR, 4028);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 比较两个时间差
     *
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return 两个时间差
     * @throws ParseException
     */

    public static int timeCompare(Date start, Date end) {
        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(start);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(end);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }

    /**
     * 比较两个时间大小
     *
     * @param start
     * @param end
     * @return end - start > 0 true ,否则 false
     */
    public static boolean compare(Date start, Date end) {
        Calendar aCalendar = Calendar.getInstance();

        aCalendar.setTime(start);

        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

        aCalendar.setTime(end);

        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        // return day2 - day1 >= 0 ? true : false;
        return end.getTime() - start.getTime() >= 0 ? true : false;

    }

    /**
     * 比较两个日期大小（不比较时分秒）
     *
     * @param start
     * @param end
     * @return end - start > 0 true ,否则 false
     */
    public static boolean compareDate(Date start, Date end) {
        String startStr = formartDate(start, "yyyy-MM-dd");
        String endStr = formartDate(end, "yyyy-MM-dd");
        Date startD = formDate(startStr, "yyyy-MM-dd");
        Date endD = formDate(endStr, "yyyy-MM-dd");
        return endD.getTime() - startD.getTime() >= 0 ? true : false;
    }

    public static int getYear() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.MONTH) + 1;
    }

    public static int getDate() {
        Calendar a = Calendar.getInstance();
        return a.get(Calendar.DATE);
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getYearFirst() {
        Calendar ca = Calendar.getInstance();
        int Year = ca.get(Calendar.YEAR);

        ca.clear();
        ca.set(Calendar.YEAR, Year);
        Date YearFirst = ca.getTime();
        return YearFirst;
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getYearLast() {
        Calendar ca = Calendar.getInstance();
        int Year = ca.get(Calendar.YEAR);

        ca.clear();
        ca.set(Calendar.YEAR, Year);
        ca.roll(Calendar.DAY_OF_YEAR, -1);

        Date yearLast = ca.getTime();

        return yearLast;

    }

    // public static void main(String[] args) {
    // String str1 = "2014-02-20";
    // String str2 = "2014-12-20";
    // List<String> listB = new ArrayList<String>();
    // List<String> listE = new ArrayList<String>();
    // listB.add("2014-01-01");
    // listE.add("2014-12-30");
    //
    // System.out.println(insertValidate(str1, str2, listB, listE));
    //
    // }

    /**
     * 判断时间交叉
     *
     * @param beginDate
     * @param endDate
     * @param beginDateList
     * @param endDateList
     * @return false 交叉,true，不交叉.
     */
    public static boolean insertValidate(String beginDate, String endDate, List<String> beginDateList, List<String> endDateList) {
        Integer begin = trimChar(beginDate);
        Integer end = trimChar(endDate);
        List<Integer> beginList = new ArrayList<Integer>();
        List<Integer> endList = new ArrayList<Integer>();
        for (int i = 0; i < beginDateList.size(); i++) {
            beginList.add(trimChar(beginDateList.get(i)));
            endList.add(trimChar(endDateList.get(i)));// 一个开始时间对应一个结束时间
        }

        for (int i = 0; i < endList.size(); i++) {
            if (begin < endList.get(i) && end > beginList.get(i)) {
                return false; // 存在重叠
            }
        }

        return true;
    }

    public static Integer trimChar(String str) {
        String year = str.substring(0, 4);
        String yue = str.substring(5, 7);
        String ri = str.substring(8, 10);
        String newString = year + yue + ri;
        System.out.println(newString);
        return Integer.parseInt(newString);
    }

    /**
     * 比较两个时间相差多少天
     *
     * @param startDt
     * @param endDt
     * @return
     */
    public static int getDateDays(Date startDt, Date endDt) {

        long start = startDt.getTime();

        long end = endDt.getTime();
        long a = (end - start) / (1000 * 60 * 60 * 24);
        return (int) a;
    }

    public static Date toStartDate(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 1);
        return c.getTime();
    }

    public static Date toEndDate(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    // 验证是否时间交叉，交叉返回true，没有交叉返回false
    public static boolean isCrossDate(Date start1, Date end1, Date start2, Date end2) {

        if (end1 == null) {

            if (end2 == null) {
                return true;
            }

            if (start1.getTime() > start2.getTime() && start1.getTime() >= end2.getTime()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (end2 == null) {
                if (start2.getTime() > start1.getTime() && start2.getTime() >= end1.getTime()) {
                    return false;
                } else {
                    return true;
                }
            }

            if ((start1.getTime() > start2.getTime() && start1.getTime() >= end2.getTime()) || (end1.getTime() < start2.getTime() && end1.getTime() < end1.getTime())) {
                return false; // 没有交叉
            } else {
                return true;
            }
        }
    }

    //当前日期所在月的第一天
    public static Date getFirstDayOfMonth(Date date) {
        return getFirstDayOfMonth2(date).getTime();
    }
    //当前日期所在月的第一天(返回日期对象)
    public static Calendar getFirstDayOfMonth2(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DATE, 1);
        return ca;
    }
    ///当前日期所在月的最后一天
    public static Date getLastDayOfMonth(Date date) {
        Calendar ca=getFirstDayOfMonth2(date);
        ca.roll(Calendar.DAY_OF_MONTH, -1);
        return ca.getTime();
    }

    /**
     * 日期对象的str
     * @param obj
     * @return
     */
    public static String getDateObjStr(Object obj){
        String dateStr= StringUtils.EMPTY;
        if (null!=obj) {
            if (obj instanceof Timestamp) {
                Timestamp objValue=(Timestamp)obj;
                Date dateValue=new Date(objValue.getTime());
                dateStr=formartDate(dateValue, DATE_FORMAT_NINETEEN);
            }else if(obj instanceof Date){
                Date dateValue=(Date)obj;
                dateStr=formartDate(dateValue, DATE_FORMAT_NINETEEN);
            }else{
                dateStr=String.valueOf(obj);
            }
        }
        return dateStr;
    }

    /**
     * 获得当月的字符串:当前月201605
     * @return
     */
    public static String getCurrMonthSix(){
        return getCurrentDate(DATE_FORMAT_SIX);
    }

    /**
     * 获得上个月的字符串:当前月201605,则返回201604
     * @return
     */
    public static String getBeforeMonthSix(){
        final int months=1;
        Date beforeMonthDate=getDateBeforeMonth(now(), months);
        return formartDate(beforeMonthDate,DATE_FORMAT_SIX);
    }

}


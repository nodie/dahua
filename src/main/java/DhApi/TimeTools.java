package DhApi;/*
 * Copyright (c) AdSame Corporation. All rights reserved.
 * nodie
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TimeTools implements Runnable {

    /** 当前时间，长整形 */
    public static long tm;
    /** 当前时间，整型　*/
    public static int now;
    /** 当前yyyy-mm-dd类型时间　*/
    public static String date;
    /** 比对天 */
    public static String diffdate = "0";
    /** 当前yyyy-mm-dd hh:mm:ss类型时间　*/
    public static String datetime;
    /** 当前dd日　*/
    public static byte day;
    /** 前一天yyyy-MM-dd HH:mm:ss */
    public static String predaytime;
    /** 前一天dd */
    public static byte preday;
    /** 前一天yyyy-MM-dd */
    public static String predate;
    /** 当前hh小时 */
    public static byte hour;
    /** 前一小时yyyy-MM-dd HH:mm:ss */
    public static String prehourtime;
    /** 前一小时hh */
    public static byte prehour;
    /** 前一小时hh的dd */
    public static byte prehourday;
    /** 前一小时hh的yyyy-MM-dd */
    public static String prehourdate;
    /** 比对小时 */
    public static byte diffhour;
    /** 比对小时，用来 flush 小时数据 */
    public static byte diffhour2;
    /** 对比mm分钟 */
    public static byte diffmin;
    /** 当前mm分钟 */
    public static byte min;
    /** 当前秒 */
    public static byte sec;

    /** 间隔等待时间　*/
    public int wait = 0;
    /** 最大运行时间　*/
    public int max = 0;
    /** 处理记录数　*/
    public int num = 0;
    
    
    public TimeTools(int wait) {
        this.wait = wait;
    }

    /**
     * Current Hour, Format yyyy-MM-dd.HH
     *
     * @return String
     */
    public static String getCurrentHour() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd.HH");
        Date todayDate = new Date();
        String currentHour = format.format(todayDate);

        return currentHour;
    }

    /**
     * Previous Hour，Format yyyy-MM-dd.HH
     *
     * @return String
     */
    public static String getPreviousHour() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd.HH");
        Date todayDate = new Date();
        long beforeTime = (todayDate.getTime() / 1000) - 60 * 60;
        todayDate.setTime(beforeTime * 1000);
        String previousHour = format.format(todayDate);

        return previousHour;
    }

    /**
     * Previous Hour Int，Format HH
     *
     * @return int
     */
    public static int getPreviousHourInt() {
        //  当前日期小时，格式 HH
        SimpleDateFormat format = new SimpleDateFormat("HH");
        Date todayDate = new Date();
        long beforeTime = (todayDate.getTime() / 1000) - 60 * 60;
        todayDate.setTime(beforeTime * 1000);
        String previousHour = format.format(todayDate);

        return Integer.parseInt(previousHour);
    }

    /**
     * Current DateTime , Format yyyy-MM-dd
     *
     * @return String
     */
    public static String getCurrentDate() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(System.currentTimeMillis());
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }
    
    /**
     * Current DateTime , Format yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getCurrentDateTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(System.currentTimeMillis());
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }

    /**
     * Current Time Millis，Format long
     *
     * @return long
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Date Time the long millis corresponds，Format yyyy-MM-dd HH:mm:ss
     *
     * @param millis
     * @return String
     */
    public static String getDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date(millis);
        String sDateTime = sdf.format(dt);
        return sDateTime;
    }
    
    
    /**
     * yyyy-MM-dd or yyyy-MM-dd HH:mm:ss
     * return long millis corresponds
     * 
     * @param daytime
     * @return
     */
	public static long getSecond(String daytime) {
	    //SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	    SimpleDateFormat dateFormat2;
	    
	    if (daytime.length() == 10) {
	        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	    } else {
	        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    }
	    
	    Date d;
	    try {
	        d = dateFormat2.parse(daytime);
	    } catch (Exception e) {
	        return -1;
	    }
	    return d.getTime();
	}
    
    
    /**
     * yyyy-MM-dd or yyyy-MM-dd HH:mm:ss
     * return long millis corresponds
     * 
     * @param daytime
     * @return
     */
	public static long getMillSecond(String daytime) {
	    //SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	    SimpleDateFormat dateFormat2;
	    
	    if (daytime.length() == 10) {
	        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	    } else {
	        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	    }
	    
	    Date d;
	    try {
	        d = dateFormat2.parse(daytime);
	    } catch (Exception e) {
	        return -1;
	    }
	    return d.getTime();
	}
	
	/**
	 * yyyy-MM-dd
	 * @return
	 */
	public static String getDay(long l) {
	    Date d = new Date(l);
	    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	    return (dateFormat2.format(d));
	}
	
	/**
	 * yyMMdd
	 * @param time
	 * @return
	 */
	public static int getIntDate(long time) {
	    try {
	        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
	        return Integer.parseInt(formatter.format(time));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	
	/**
	 * return long millis corresponds
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDiffDay(long startDate, long endDate) {
	    try {
	    	
	    	Calendar fromCalendar = Calendar.getInstance();  
	        fromCalendar.setTime(new Date(startDate));  
	        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);  
	        fromCalendar.set(Calendar.MINUTE, 0);  
	        fromCalendar.set(Calendar.SECOND, 0);  
	        fromCalendar.set(Calendar.MILLISECOND, 0);  
	  
	        Calendar toCalendar = Calendar.getInstance();  
	        toCalendar.setTime(new Date(endDate));  
	        toCalendar.set(Calendar.HOUR_OF_DAY, 0);  
	        toCalendar.set(Calendar.MINUTE, 0);  
	        toCalendar.set(Calendar.SECOND, 0);  
	        toCalendar.set(Calendar.MILLISECOND, 0);
	    	
	        //return ((endDate - startDate) / (24 * 60 * 60 * 1000));
	        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	
	    return 0;
	}
	
	/**
	 * return int minute
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffMinute(long startDate, long endDate) {
	    try {
	        return (int) ((endDate - startDate) / (60 * 1000));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	
	    return 0;
	}
	

	/**
	 * return int Seconds
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDiffSeconds(long startDate, long endDate) {
	    try {
	        return (int) ((endDate - startDate) / 1000);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	
	    return 0;
	}
	

    /** 每个访问的时长，忠诚度的子报表 */
    static int timelengthps = 10;
    static int sessionspace = 1800;
    /**
     * 
     * @param timelength
     * @return
     */
	public static int getTimelength(long timelength) {
        int l = (int) (timelength / timelengthps * 10 + 10);
        if (l > sessionspace) {
            //  约束时长字段，不要超过30分钟，当超过30分钟，则合并后续数据
            l = (sessionspace + 1);
        }

        return l;
    }

    public static void main(String[] arg) {
        System.out.println(getMillSecond("2015-03-11 14:48:43:000"));
        System.out.println(getMillSecond("2015-03-11 14:48:43:000"));
        System.out.println(getDiffDay(getMillSecond("2015-03-11 23:59:59:000"), getMillSecond("2015-03-12 00:00:00:000")));
        System.out.println(getDateTime(1433212885706l));
        System.out.println(getDateTime(1433520000000l));
        System.out.println(getDiffDay(1433212885706l, 1433520000000l));
    }

	@Override
	public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        while(true) {
            Calendar calendar = Calendar.getInstance();
            
            //  current day time
            tm = calendar.getTimeInMillis();
            datetime = dateFormat.format(new Date(tm));

            day = Byte.parseByte(datetime.substring(8,10));
            hour = Byte.parseByte(datetime.substring(11, 13));
            min = Byte.parseByte(datetime.substring(14, 16));
            sec = Byte.parseByte(datetime.substring(17, 19));
            date = datetime.substring(0, 10);
            now = (int) (tm / 1000);
            //System.out.println(day + "+" + hour + "+" + min + "+" + date + "+" + now);
            
            
            
            //  pre day time
            predaytime = dateFormat.format(new Date(tm - 86400000));
            
            preday = Byte.parseByte(predaytime.substring(8,10));
            predate = predaytime.substring(0, 10);
            //System.out.println(preday + "+" + predate);
            
            
            
            //  pre hour time
            prehourtime = dateFormat.format(new Date(tm - 3600000));
            
            prehourday = Byte.parseByte(prehourtime.substring(8,10));
            prehour = Byte.parseByte(prehourtime.substring(11, 13));
            prehourdate = prehourtime.substring(0, 10);
            //System.out.println(prehour + "+" + prehourdate);
            
            try {
                TimeUnit.SECONDS.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

    /**
     * 转换时间日期格式字串为long型
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     *  时长
     * @param start
     * @param end
     * @return
     */
    public static long durationTime(String start,String end){

        return convertTimeToLong(end)-convertTimeToLong(start);
    }
}

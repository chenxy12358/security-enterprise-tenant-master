package io.renren.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * 日期操作工具类
 * 
 */
public class DateUtil2 {

	private static Logger log = LoggerFactory.getLogger(DateUtil2.class);

	private static String defaultDatePattern = "yyyy-MM-dd";

	private static String timePattern = "HH:mm:ss";

	private static String datetimePattern = "yyyy-MM-dd HH:mm:ss";

	private static int firstDay = Calendar.MONDAY;

	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, week);
		c.set(Calendar.DAY_OF_WEEK, 1);
		return c.getTime();
	}

	/**
	 * 取得当前日期是第几周, 星期第1天是星期一,年和月算周，要4天及以上才算是1周
	 * 
	 * @return
	 */
	public static int getWeekNumber(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(firstDay); // 星期第1天是星期一
		c.setMinimalDaysInFirstWeek(4); // 年和月算周，要4天及以上才算是1周
		c.setTime(date);
		int weeknum = c.get(Calendar.WEEK_OF_YEAR);
		return weeknum;
	}

	/**
	 * 得到指定日期的星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeek(Date date) {
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int mydate = cd.get(Calendar.DAY_OF_WEEK);
		String showDate = "星期六";
		switch (mydate) {
		case 1:
			showDate = "星期日";
			break;
		case 2:
			showDate = "星期一";
			break;
		case 3:
			showDate = "星期二";
			break;
		case 4:
			showDate = "星期三";
			break;
		case 5:
			showDate = "星期四";
			break;
		case 6:
			showDate = "星期五";
			break;
		default:
			showDate = "星期六";
			break;
		}
		return showDate;

	}

	/**
	 * 将日期转换成字符串，默认是yyyy-MM-dd
	 * 
	 * @param aDate
	 * @return
	 */
	public static final String getDate(Date aDate) {
		return getDateTime(defaultDatePattern, aDate);
	}

	/**
	 * 换指定的格式，将日期转换成字符串
	 * 
	 * @param aMask
	 *            字符串格式如 yyyy-MM-dd
	 * @param strDate
	 * @return 日期
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * 得到指定日期的时分秒
	 * 
	 * @param theTime
	 * @return 返回时分秒 HH:mm:ss
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * 将指定的日期转换成对应的字符串格式
	 * 
	 * @param aMask
	 * @param aDate
	 * @return
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 将指定的日期转换成字符串格式
	 * 
	 * @param aDate
	 * @return
	 */
	public static final String getDateTime(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(datetimePattern);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 得到当前时间，带分秒
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getDateTime(datetimePattern, new Date());
	}

	/**
	 * 得到指定的日期 前加上或减去对应的天数后的日期
	 * 
	 * @param date
	 *            原来日期
	 * @param windage
	 * @return 计算后的日期
	 */
	public static Date adjustDate(Date date, Integer windage) {
		return adjust(date, windage, Calendar.DATE);
	}

	/**
	 * 得到指定的日期 前加上或减去对应年后的日期
	 * 
	 * @param date
	 *            原来日期
	 * @param windage
	 * @return 计算后的日期
	 */
	public static Date adjustYear(Date date, Integer windage) {
		return adjust(date, windage, Calendar.YEAR);
	}

	/**
	 * 得到指定的日期 前加上或减去对应月后的日期
	 * 
	 * @param date
	 *            原来日期
	 * @param windage
	 * @return 计算后的日期
	 */
	public static Date adjustMonth(Date date, Integer windage) {
		return adjust(date, windage, Calendar.MONTH);
	}
	
	/**
	 * 
	* @Description: TODO<p>小时加减</p>
	* @Title: adjustHour 
	* @author bait
	* @date 2016年3月1日 下午8:37:53 
	* @param date
	* @param windage
	* @return
	 */
	public static Date adjustHour(Date date, Integer windage) {
		return adjust(date, windage, Calendar.HOUR);
	}
	
	/**
	 * 
	* @Description: TODO<p>分钟加减</p>
	* @Title: adjustMinute 
	* @author bait
	* @date 2016年3月1日 下午8:38:55 
	* @param date
	* @param windage
	* @return
	 */
	public static Date adjustMinute(Date date, Integer windage) {
		return adjust(date, windage, Calendar.MINUTE);
	}

	/**
	 * 得到输入时间所在月份的第一天和最后一天
	 * 
	 * @param date
	 *            日期格式 yyyy-MM-dd
	 * @return
	 */
	public static String[] getFirstOrLashDayByMonth(String date) {
		String[] dates = new String[2];
		Calendar c = Calendar.getInstance();
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		dates[0] = year + "-" + month + "-" + c.getActualMinimum(Calendar.DAY_OF_MONTH);
		dates[1] = year + "-" + month + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return dates;
	}

	/**
	 * 得到输入时间所在月份的第一天和最后一天
	 * 
	 * @param date
	 *            指定日期
	 * @return
	 */
	public static Date[] getFirstOrLastDayOfMonth(Date date) {
		Calendar firstDate = new GregorianCalendar();
		firstDate.setTime(date);
		Calendar lastDate = (Calendar) firstDate.clone();
		firstDate.set(Calendar.DATE, firstDate.getActualMinimum(Calendar.DAY_OF_MONTH));
		lastDate.set(Calendar.DATE, firstDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new Date[] { firstDate.getTime(), lastDate.getTime() };
	}

	/**
	 * 获取当前月份的第一天和最后一天
	 * 
	 * @param date
	 *            指定日期
	 * @return
	 */
	public static Date[] getFirstOrLastDayOfMonth() {
		return getFirstOrLastDayOfMonth(new Date());
	}

	/**
	 * 得到指定日期的上月或下月第一天和最后一天
	 * 
	 * @param lastOrNext
	 *            0上月，1下月
	 * @param date
	 *            指定的日期
	 * @return
	 */
	public static String[] getDateForLastOrNextMonth(Integer lastOrNext, Date date) {
		if (lastOrNext == 0) {// 上月
			date = adjustMonth(date, -1);
		} else {
			date = adjustMonth(date, 1);
		}
		String str = getDateTime("yyyy-MM-dd", date);
		return getFirstOrLashDayByMonth(str);
	}

	private static Date adjust(Date date, Integer windage, int flag) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(flag, windage);
		return cal.getTime();
	}

	/**
	 * 获取当年的第一天
	 * 
	 * @param year
	 *            为null表示当前年份
	 * @return
	 */
	public static Date getFirstDateOfYear() {
		Calendar cld = Calendar.getInstance();
		cld.set(cld.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		return cld.getTime();
	}

	/**
	 * 获取当年第一天的格式化字符串，格式化参数patter可为null，表示默认格式 yyyy-MM-dd
	 * 
	 * @param year
	 * @param pattern
	 * @return
	 */
	public static String getFirstDateStringOfYear(String pattern) {
		Date date = getFirstDateOfYear();
		return getDateTime(null == pattern ? defaultDatePattern : pattern, date);
	}

	/**
	 * 获取当年的最后一天
	 * 
	 * @param year为null表示当前年份
	 * @return
	 */
	public static Date getLastDateOfYear() {
		Calendar cld = Calendar.getInstance();
		cld.set(cld.get(Calendar.YEAR), 11, 31, 0, 0, 0);
		return cld.getTime();
	}

	/**
	 * 获取当年最后一天的格式化字符串，格式化参数patter可为null，表示默认格式 yyyy-MM-dd
	 * 
	 * @param year
	 * @param pattern
	 * @return
	 */
	public static String getLastDateStringOfYear(String pattern) {
		Date date = getLastDateOfYear();
		return getDateTime(null == pattern ? defaultDatePattern : pattern, date);
	}
	
	/**
     * 两个时间之间相差距离多少天
     * @param one 时间参数 1：
     * @param two 时间参数 2：
     * @return 相差天数
     */
    public static long getDistanceDays(String str1, String str2) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days=0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
    
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }
    
    /**
     * 
    * @Description: TODO<p>Java将Unix时间戳转换成日期</p>
    * @Title: TimeStamp2Date 
    * @author bait
    * @date 2018年1月6日 上午10:52:39 
    * @param timestampString
    * @return
     */
    public static Date TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        return new Date(timestamp);
    }
    
    /**
     * 
    * @Description: TODO<p>Java将Unix时间戳转换成指定格式日期字符串</p>
    * @Title: TimeStamp2Date 
    * @author bait
    * @date 2018年1月6日 上午10:52:59 
    * @param timestampString
    * @param formats
    * @return
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if (StringUtil.isEmpty(formats))
            formats = datetimePattern;
        Long timestamp = Long.parseLong(timestampString) * 1000;
        
        String date =getDateTime(formats, new Date(timestamp));
        return date;
    }
    
    public static void main(String[] args) {
		String num="1515207141";
		System.out.println(TimeStamp2Date(num,""));
	}
}
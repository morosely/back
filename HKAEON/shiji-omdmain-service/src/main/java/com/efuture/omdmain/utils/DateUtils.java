package com.efuture.omdmain.utils;

import com.alibaba.fastjson.JSONObject;
import com.product.exception.ServiceRuntimeException;
import com.product.model.ServiceSession;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String SIMPLEDATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String Date = "yyyy-MM-dd";
    public static final String Time = "HH:mm";

    public static String getYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return yesterday.format(format);
    }

    public static String getCurrentDay() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(format);
    }

    public static String getTime(Date time) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }

    /**
     * 加一天
     *
     * @return
     */
    public static String getAfterDay(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.DATE, 1);
        String afterDay = sdf.format(calendar.getTime());
        return afterDay;
    }

    /**
     * 加 N 天
     *
     * @param day 天数
     * @return
     * @throws Exception
     */
    public static String getAfterDayByDay(int day) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        String afterDay = sdf.format(calendar.getTime());
        return afterDay;
    }

    /**
     * 日期格式化
     *
     * @param session
     * @param paramsObject
     * @param strings
     * @return
     * @throws Exception
     */
    public static void formatDate(ServiceSession session, JSONObject paramsObject, String... strings) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (String field : strings) {
            if (paramsObject.containsKey(field) && StringUtils.isNotBlank(paramsObject.getString(field))) {
                String format = sdf.format(paramsObject.getDate(field));
                paramsObject.put(field, format);
            }
        }
    }

    /**
     * 取当前时间的年月日部分
     *
     * @return
     */
    public static String getDateStringFromNow(Date nowTime) {
        return new SimpleDateFormat(Date).format(nowTime);
    }

    /**
     * 日期格式化
     */
    public static void formatDate(JSONObject paramsObject, String... strings) {
        SimpleDateFormat sdf = new SimpleDateFormat(Date);
        for (String field : strings) {
            if (paramsObject.containsKey(field) && !StringUtils.isEmpty(paramsObject.getString(field))) {
                paramsObject.put(field, sdf.format(paramsObject.getDate(field)));
            }
        }
    }

    /**
     * 时间格式化
     */
    public static void formatTime(JSONObject paramsObject, String... strings) {
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLEDATETIME);
        for (String field : strings) {
            if (paramsObject.containsKey(field) && !StringUtils.isEmpty(paramsObject.getString(field))) {
                paramsObject.put(field, sdf.format(paramsObject.getDate(field)));
            }
        }
    }

    public static void checkUpdateDate(JSONObject paramsObject) throws Exception {
        if (StringUtils.isNotBlank(paramsObject.getString("updateDateStart"))
                && StringUtils.isNotBlank(paramsObject.getString("updateDateEnd"))) {
            SimpleDateFormat sdf = new SimpleDateFormat(Date);

            if (sdf.parse(sdf.format(paramsObject.getDate("updateDateStart"))).getTime() > sdf
                    .parse(sdf.format(paramsObject.getDate("updateDateEnd"))).getTime()) {
                throw new ServiceRuntimeException("同步日期有误请检查!");
            }
        }

        if (paramsObject.containsKey("type") && StringUtils.isNotBlank(paramsObject.getString("type"))) {
            if ("9".equals(paramsObject.getString("type"))) {

            }
        }
    }

    /**
     * 获取昨天结束的日期时间
     *
     * @return yyyy-MM-dd 23:59:59
     */
    public static String getYesterEndDateTime() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDateTime yesterdayEnd = LocalDateTime.of(yesterday.getYear(), yesterday.getMonth(), yesterday.getDayOfMonth(), 23, 59, 59, 1);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return yesterdayEnd.format(format);
    }

    /**
     * 获取昨天零点开始的日期时间
     *
     * @return yyyy-MM-dd 00:00:00
     */
    public static String getYesterDateTime() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return yesterday.format(format) + " 00:00:00";
    }

    public static String getCurrentDayStart() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(format) + " 00:00:00";
    }

    public static String getCurrentDayEnd() {
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return today.format(format);
    }
}

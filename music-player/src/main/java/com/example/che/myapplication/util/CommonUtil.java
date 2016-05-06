package com.example.che.myapplication.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }

    public static ObjectMapper getJacksonMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static Map<String, String> url2Map(String url) {
        url = url.substring(url.lastIndexOf("?") + 1);
        String[] params = url.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            map.put(param.split("=")[0], param.split("=")[1]);
        }
        return map;
    }

    // 获取随机数字
    public static String getRand(int num) {
        String sRand = "";
        Random random = new Random();
        // 取随机产生的认证�?(4位数�?)
        for (int i = 0; i < num; i++) {
            String rand = "";
            // 随机生成数字或�?�字�?
            if (random.nextInt(10) > 5) {
                rand = String.valueOf((char) (random.nextInt(10) + 48));
            } else {
                rand = String.valueOf((char) (random.nextInt(26) + 65));
            }
            sRand += rand;
        }
        return sRand;
    }

    public final static String dateToString(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = null;
        try {
            date = sdf.format(d);
        } catch (Exception e) {
            date = "";
        }
        return date;
    }

    public final static String dateToString(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String date = null;
        try {
            date = sdf.format(d);
        } catch (Exception e) {
            date = "";
        }
        return date;
    }

    public final static String dateTimeToString(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = null;
        try {
            date = sdf.format(d);
        } catch (Exception e) {
            date = "";
        }
        return date;
    }

    public final static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public final static Date stringToDate(String dateString, String format) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public final static Date stringToDateTime(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    // 比较日期是否在有效期�?
    public static Boolean CompareDate(String BeforeDate, String LastDate) {
        Date beforeDate, nowDate, lastDate;

        beforeDate = stringToDate(BeforeDate);
        nowDate = stringToDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        lastDate = stringToDate(LastDate);
        if (beforeDate.getTime() < nowDate.getTime() && lastDate.getTime() > nowDate.getTime()) {
            return true;
        }
        return false;
    }

    // 比较2个日期相差多少天
    public static long CompareDate(String Date1, String Date2, int DateType) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd", Locale.getDefault());
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(Date1);
            d2 = format.parse(Date2);

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(d1);
            cal2.setTime(d2);

            long milliseconds1 = cal1.getTimeInMillis();
            long milliseconds2 = cal2.getTimeInMillis();
            long diff = milliseconds2 - milliseconds1;
            long diffSeconds = diff / 1000;
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            long diffDays = diff / (24 * 60 * 60 * 1000);

            switch (DateType) {
                case 0:
                    return diffSeconds; // 返回秒
                case 1:
                    return diffMinutes; // 返回分
                case 2:
                    return diffHours; // 返回小时
                case 3:
                    return diffDays; // 返回天数
                default:
                    return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static int getVerCode(Context context, String packageName) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return verCode;
    }

    public static String getNowTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = sdf.format(now);
        return date;
    }

    public final static String convertFristToUpperCase(String temp) {
        String frist = temp.substring(0, 1);
        String other = temp.substring(1);
        return frist.toUpperCase(Locale.getDefault()) + other;
    }

    public static String toUTF8(String str) {
        String u = str;
        try {
            u = java.net.URLEncoder.encode(u, "UTF-8");
        } catch (Exception e) {

        }
        return u;
    }

    // 根据随机数来获取不同的布�?
    public static int random(int s) {
        Random r = new Random();
        int i = r.nextInt(s);
        return i;
    }

    // 判断邮箱的函�?
    public static boolean checkEmail(String email) {
        if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
            return true;
        } else {
            return false;
        }
    }

    // 判断手机号码
    public static boolean checkphone(String phonenumber) {
        String phone = "\\d{11}";
        Pattern p = Pattern.compile(phone);
        Matcher m = p.matcher(phonenumber);
        return m.matches();
    }

    public static String obj2Str(Object obj) {
        if (obj != null) {
            return String.valueOf(obj);
        } else {
            return null;
        }
    }

    public static boolean comparisonInt(Integer x1, Integer x2) {
        if (x1 == null && x2 == null) {
            return true;
        }
        if (x1 == null || x2 == null) {
            return false;
        }
        return x1.intValue() == x2.intValue();
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，4,7，其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）
        总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        */
        String telRegex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(email)) return false;
        else return email.matches(telRegex);
    }

    /**
     * 折线图X轴时间显示格式转换
     *
     * @param date
     * @param flag
     * @return
     */
    public static String dateFormat(String date, int flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ss = "HH:mm";
        try {
            Date dd = sdf.parse(date);
            if (flag == 3 || flag == 4) {//最近七天
                if (dd.getMonth() > 9) {
                    ss = "MM月";
                    if (dd.getDay() > 9) {
                        ss = ss + "dd日";
                    } else {
                        ss = ss + "d日";
                    }
                } else {
                    ss = "M月";
                    if (dd.getDay() > 9) {
                        ss = ss + "dd日";
                    } else {
                        ss = ss + "d日";
                    }
                }
            } else if (flag == -1) {
                if (dd.getMonth() > 9) {
                    ss = "MM月";
                    if (dd.getDay() > 9) {
                        ss = ss + "dd日 HH:mm";
                    } else {
                        ss = ss + "d日 HH:mm";
                    }
                } else {
                    ss = "M月";
                    if (dd.getDay() > 9) {
                        ss = ss + "dd日 HH:mm";
                    } else {
                        ss = ss + "d日 HH:mm";
                    }
                }
            }
            String time = new SimpleDateFormat(ss).format(dd);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据生日计算年龄
     *
     * @param birth
     * @return
     */
    public static int getUserAge(String birth) {
        int age = 0;
        if (!isNullOrEmpty(birth)) {
            Date birthDate = stringToDate(birth);
            Date now = new Date();
            SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
            SimpleDateFormat format_M = new SimpleDateFormat("MM");
            String birth_year = format_y.format(birthDate);
            String this_year = format_y.format(now);
            String birth_month = format_M.format(birthDate);
            String this_month = format_M.format(now);
            age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
            if (this_month.compareTo(birth_month) < 0) age -= 1;
            if (age < 0) age = 0;
        }
        return age;
    }

}

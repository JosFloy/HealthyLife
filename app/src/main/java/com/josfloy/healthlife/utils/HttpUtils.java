package com.josfloy.healthlife.utils;

import android.text.TextUtils;
import com.josfloy.healthlife.entity.PMInfo;
import com.josfloy.healthlife.entity.TodayInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下载数据的工具类
 */
public class HttpUtils {
    private static String userAgent = "Mozilla/5.0 (Windows NT 6.1) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/29.0.1547.66 Safari/537.36";
    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private static final String DEF_CHARSET = "UTF-8";
    public static final String HUMIDITY = "%rh";// 湿度单位
    public static final String TEMPERATURE = "°C";// 温度单位

    /**
     * HttpUrl
     */
    public static String getJsonStr(String dataUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String rs;
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-agent", userAgent);
            connection.setUseCaches(false);
            connection.setConnectTimeout(DEF_CONN_TIMEOUT);
            connection.setReadTimeout(DEF_READ_TIMEOUT);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, DEF_CHARSET));
                String strRead;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }
                rs = sb.toString();
                return rs;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    /**
     * 当日的天气
     *
     * @param str
     * @return TodayInfo
     */
    public static TodayInfo parseNowJson(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reason = jsonObject.getString("reason");
            if (TextUtils.equals("successd!", reason)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                JSONObject realtime = data.getJSONObject("realtime");
                JSONObject wind = realtime.getJSONObject("wind");

                // 解析出风的相关信息
                String windspeed = wind.getString("windspeed");// 风速
                String direct = wind.getString("direct");
                String power = wind.getString("power");

                // 解析出天气的相关信息
                JSONObject weather = realtime.getJSONObject("weather");
                String humidity = weather.getString("humidity");
                String info = weather.getString("info");
                String temperature = weather.getString("temperature");

                // 日期、城市、农历等信息
                String date = realtime.getString("date");
                String city_name = realtime.getString("city_name");
                String week = realtime.getString("week");
                String moon = realtime.getString("moon");

                // 构件对象
                TodayInfo todayInfo = new TodayInfo();
                todayInfo.setWindspeed(windspeed);
                todayInfo.setCity_name(city_name);
                todayInfo.setDate(date);
                todayInfo.setDirect(direct);
                todayInfo.setHumidity(humidity);
                todayInfo.setInfo(info);
                todayInfo.setMoon(moon);
                todayInfo.setPower(power);
                todayInfo.setTemperature(temperature);
                todayInfo.setWeek(week);

                return todayInfo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 天气建议
     *
     * @param str
     * @return
     */
    public static List<Map<String, Object>> parseLifeJson(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reason = jsonObject.getString("reason");
            if (TextUtils.equals("successed!", reason)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                JSONObject life = data.getJSONObject("life");
                String date = life.getString("date");// 日期
                // Map<String, Object> map1 = new HashMap<String, Object>();
                // map1.put("date", date);
                // list.add(map1);
                JSONObject info = life.getJSONObject("info");
                // 空调建议
                JSONArray kongtiao = info.getJSONArray("kongtiao");
                list.add(getLifeMaps(kongtiao));
                // 运动建议
                JSONArray yundong = info.getJSONArray("yundong");
                list.add(getLifeMaps(yundong));
                // 紫外线建议
                JSONArray ziwaixian = info.getJSONArray("ziwaixian");
                list.add(getLifeMaps(ziwaixian));
                // 感冒建议
                JSONArray ganmao = info.getJSONArray("ganmao");
                list.add(getLifeMaps(ganmao));
                // 洗车建议
                JSONArray xiche = info.getJSONArray("xiche");
                list.add(getLifeMaps(xiche));
                // 污染建议
                JSONArray wuran = info.getJSONArray("wuran");
                list.add(getLifeMaps(wuran));
                // 穿衣建议
                JSONArray chuanyi = info.getJSONArray("chuanyi");
                list.add(getLifeMaps(chuanyi));

                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 返回map集合
     *
     * @param obj
     * @return
     */
    private static Map<String, Object> getLifeMaps(JSONArray obj) {
        try {
            Map<String, Object> map = new HashMap<>();

            String objStr1 = obj.get(0).toString();
            String objStr2 = obj.get(1).toString();
            // Log.e("Q", objStr1);
            // Log.e("H", objStr2);
            map.put("Q", objStr1);
            map.put("H", objStr2);
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回天气预报信息集合
     *
     * @param str
     * @return List
     */
    public static List<Map<String, Object>> parseWeatherJson(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reason = jsonObject.getString("reason");
            if (TextUtils.equals("successed!", reason)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                JSONArray weather = data.getJSONArray("weather");
                for (int i = 0; i < weather.length() - 1; i++) {
                    Map<String, Object> map = new HashMap<>();
                    // String date =
                    // weather.getJSONObject(i).get("date").toString();
                    // Log.e("weather", date);
                    map.put("date", weather.getJSONObject(i).get("date").toString());// 日期
                    map.put("week", weather.getJSONObject(i).get("week").toString());// 星期
                    map.put("nongli", weather.getJSONObject(i).get("nongli").toString());// 农历
                    // 获取白天和晚上的天气信息
                    JSONObject info = weather.getJSONObject(i).getJSONObject("info");
                    setWeatherDataToMap(info, map);
                    list.add(map);
                }
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向天气集合中添加数据
     *
     * @param josnObj
     * @param mapObj
     */
    private static void setWeatherDataToMap(JSONObject josnObj, Map<String, Object> mapObj) {
        try {
            JSONArray day = josnObj.getJSONArray("day");
            JSONArray night = josnObj.getJSONArray("night");
            // 加入白天的信息
            mapObj.put("DAY_WQ", day.get(0).toString());// 温度-->区间前
            mapObj.put("DAY_TQ", day.get(1).toString());// 天气状况
            mapObj.put("DAY_WH", day.get(2).toString());// 温度-->区间后
            mapObj.put("DAY_WW", day.get(3).toString());// 风向
            mapObj.put("DAY_WJ", day.get(4).toString());// 风级
            mapObj.put("DAY_SJ", day.get(5).toString());// 时间
            // 加入夜晚的信息
            mapObj.put("NIGHT_WQ", night.get(0).toString());
            mapObj.put("NIGHT_TQ", night.get(1).toString());
            mapObj.put("NIGHT_WH", night.get(2).toString());
            mapObj.put("NIGHT_WW", night.get(3).toString());
            mapObj.put("NIGHT_WJ", night.get(4).toString());
            mapObj.put("NIGHT_SJ", night.get(5).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析PM2.5的数据信息
     *
     * @param str
     * @return PMInfo
     */
    public static PMInfo parsePMInfoJson(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String reason = jsonObject.getString("reason");
            if (TextUtils.equals("successed!", reason)) {
                JSONObject result = jsonObject.getJSONObject("result");
                JSONObject data = result.getJSONObject("data");
                JSONObject pm25 = data.getJSONObject("pm25");
                // 构建对象
                PMInfo pmInfo = new PMInfo();
                pmInfo.setDateTime(pm25.getString("dateTime"));
                pmInfo.setCityName(pm25.getString("cityName"));

                JSONObject pm25s = pm25.getJSONObject("pm25");
                pmInfo.setCurPm(pm25s.getString("curPm"));
                pmInfo.setPm25(pm25s.getString("pm25"));
                pmInfo.setPm10(pm25s.getString("pm10"));
                pmInfo.setLevel(pm25s.getString("level"));
                pmInfo.setQuality(pm25s.getString("quality"));
                pmInfo.setDes(pm25s.getString("des"));

                return pmInfo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

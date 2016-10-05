package firstandroid.w.coolwhether.util;

import android.text.TextUtils;

import firstandroid.w.coolwhether.db.CoolWeatherDB;
import firstandroid.w.coolwhether.model.City;
import firstandroid.w.coolwhether.model.County;
import firstandroid.w.coolwhether.model.Province;

/**
 * Created by Administrator on 2016/10/5.
 */
public class Utility {
    //解析和处理服务器返回的省级数据
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allprovince = response.split(",");
            if (allprovince != null && allprovince.length > 0) {
                for (String p : allprovince) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    //解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,
                                                               String response,int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allcity = response.split(",");
            if (allcity != null && allcity.length > 0) {
                for (String c : allcity) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    //解析和处理服务器返回的县级数据
    public static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,
                                             String response,int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allcounty = response.split(",");
            if (allcounty != null && allcounty.length > 0) {
                for (String c : allcounty) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}

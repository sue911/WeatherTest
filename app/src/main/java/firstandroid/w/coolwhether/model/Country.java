package firstandroid.w.coolwhether.model;

/**
 * Created by Administrator on 2016/9/9.
 */
public class Country {
    private String countryName;
    private int id;
    private String countryCode;
    private int cityId;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getCountryName(){
        return countryName;
    }
    public void setCountryName(String countryName){
        this.countryName = countryName;
    }
    public String getCountryCode(){
        return countryCode;
    }
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }
    public int getCityId(){
        return cityId;
    }
    public void setCityId(int provinceId){
        this.cityId = cityId;
    }
}

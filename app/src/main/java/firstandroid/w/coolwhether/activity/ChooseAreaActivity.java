package firstandroid.w.coolwhether.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import firstandroid.w.coolweather.R;
import firstandroid.w.coolwhether.db.CoolWeatherDB;
import firstandroid.w.coolwhether.model.City;
import firstandroid.w.coolwhether.model.County;
import firstandroid.w.coolwhether.model.Province;
import firstandroid.w.coolwhether.util.HttpCallbackListener;
import firstandroid.w.coolwhether.util.HttpUtil;
import firstandroid.w.coolwhether.util.Utility;

/**
 * Created by Administrator on 2016/10/5.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVNCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String>adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String>datalist = new ArrayList<String>();
    //省列表
    private List<Province>provinceList;
    //市列表
    private List<City>cityList;
    //县列表
    private List<County>countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView)findViewById(R.id.list_view);
        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVNCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }
    private void queryProvinces(){
        provinceList = coolWeatherDB.loadProvince();
        if(provinceList.size()>0){
            datalist.clear();
            for(Province province : provinceList){
                datalist.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVNCE;
        }else{
            queryFromServer(null,"province");
        }
    }
    private void queryCities(){
        cityList = coolWeatherDB.loadCity(selectedProvince.getId());
        if(cityList.size()>0){
            datalist.clear();
            for(City city : cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }
    private void queryCounties(){
        cityList = coolWeatherDB.loadCity(selectedCity.getId());
        if(cityList.size()>0){
            datalist.clear();
            for(County county : countyList){
                datalist.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(final String code,final String type) {
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvincesResponse(coolWeatherDB,response);
                }
                else if("city".equals(type)){
                    result = Utility.handleCityResponse(coolWeatherDB,response,selectedProvince.getId());

                }else  if("county".equals(type)){
                    result = Utility.handleCountyResponse(coolWeatherDB,response,selectedCity.getId());
                }
                if(result){
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           closeProgressDialog();
                           if("province".equals(type)){
                               queryProvinces();
                           }else if("city".equals(type)){
                               queryCities();
                           }else if("county".equals(type)){
                               queryCounties();
                           }
                       }
                   });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading~~");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(currentLevel == LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }else{
            finish();
        }
    }
}

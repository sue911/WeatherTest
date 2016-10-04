package firstandroid.w.coolwhether.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/9/9.
 */
public class CoolWeatherDB {
    private static final String DB_NAME = "CoolWeather";
    private static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;
    private CoolWeatherDB(Context context){
        CoolWhetherOpenHelper dbHelper = new CoolWhetherOpenHelper(context,DB_NAME,null,1);
        db = dbHelper.getWritableDatabase();
    }
    public  Synchronized static CoolWeatherDB getInstance(Context context){

    }
}

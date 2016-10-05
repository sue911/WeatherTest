package firstandroid.w.coolwhether.util;

/**
 * Created by Administrator on 2016/10/5.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}

package dmg.com.rg;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import dmg.com.rg.util.Constants;

/**
 * Created by Star on 10/19/16.
 */

public class App extends Application {

    private final static String TAG = Application.class.getSimpleName();
    private static Context appContext;
    public static AsyncHttpClient httpClient;
    public static ImageLoader imageLoader;

    public static Application getInstance() {
        return new App();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Application.onCreate - Initializing application...");
        super.onCreate();
        appContext = getApplicationContext();
        initImageLoader();
        initHttpClient();
    }

    public static Context getContext() {
        return appContext;
    }

    private static void initHttpClient() {
        try {
            httpClient = new AsyncHttpClient();
            httpClient.setTimeout(Constants.REQUEST_TIMEOUT_SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
    }

}

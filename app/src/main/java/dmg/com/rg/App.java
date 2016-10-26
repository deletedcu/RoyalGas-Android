package dmg.com.rg;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import dmg.com.rg.helper.DatabaseAdapter;
import dmg.com.rg.util.Constants;

/**
 * Created by Star on 10/19/16.
 */

public class App extends Application {

    private final static String TAG = Application.class.getSimpleName();
    private static Context appContext;
    public static AsyncHttpClient httpClient;
    public static ImageLoader imageLoader;
    public static DatabaseAdapter dbAdapter;
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;

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
        initDatabaseAdapter();
        preferences = getSharedPreferences("royalgas", 0);
        editor = preferences.edit();
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
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.mipmap.background)
                .showStubImage(R.mipmap.background)
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        imageLoader.init(configuration);
    }

    private static void initDatabaseAdapter() {
        dbAdapter = new DatabaseAdapter(getContext());
        dbAdapter.createDatabase();
    }

}

package dmg.com.rg.ui.layout.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;
import dmg.com.rg.ui.layout.fragment.ImageFrag;
import dmg.com.rg.util.Constants;
import dmg.com.rg.util.NetworkUtils;
import dmg.com.rg.util.ShardData;

/**
 * Created by Star on 11/14/16.
 */

public class SplashActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private Object mObject = new Object();
    private double progress1;
    private double progress2;
    private double progress3;
    private boolean loadedMenu;
    private boolean loadedHome;
    private boolean loadedGallery;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (NetworkUtils.checkNetworkState(this)) {
            syncMenu();
            syncGallery();
            syncHome();
        } else {
            loadMenu();
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setMax(100);
                        for (int incr = 15; incr <= 100; incr += 5) {
                            mProgressBar.setProgress(incr);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        goMainActivity();
                    }
                }
            ).start();
        }
    }

    private void updateProgress() {
        mProgressBar.setMax(100);
        int value = (int) ((progress1 + progress2 + progress3) / 3 * 100);
        mProgressBar.setProgress(value);
    }

    private void checkLoaded() {
        if (loadedMenu && loadedGallery && loadedHome) {
            goMainActivity();
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadMenu() {
        boolean isCache = App.preferences.getBoolean(Constants.ISCACHE_MENU, false);
        if (isCache) {
            Cursor cursor = App.dbAdapter.getMenus();
            if (cursor.getCount() > 0) {
                List<MyMenu> list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(MyMenu.TITLE));
                    String path = cursor.getString(cursor.getColumnIndex(MyMenu.PATH));
                    String icon = cursor.getString(cursor.getColumnIndex(MyMenu.ICON));
                    String image = cursor.getString(cursor.getColumnIndex(MyMenu.IMAGE));
                    String type = cursor.getString(cursor.getColumnIndex(MyMenu.TYPE));

                    MyMenu menu = new MyMenu(title, path, icon, image, type);
                    list.add(menu);
                }

                ShardData.getInstance().setMenuList(list);
            }

            cursor.close();
        }
    }

    private void syncMenu() {
        synchronized (mObject) {
            String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.MENU_URL);
            App.httpClient.get(this, url, new AsyncHttpResponseHandler() {

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    Log.d("progress: ", String.valueOf(bytesWritten) + "/" + String.valueOf(totalSize));
                    if (totalSize > 0) {
                        progress1 = bytesWritten / totalSize > 1 ? 1 : bytesWritten / totalSize;
                        updateProgress();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String strResponse = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        long updateDate = jsonObject.getLong("updateDate");
                        long oldDate = App.preferences.getLong(Constants.UPDATE_MENU, 0);
                        if (updateDate > oldDate) {
                            JSONArray jsonArray = jsonObject.getJSONArray("sections");

                            for (int i = 0; i < jsonArray.length(); i ++) {
                                JSONObject item = jsonArray.getJSONObject(i);

                                ContentValues values = new ContentValues();
                                values.put(MyMenu.TITLE, item.getString(MyMenu.TITLE));
                                values.put(MyMenu.PATH, item.getString(MyMenu.PATH));
                                values.put(MyMenu.ICON, item.getString(MyMenu.ICON));
                                values.put(MyMenu.IMAGE, item.getString(MyMenu.IMAGE));
                                values.put(MyMenu.TYPE, item.getString(MyMenu.TYPE));

                                App.dbAdapter.writeMenu(values);
                            }

                            App.editor.putBoolean(Constants.ISCACHE_MENU, true);
                            App.editor.putLong(Constants.UPDATE_MENU, updateDate);
                            App.editor.commit();
                        }

                        loadMenu();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getLocalizedMessage());
                    } finally {
                        loadedMenu = true;
                        checkLoaded();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    loadMenu();
                    loadedMenu = true;
                    checkLoaded();
                }
            });
        }

    }

    private void syncGallery() {

        synchronized (mObject) {
            String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.HOMEGALLERY_URL);
            App.httpClient.get(this, url, new AsyncHttpResponseHandler() {

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    if (totalSize > 0) {
                        progress2 = bytesWritten / totalSize > 1 ? 1 : bytesWritten / totalSize;
                        updateProgress();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String strResponse = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        long updateDate = jsonObject.getLong("updateDate");
                        long oldDate = App.preferences.getLong(Constants.UPDATE_GALLERY, 0);
                        if (updateDate > oldDate) {
                            JSONArray jsonArray = jsonObject.getJSONArray("sections");
                            if (jsonArray.length() > 0) {
                                App.dbAdapter.removeGallery();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    ContentValues values = new ContentValues();
                                    values.put(ImageFrag.Keys.TITLE, item.getString(ImageFrag.Keys.TITLE));
                                    values.put(ImageFrag.Keys.DESCRIPTION, item.getString(ImageFrag.Keys.DESCRIPTION));
                                    values.put(ImageFrag.Keys.IMAGE_PATH, item.getString(ImageFrag.Keys.IMAGE_PATH));

                                    App.dbAdapter.writeGallery(values);

                                }

                                App.editor.putLong(Constants.UPDATE_GALLERY, updateDate);
                                App.editor.putBoolean(Constants.ISCACHE_GALLERY, true);
                                App.editor.commit();

                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getLocalizedMessage());
                    } finally {
                        loadedGallery = true;
                        checkLoaded();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    loadedGallery = true;
                    checkLoaded();
                }
            });
        }

    }

    private void syncHome() {

        synchronized (mObject) {
            String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.HOME_URL);
            App.httpClient.get(this, url, new AsyncHttpResponseHandler() {

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    if (totalSize > 0) {
                        progress3 = bytesWritten / totalSize > 1 ? 1 : bytesWritten / totalSize;
                        updateProgress();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String strResponse = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        long updateDate = jsonObject.getLong("updateDate");
                        long oldDate = App.preferences.getLong(Constants.UPDATE_HOME, 0);
                        if (updateDate > oldDate) {
                            JSONArray jsonArray = jsonObject.getJSONArray("sections");

                            for (int i = 0; i < jsonArray.length(); i ++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                ContentValues values = new ContentValues();
                                values.put(MyMenu.TITLE, item.getString(MyMenu.TITLE));
                                values.put(MyMenu.PATH, item.getString(MyMenu.PATH));
                                values.put(MyMenu.ICON, item.getString(MyMenu.ICON));
                                values.put(MyMenu.IMAGE, item.getString(MyMenu.IMAGE));
                                values.put(MyMenu.TYPE, item.getString(MyMenu.TYPE));
                                values.put(MyMenu.DESCRIPTION, item.getString(MyMenu.DESCRIPTION));

                                App.dbAdapter.writeHome(values);
                            }

                            App.editor.putBoolean(Constants.ISCACHE_HOME, true);
                            App.editor.putLong(Constants.UPDATE_HOME, updateDate);
                            App.editor.commit();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getLocalizedMessage());
                    } finally {
                        loadedHome = true;
                        checkLoaded();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    loadedHome = true;
                    checkLoaded();
                }
            });
        }

    }

}

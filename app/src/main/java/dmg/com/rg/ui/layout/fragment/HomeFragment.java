package dmg.com.rg.ui.layout.fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import cz.msebera.android.httpclient.Header;
import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;
import dmg.com.rg.ui.adapter.HomeAdapter;
import dmg.com.rg.ui.layout.activity.MyWebViewActivity;
import dmg.com.rg.util.Constants;
import dmg.com.rg.util.ConvertUtils;
import dmg.com.rg.util.DeviceUtils;

/**
 * Created by Star on 10/19/16.
 */

public class HomeFragment extends Fragment {

    private Object mObject = new Object();
    private String TAG = this.getClass().getSimpleName();
    private List<ImageFrag> mGalleryList = new ArrayList<ImageFrag>();
    private List<MyMenu> mHomeList = new ArrayList<MyMenu>();
    private ImagePagerAdapter mGalleryAdapter;
    private HomeAdapter mHomeAdapter;
    private ProgressDialog mLoadingDialog;

    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.viewPager_image)
    ViewPager mViewPager;
    @BindView(R.id.viewPager_countIndicator)
    LinearLayout mViewPagerIndicator;
    @BindView(R.id.gridview)
    GridView mGridView;
    @BindDrawable(R.drawable.active)
    Drawable mOvalBlue;
    @BindDrawable(R.drawable.inactive)
    Drawable mOvalGray;

    @OnItemClick(R.id.gridview)
    void menuItemSelected(View view, int position) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        MyMenu menu = (MyMenu) mHomeAdapter.getItem(position);
        if (menu.getStrType().equals(MyMenu.WEBVIEW_TYPE)) {
            Intent intent = new Intent(getContext(), MyWebViewActivity.class);
            intent.putExtra("link", menu.getStrPath());
            intent.putExtra("title", menu.getStrTitle());
            getContext().startActivity(intent);
        } else if (menu.getStrType().equals(MyMenu.NATIVE_TYPE)) {
            if (menu.getStrTitle().equals("Contact Us")) {
                Snackbar.make(view, "Contact Us", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                String[] recipients = {"m.abdelhadi@gmail.com"};
//                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
//                intent.putExtra(Intent.EXTRA_SUBJECT, "From my phone");
//                intent.putExtra(Intent.EXTRA_TEXT, "Hello, RoyalGas support team.");
//                intent.putExtra(Intent.EXTRA_CC, "");
//                intent.setType("text/html");
//                getContext().startActivity(Intent.createChooser(intent, "Send mail"));
            }
        }

    }

    public HomeFragment() {
        super();
    }

    @NonNull
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView ...");
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);

        initImagePager();
        initGridView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initImagePager() {

        mGalleryAdapter = new ImagePagerAdapter(getFragmentManager(), mGalleryList);
        mViewPager.setAdapter(mGalleryAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toggleDotsIndicator(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        loadGallery();

    }

    private void initGridView() {

        mHomeAdapter = new HomeAdapter(getContext(), mHomeList);
        mGridView.setAdapter(mHomeAdapter);
        loadHome();
    }

    private void addDotsIndicator(int count) {
        int margin = ConvertUtils.convertDpToPixels(getActivity(), 3);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < count; i ++) {
            ImageView dotView = new ImageView(getActivity());
            dotView.setTag(i);
            dotView.setImageDrawable(mOvalGray);
            dotView.setLayoutParams(layoutParams);
            mViewPagerIndicator.addView(dotView);
        }
    }

    private void toggleDotsIndicator(int position) {
        int childViewCount = mViewPagerIndicator.getChildCount();
        for (int i = 0; i < childViewCount; i ++) {
            View childView = mViewPagerIndicator.getChildAt(i);
            if (childView instanceof ImageView) {
                ImageView dotView = (ImageView) childView;
                Object positionTag = dotView.getTag();
                if (positionTag != null && positionTag instanceof Integer) {
                    if ((int) positionTag == position && dotView.getDrawable() != mOvalBlue) {
                        dotView.setImageDrawable(mOvalBlue);
                    } else if ((int) positionTag != position && dotView.getDrawable() == mOvalBlue) {
                        dotView.setImageDrawable(mOvalGray);
                    }
                }
            }
        }
    }

    private void setGridviewLayout(int itemCount) {
        int width = DeviceUtils.getScreenResolution(getContext()).x;
        int rows = 0;
        if (itemCount % 2 == 0) {
            rows = itemCount / 2;
        } else {
            rows = itemCount / 2 + 1;
        }
        int height = width * rows / 2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        mGridView.setLayoutParams(layoutParams);
        mGridView.setColumnWidth(width / 2 - ConvertUtils.convertDpToPixels(getContext(), (float)7.5));
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        private List<ImageFrag> mList;

        public ImagePagerAdapter(FragmentManager fm, List<ImageFrag> list) {
            super(fm);
            mList = list;
        }

        public void setList(List<ImageFrag> list) {
            if (!list.isEmpty()) {
                mList = list;
                notifyDataSetChanged();
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }
    }

    private void loadGallery() {
        boolean isCache = App.preferences.getBoolean(Constants.ISCACHE_GALLERY, false);
        if (isCache) {
            Cursor cursor = App.dbAdapter.getGallerys();
            if (cursor.getCount() > 0) {
                mGalleryList.clear();
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(ImageFrag.Keys.TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(ImageFrag.Keys.DESCRIPTION));
                    String imagePath = cursor.getString(cursor.getColumnIndex(ImageFrag.Keys.IMAGE_PATH));

                    ImageFrag frag = ImageFrag.newInstance(title, description, imagePath);
                    mGalleryList.add(frag);
                }

                mGalleryAdapter.setList(mGalleryList);
                addDotsIndicator(mGalleryList.size());
            } else {
                syncGallery();
            }

            cursor.close();
        } else {
            syncGallery();
        }
    }

    private void syncGallery() {

        synchronized (mObject) {
            String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.HOMEGALLERY_URL);
            App.httpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String strResponse = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("sections");
                        if (jsonArray.length() > 0) {
                            App.dbAdapter.removeGallery();

                            for (int i = 0; i < jsonArray.length(); i ++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                ContentValues values = new ContentValues();
                                values.put(ImageFrag.Keys.TITLE, item.getString(ImageFrag.Keys.TITLE));
                                values.put(ImageFrag.Keys.DESCRIPTION, item.getString(ImageFrag.Keys.DESCRIPTION));
                                values.put(ImageFrag.Keys.IMAGE_PATH, item.getString(ImageFrag.Keys.IMAGE_PATH));

                                App.dbAdapter.writeGallery(values);

                            }

                            App.editor.putBoolean(Constants.ISCACHE_GALLERY, true);
                            App.editor.commit();

                            loadGallery();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getLocalizedMessage());
                    } finally {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

    }

    private void loadHome() {
        boolean isCache = App.preferences.getBoolean(Constants.ISCACHE_HOME, false);
        if (isCache) {
            Cursor cursor = App.dbAdapter.getHomes();
            if (cursor.getCount() > 0) {
                mHomeList.clear();
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(MyMenu.TITLE));
                    String path = cursor.getString(cursor.getColumnIndex(MyMenu.PATH));
                    String icon = cursor.getString(cursor.getColumnIndex(MyMenu.ICON));
                    String image = cursor.getString(cursor.getColumnIndex(MyMenu.IMAGE));
                    String type = cursor.getString(cursor.getColumnIndex(MyMenu.TYPE));
                    String description = cursor.getString(cursor.getColumnIndex(MyMenu.DESCRIPTION));

                    MyMenu menu = new MyMenu(title, description, path, icon, image, type);
                    mHomeList.add(menu);
                }

                mHomeAdapter.setList(mHomeList);
                setGridviewLayout(mHomeList.size());
                mScrollView.smoothScrollTo(0, 0);
            } else {
                syncHome();
            }

            cursor.close();
        } else {
            syncHome();
        }
    }

    private void syncHome() {

        synchronized (mObject) {
            String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.HOME_URL);
            App.httpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String strResponse = new String(responseBody, "UTF-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
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
                        App.editor.commit();
                        loadHome();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getLocalizedMessage());
                    } finally {

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }

    }

}

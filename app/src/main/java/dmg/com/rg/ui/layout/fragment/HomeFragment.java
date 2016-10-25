package dmg.com.rg.ui.layout.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"m.abdelhadi@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "From my phone");
                intent.putExtra(Intent.EXTRA_TEXT, "Hello, RoyalGas support team.");
                intent.putExtra(Intent.EXTRA_CC, "");
                intent.setType("text/html");
                getContext().startActivity(Intent.createChooser(intent, "Send mail"));
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

        loadGalleryImages();

    }

    private void initGridView() {

        mHomeAdapter = new HomeAdapter(getContext(), mHomeList);
        mGridView.setAdapter(mHomeAdapter);
//        mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                MyMenu menu = (MyMenu) mHomeAdapter.getItem(position);
//                Intent intent = new Intent(getContext(), MyWebViewActivity.class);
//                intent.putExtra("link", menu.getStrPath());
//                intent.putExtra("title", menu.getStrTitle());
//                getContext().startActivity(intent);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        loadHomeSections();
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

    private void loadGalleryImages() {
        String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.HOMEGALLERY_URL);
        App.httpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String strResponse = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("sections");
                    for (int i = 0; i < jsonArray.length(); i ++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String title = item.getString(ImageFrag.Keys.TITLE);
                        String description = item.getString(ImageFrag.Keys.DESCRIPTION);
                        String imagePath = item.getString(ImageFrag.Keys.IMAGE_PATH);
                        ImageFrag frag = ImageFrag.newInstance(title, description, imagePath);
                        mGalleryList.add(frag);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getLocalizedMessage());
                } finally {
                    mGalleryAdapter.setList(mGalleryList);
                    addDotsIndicator(mGalleryList.size());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void loadHomeSections() {
        mLoadingDialog = new ProgressDialog(getContext());
        mLoadingDialog.show();
        String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.HOME_URL);
        App.httpClient.get(getContext(), url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    mHomeList.clear();
                    String strResponse = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("sections");
                    for (int i = 0; i < jsonArray.length(); i ++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String title = item.getString(MyMenu.TITLE);
                        String path = item.getString(MyMenu.PATH);
                        String icon = item.getString(MyMenu.ICON);
                        String image = item.getString(MyMenu.IMAGE);
                        String type = item.getString(MyMenu.TYPE);
                        String description = item.getString(MyMenu.DESCRIPTION);
                        MyMenu menu = new MyMenu(title, description, path, icon, image, type);
                        mHomeList.add(menu);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getLocalizedMessage());
                } finally {
                    mHomeAdapter.setList(mHomeList);
                    setGridviewLayout(mHomeList.size());
                    mScrollView.smoothScrollTo(0, 0);
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                        mLoadingDialog = null;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                    mLoadingDialog = null;
                }
            }
        });
    }

}

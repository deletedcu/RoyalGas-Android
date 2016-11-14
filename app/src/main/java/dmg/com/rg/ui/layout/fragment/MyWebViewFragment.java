package dmg.com.rg.ui.layout.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmg.com.rg.R;
import dmg.com.rg.ui.layout.activity.MyWebViewActivity;
import dmg.com.rg.util.NetworkUtils;

/**
 * Created by Star on 10/20/16.
 */

public class MyWebViewFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private String mLink;
    private ProgressDialog loadingDialog;

    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.button_refresh)
    Button mButtonRefresh;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar;

    @OnClick(R.id.button_refresh)
    void refrshView() {
        mWebView.loadUrl(mLink);
    }

    public MyWebViewFragment() {
        super();
    }

    public static MyWebViewFragment newInstance(String link) {
        MyWebViewFragment fragment = new MyWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("link", link);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mLink = arguments.getString("link", "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView...");
        View view = inflater.inflate(R.layout.frag_webview, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    @Override
    public void onDestroy() {
//        if (loadingDialog != null) {
//            loadingDialog.dismiss();
//            loadingDialog = null;
//        }
        super.onDestroy();
    }

    private void initUI() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setAppCachePath( getContext().getCacheDir().getAbsolutePath() );
        mWebView.getSettings().setAllowFileAccess( true );
        mWebView.getSettings().setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (!NetworkUtils.checkNetworkState(getContext())) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        }
        mWebView.loadUrl(mLink);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (NetworkUtils.checkNetworkState(getContext())) {
                mButtonRefresh.setVisibility(View.GONE);
//                if (loadingDialog == null) {
//                    loadingDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
//                } else {
//                    if (!loadingDialog.isShowing())
//                        loadingDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
//                }
            } else {
//                if (loadingDialog != null) {
//                    loadingDialog.dismiss();
//                    loadingDialog = null;
//                }
//                mButtonRefresh.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            if (loadingDialog != null) {
//                loadingDialog.dismiss();
//                loadingDialog = null;
//            }

        }
    }

}

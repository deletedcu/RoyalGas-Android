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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmg.com.rg.R;
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
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        super.onDestroy();
    }

    private void initUI() {
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.loadUrl(mLink);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (NetworkUtils.checkNetworkState(getContext())) {
                mButtonRefresh.setVisibility(View.GONE);
                if (loadingDialog == null) {
                    loadingDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
                } else {
                    if (!loadingDialog.isShowing())
                        loadingDialog = ProgressDialog.show(getContext(), "", "Loading...", true);
                }
            } else {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
                mButtonRefresh.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }

        }
    }

}
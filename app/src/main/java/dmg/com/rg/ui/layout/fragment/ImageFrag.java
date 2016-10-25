package dmg.com.rg.ui.layout.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.ui.view.ProgressBarAnimation;

/**
 * Created by Star on 10/19/16.
 */

public class ImageFrag extends Fragment {

    private String TAG = this.getClass().getSimpleName();
    private int mImageResourceId;
    private String mTitle;
    private String mDescription;
    private String mImagePath;

    @BindView(R.id.image_frag) ImageView mImageView;
    @BindView(R.id.text_title) TextView mTextTitle;
    @BindView(R.id.text_description) TextView mTextDescription;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    public static class Keys {
        static final String TITLE = "title";
        static final String DESCRIPTION = "description";
        static final String IMAGE_PATH = "image";
    }

    public ImageFrag() {
        super();
    }

    public static ImageFrag newInstance(String title, String description, String imagePath) {
        ImageFrag fragment = new ImageFrag();
        Bundle bundle = new Bundle();
        bundle.putString(Keys.TITLE, title);
        bundle.putString(Keys.DESCRIPTION, description);
        bundle.putString(Keys.IMAGE_PATH, imagePath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTitle = arguments.getString(Keys.TITLE);
        mDescription = arguments.getString(Keys.DESCRIPTION);
        mImagePath = arguments.getString(Keys.IMAGE_PATH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView...");
        View view = inflater.inflate(R.layout.item_image, container, false);
        ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        if (!mImagePath.isEmpty()) {

            App.imageLoader.displayImage(mImagePath, mImageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                    ProgressBarAnimation anim = new ProgressBarAnimation(mProgressBar, 0, 100);
                    anim.setDuration(500);
                    mProgressBar.setAnimation(anim);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    mProgressBar.setVisibility(View.GONE);
                    mImageView.setImageResource(R.mipmap.back_5);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mProgressBar.setVisibility(View.GONE);
                    mImageView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
        mTextTitle.setText(mTitle);
        mTextDescription.setText(mDescription);
    }

}

package dmg.com.rg.ui.viewholder;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;
import dmg.com.rg.ui.view.ProgressBarAnimation;

/**
 * Created by Star on 10/20/16.
 */

public class HomeViewHolder {

    private View mView;

    @BindView(R.id.image_pageinfo) ImageView mImageView;
    @BindView(R.id.text_pageinfo_title) TextView mTextTitle;
    @BindView(R.id.text_pageinfo_description) TextView mTextDescription;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    public HomeViewHolder(View view) {
        mView = view;
        ButterKnife.bind(this, view);
    }

    public void setInfo(MyMenu menu) {
        setImage(menu.getStrImagePath());
        setTitle(menu.getStrTitle());
        setDescription(menu.getStrDescription());
    }

    public void setImage(String url) {
        if (url.isEmpty()) {
            mImageView.setImageResource(R.mipmap.background);
            return;
        }
        App.imageLoader.loadImage(url, new ImageLoadingListener() {
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
                mImageView.setImageResource(R.mipmap.background);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                mProgressBar.setVisibility(View.GONE);
                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                mProgressBar.setVisibility(View.GONE);
                mImageView.setImageResource(R.mipmap.background);
            }
        });
//        if (url.isEmpty()) {
//            mImageView.setImageResource(R.mipmap.background);
//        } else {
//            App.imageLoader.displayImage(url, mImageView, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String s, View view) {
//                    ProgressBarAnimation anim = new ProgressBarAnimation(mProgressBar, 0, 100);
//                    anim.setDuration(500);
//                    mProgressBar.setAnimation(anim);
//                    mProgressBar.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onLoadingFailed(String s, View view, FailReason failReason) {
//                    mProgressBar.setVisibility(View.GONE);
//                    mImageView.setImageResource(R.mipmap.background);
//                }
//
//                @Override
//                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                    mProgressBar.setVisibility(View.GONE);
//                    mImageView.setImageBitmap(bitmap);
//                }
//
//                @Override
//                public void onLoadingCancelled(String s, View view) {
//                    mProgressBar.setVisibility(View.GONE);
//                    mImageView.setImageResource(R.mipmap.background);
//                }
//            });
//        }

    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setDescription(String description) {
        mTextDescription.setText(description);
    }

}

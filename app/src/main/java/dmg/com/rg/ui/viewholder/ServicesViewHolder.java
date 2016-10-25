package dmg.com.rg.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmg.com.rg.R;
import dmg.com.rg.model.Services;

/**
 * Created by Star on 10/20/16.
 */

public class ServicesViewHolder {

    @BindView(R.id.image_services)
    ImageView mImageView;
    @BindView(R.id.text_services_title)
    TextView mTextTitle;
    @BindView(R.id.text_pageinfo_description)
    TextView mTextDescription;

    public ServicesViewHolder(View itemView) {
        ButterKnife.bind(this, itemView);
    }

    public void setValues(Services services) {
        setImage(services.getStrImageURL());
        setTitle(services.getStrTitle());
        setDescription(services.getStrDescription());
    }

    public void setImage(String url) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, mImageView);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setDescription(String description) {
        mTextDescription.setText(description);
    }

}

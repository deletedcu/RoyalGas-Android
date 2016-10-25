package dmg.com.rg.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;

/**
 * Created by Star on 10/20/16.
 */

public class MyMenuViewHolder {

    @BindView(R.id.image_menuicon) ImageView mImageIcon;
    @BindView(R.id.text_menu_title) TextView mTextTitle;

    public MyMenuViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void setValues(MyMenu item) {
        setMenuIcon(item.getStrIconURL());
        setMenuTitle(item.getStrTitle());
    }

    public void setMenuIcon(String iconURL) {

        if (!iconURL.isEmpty()) {
            App.imageLoader.displayImage(iconURL, mImageIcon);
        }

    }

    public void setMenuTitle(String title) {
        mTextTitle.setText(title);
    }

}

package dmg.com.rg.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;
import dmg.com.rg.model.PageInfo;
import dmg.com.rg.ui.viewholder.HomeViewHolder;
import dmg.com.rg.util.ConvertUtils;
import dmg.com.rg.util.DeviceUtils;

/**
 * Created by Star on 10/20/16.
 */

public class HomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MyMenu> mList;
    private int itemSize;
    public HomeAdapter(Context mContext) {
        super();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HomeAdapter(Context mContext, List<MyMenu> list) {
        super();
        this.mList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int screenWidth = DeviceUtils.getScreenResolution(mContext).x;
        itemSize = screenWidth / 2 - ConvertUtils.convertDpToPixels(mContext, (float) 7.5);
    }

    public void setList(List<MyMenu> list) {
        if (!list.isEmpty()) {
            this.mList = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pageinfo, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image_pageinfo);
            holder.mTextTitle = (TextView) convertView.findViewById(R.id.text_pageinfo_title);
            holder.mTextDescription = (TextView) convertView.findViewById(R.id.text_pageinfo_description);
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(itemSize, itemSize);
        convertView.setLayoutParams(layoutParams);

        MyMenu menu = (MyMenu) getItem(position);

        holder.mTextTitle.setText(menu.getStrTitle());
        holder.mTextDescription.setText(menu.getStrDescription());
        if (menu.getStrImagePath().isEmpty()) {
            holder.mImageView.setImageResource(R.mipmap.background);
        } else {
            App.imageLoader.displayImage(menu.getStrImagePath(), holder.mImageView);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView mImageView;
        TextView mTextTitle;
        TextView mTextDescription;
        ProgressBar mProgressBar;
    }
}

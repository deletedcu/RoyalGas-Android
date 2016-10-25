package dmg.com.rg.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.List;

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

    public HomeAdapter(Context mContext) {
        super();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public HomeAdapter(Context mContext, List<MyMenu> list) {
        super();
        this.mList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_pageinfo, null);
        } else {
            view = convertView;
        }

        int width = DeviceUtils.getScreenResolution(view.getContext()).x / 2 - ConvertUtils.convertDpToPixels(view.getContext(), (float) 7.5);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
        view.setLayoutParams(layoutParams);

        HomeViewHolder viewHolder = new HomeViewHolder(view);
        MyMenu menu = (MyMenu) getItem(position);

        viewHolder.setInfo(menu);

        return view;
    }
}

package dmg.com.rg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import dmg.com.rg.R;
import dmg.com.rg.model.Services;
import dmg.com.rg.ui.viewholder.ServicesViewHolder;

/**
 * Created by Star on 10/20/16.
 */

public class ServicesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Services> mList;

    public ServicesAdapter(Context context) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ServicesAdapter(Context context, List<Services> list) {
        super();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
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
        View view;
        if (convertView == null)
            view = mInflater.inflate(R.layout.item_services, null);
        else
            view = convertView;

        Services services = (Services) getItem(position);
        ServicesViewHolder viewHolder = new ServicesViewHolder(view);
        viewHolder.setValues(services);

        return view;
    }
}

package dmg.com.rg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;
import dmg.com.rg.ui.viewholder.MyMenuViewHolder;

/**
 * Created by Star on 10/20/16.
 */

public class MyMenuAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MyMenu> mList;

    public MyMenuAdapter(Context context) {
        super();
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyMenuAdapter(Context context, List<MyMenu> list) {
        super();
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = list;
    }

    public void setList(List<MyMenu> list) {
        if (list != null) {
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
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_menu, null);
        } else {
            view = convertView;
        }

        MyMenu item = (MyMenu) getItem(position);
        MyMenuViewHolder viewHolder = new MyMenuViewHolder(view);
        viewHolder.setValues(item);

        return view;
    }
}

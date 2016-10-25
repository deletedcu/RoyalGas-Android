package dmg.com.rg.ui.layout.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import dmg.com.rg.R;
import dmg.com.rg.model.Services;
import dmg.com.rg.ui.adapter.ServicesAdapter;

/**
 * Created by Star on 10/20/16.
 */

public class ServicesFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private ServicesAdapter mAdapter;

    @BindView(R.id.listview_services)
    ListView mListView;

    @OnItemClick(R.id.listview_services)
    void itemClicked(View view, int position) {
        Services item = (Services) mAdapter.getItem(position);
        Log.d(TAG, item.getStrTitle());
    }

    public ServicesFragment() {
        super();
    }

    public static ServicesFragment newInstance() {
        return new ServicesFragment();
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView...");
        View view = inflater.inflate(R.layout.frag_services, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        List<Services> list = new ArrayList<>();
        mAdapter = new ServicesAdapter(getContext(), list);
        mListView.setAdapter(mAdapter);
    }

}
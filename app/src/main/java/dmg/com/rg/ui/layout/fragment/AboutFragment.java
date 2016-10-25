package dmg.com.rg.ui.layout.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dmg.com.rg.R;

/**
 * Created by Star on 10/19/16.
 */

public class AboutFragment extends Fragment {

    private String TAG = this.getClass().getSimpleName();
    private View view;

    public AboutFragment() {
        super();
    }

    @NonNull
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView ...");
        view = inflater.inflate(R.layout.frag_aboutus, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

package dmg.com.rg.ui.layout.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;

import butterknife.BindView;
import dmg.com.rg.R;
import dmg.com.rg.ui.layout.fragment.MyWebViewFragment;

/**
 * Created by Star on 10/25/16.
 */

public class MyWebViewActivity extends AppCompatActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String link = bundle.getString("link");
        String title = bundle.getString("title");
        setTitle(title);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MyWebViewFragment fragment = MyWebViewFragment.newInstance(link);
        ft.replace(R.id.layout_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void initialize() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}

package dmg.com.rg.ui.layout.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;
import dmg.com.rg.App;
import dmg.com.rg.R;
import dmg.com.rg.model.MyMenu;
import dmg.com.rg.ui.adapter.MyMenuAdapter;
import dmg.com.rg.ui.layout.fragment.AboutFragment;
import dmg.com.rg.ui.layout.fragment.HomeFragment;
import dmg.com.rg.ui.layout.fragment.MyWebViewFragment;
import dmg.com.rg.ui.layout.fragment.ServicesFragment;
import dmg.com.rg.util.Constants;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    protected MyMenuAdapter mMenuAdapter;
    private List<MyMenu> mMenuList = new ArrayList<MyMenu>();
    private String mCurrentFragmentTitle = "";

    @BindView(R.id.listview_menu)
    ListView mMenuListView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @OnItemClick(R.id.listview_menu)
    void menuItemClick(View view, int position) {
        MyMenu menu = (MyMenu) mMenuAdapter.getItem(position);

        if (menu.getStrType().equals(MyMenu.WEBVIEW_TYPE)) {
            Log.d(TAG, menu.getStrTitle());
            MyWebViewFragment fragment = MyWebViewFragment.newInstance(menu.getStrPath());
            replaceFragment(fragment, menu.getStrTitle());
        } else if (menu.getStrTitle().equals("Home")) {
            Log.d(TAG, "Home");
            HomeFragment fragment = HomeFragment.newInstance();
            replaceFragment(fragment, menu.getStrTitle());
        } else if (menu.getStrTitle().equals("Contact Us")) {
            Log.d(TAG, "Contact Us");
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] recipients = {"m.abdelhadi@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, "From my phone");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, RoyalGas support team.");
            intent.putExtra(Intent.EXTRA_CC, "");
            intent.setType("text/html");
            startActivity(Intent.createChooser(intent, "Send mail"));
        }

        mDrawLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mToolbar.setNavigationIcon(R.drawable.ic_menu_gallery);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawLayout.openDrawer(GravityCompat.START);
            }
        });

        initMenu();

        setTitle("Home");
        HomeFragment fragment = HomeFragment.newInstance();
        replaceFragment(fragment, "Home");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void initMenu() {

        mMenuAdapter = new MyMenuAdapter(this, mMenuList);
        mMenuListView.setAdapter(mMenuAdapter);
        loadMenu();
    }

    private void replaceFragment(Fragment fragment, String title) {

        if (!title.equals(mCurrentFragmentTitle)) {
            mCurrentFragmentTitle = title;
            setTitle(title);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.layout_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        }

    }

    private void loadMenu() {
        String url = String.format("%s%s", Constants.WEBSERVICE_BASE_URL, Constants.MENU_URL);
        App.httpClient.get(this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String strResponse = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("sections");
                    for (int i = 0; i < jsonArray.length(); i ++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String title = item.getString(MyMenu.TITLE);
                        String path = item.getString(MyMenu.PATH);
                        String icon = item.getString(MyMenu.ICON);
                        String image = item.getString(MyMenu.IMAGE);
                        String type = item.getString(MyMenu.TYPE);
                        MyMenu menu = new MyMenu(title, path, icon, image, type);
                        mMenuList.add(menu);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getLocalizedMessage());
                } finally {
                    mMenuAdapter.setList(mMenuList);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}

package dmg.com.rg.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dmg.com.rg.R;

/**
 * Created by Star on 10/20/16.
 */

public class NetworkUtils {

    public static boolean checkNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean state = networkInfo != null && networkInfo.isConnected();
        if (!state) {
            AlertUtils.showAlert(context,
                    context.getResources().getString(R.string.string_network_error),
                    context.getResources().getString(R.string.string_network_noconnection));
        }
        return state;
    }



}

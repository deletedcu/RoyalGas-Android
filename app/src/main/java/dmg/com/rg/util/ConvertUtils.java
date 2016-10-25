package dmg.com.rg.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Star on 10/19/16.
 */

public class ConvertUtils {

    public static int convertDpToPixels(Context context, float dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertSpToPixels(Context context, float sp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int convertDpToSp(Context context, float dp) {
        int sp = (int) (convertDpToPixels(context, dp) / (float) convertSpToPixels(context, dp));
        return sp;
    }

}

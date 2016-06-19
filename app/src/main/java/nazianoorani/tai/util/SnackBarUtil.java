package nazianoorani.tai.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;

/**
 * Created by nazianoorani on 22/05/16.
 */
public class SnackBarUtil {

    public static void display(Context context, CharSequence text, int duration){
        try {
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), text, duration).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

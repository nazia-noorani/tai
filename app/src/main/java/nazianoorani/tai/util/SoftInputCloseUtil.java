package nazianoorani.tai.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by nazianoorani on 14/06/16.
 */
public class SoftInputCloseUtil {

    public static void closeInput(Activity activity){
    View view = activity.getCurrentFocus();
    if (view != null) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    }
}

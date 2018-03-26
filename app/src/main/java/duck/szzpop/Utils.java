package duck.szzpop;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * ====================================================
 * 作    者：DUCK
 * 版    本：
 * 创建日期：2018/3/23 - 09:15
 * 描    述：
 * 修订历史：
 * ====================================================
 */

public final class Utils {

    private static Utils instance = null;

    private Utils(){}

    public static Utils getInstance(){
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static synchronized void createInstance () {
        if (instance == null) {
            instance = new Utils();
        }
    }

    /**
     * 打开键盘
     *
     * @param view
     */
    public void showKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!inputManager.isActive()) {
            return;
        }
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

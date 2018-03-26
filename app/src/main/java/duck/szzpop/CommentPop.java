package duck.szzpop;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

/**
 * ====================================================
 * 作    者：DUCK
 * 版    本：
 * 创建日期：2018/3/23 - 10:03
 * 描    述：
 * 修订历史：
 * ====================================================
 */

public class CommentPop extends BasePopwindow {

    private EditText et_input;
    private View cancel;
    private View confirm;
    private int height; //窗口高
    public int keyBoradHeight; //软键盘高
    private View view;
    private int screenHeight; //屏幕高

    @Override
    public void destroy() {
        super.destroy();
    }

    public CommentPop(Activity context) {
        super(context);
    }

    @Override
    protected PopupWindow createPop(View view) {
        //屏幕宽
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        PopupWindow pop = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);

        //不能写反  这样 软件盘就会把pop顶上去
        pop.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //设置弹出 消失动画
        pop.setAnimationStyle(R.style.PopupDownAnimationFromBottom);
        return pop;
    }

    @Override
    protected int getPopwindowViewId() {
        return R.layout.pop_comment;
    }

    @Override
    protected void initPopwindowView() {

        view = findPopViewById(R.id.ll_pop);

        et_input = (EditText) findPopViewById(R.id.et_input_comment);

        //这里监听返回键  Activity监听不到 因为此时事件交由EditText处理了
        et_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popIsShowing()) {
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dimissPopwindow();
                            }
                        }, 100);
                    }
                }
                return false;
            }
        });
        cancel = findPopViewById(R.id.tv_cancel);
        confirm = findPopViewById(R.id.tv_confirm);
        addClickListener(cancel, confirm);
    }

    @Override
    protected void click(int viewId) {

        final String text = et_input.getText().toString().trim();

        if (viewId == R.id.tv_cancel) {
            transportAction(BUTTON_CANCEL, text);
            return;
        }

        if (viewId == R.id.tv_confirm) {
            transportAction(BUTTON_SURE, text);
            return;
        }

    }

    /**
     * 弹窗消失 并收起软键盘
     */
    public void hidePop() {
        Utils.getInstance().hideKeyboard(et_input);
        dimissPopwindow();
    }

    /**
     * 显示弹窗 并显示软键盘
     */
    public void showPop() {
        et_input.setText("");
        et_input.postDelayed(new Runnable() {
            @Override
            public void run() {
                et_input.requestFocus();
                Utils.getInstance().showKeyboard(et_input);
            }
        }, 200);

        if (keyBoradHeight == 0 || height == 0) {
            //键盘高 pop的高未测量
            showPopwindowInScreenMiddle();
        } else {
            //键盘高 pop的高已测量
            final View view = context.findViewById(android.R.id.content);
            showPopwindow(view, Gravity.NO_GRAVITY, 0, screenHeight - keyBoradHeight - height);
        }

        //延时获取pop的确切高度
        if (height == 0) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    height = view.getHeight();
                    Log.d(tag, "height : " + height);
                }
            }, 1000);
        }

    }

    /**
     * 实时获取输入内容
     *
     * @return
     */
    public String getText() {
        return et_input.getText().toString();
    }

}

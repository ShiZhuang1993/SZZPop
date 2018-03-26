package duck.szzpop;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * ====================================================
 * 作    者：DUCK
 * 版    本：
 * 创建日期：2017/8/2 10:43
 * 描    述：Popwindow基类
 * 修订历史：
 * ====================================================
 */
public abstract class BasePopwindow implements View.OnClickListener {

    protected final String tag;
    protected PopupWindow ppw;
    protected Activity context;
    protected Object object; //需要传的值
    protected boolean flag = true; //弹出时屏幕是否变暗  true变暗 false不变暗

    public static final int BUTTON_SURE = 10022; //确定按钮被点击
    public static final int BUTTON_CANCEL = 10023; //取消按钮被点击
    public static final int POP_DISMISS = 10024; //popwindow消失的时候
    public static final int POP_CLICK = 10025; //布局中某个控件被点击

    private View view; //popwindow的主布局
    private PopAction pa; //pop的点击回调


    /**
     * 结束释放资源
     */
    public void destroy() {
        if (ppw != null && popIsShowing()) {
            ppw.dismiss();
        }
        context = null;
        ppw = null;
        pa = null;
        view = null;
        object = null;
    }

    public interface PopAction {
        void onAction(int type, Object o);
    }

    public void setAction(PopAction pa) {
        this.pa = pa;
    }

    public BasePopwindow(Activity context) {
        this.tag = getClass().getSimpleName();
        this.context = context;
        initialize();
    }

    //初始化开始
    protected void initialize() {
        int viewId = getPopwindowViewId();

        if (viewId == 0) {
            throw new IllegalStateException("请设置popwindow的布局！！");
        }

        view = LayoutInflater.from(context).inflate(viewId, null);
        this.ppw = createPop(view);

        if (ppw == null) {
            throw new IllegalStateException("请创建popwindow！！");
        }

        //popwindow消失时的监听
        this.ppw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (flag) {
                    backgroundAlpaha(1.0f);
                }
                transportAction(POP_DISMISS, object);
                popDis();
                object = null;
            }
        });

        init();
        initPopwindowView();
        initPopwindowData();
    }

    /**
     * 窗口消失
     */
    protected void popDis() {

    }

    /**
     * 创建pop
     *
     * @param view pop的主布局
     * @return
     */
    protected PopupWindow createPop(View view) {
        return new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //显示popwindow在某个view的中间
    public void showPopwindowMiddle(View parent) {
        showPopwindow(parent, Gravity.CENTER);
    }

    //显示popwindow
    public void showPopwindow(View parent, int gravity) {
        showPopwindow(parent, gravity, 0, 0);
    }

    //在那个控件下显示
    public void showPopwindowBelow(View view) {
        ppw.showAsDropDown(view);
    }

    //显示popwindow
    public void showPopwindow(View parent, int gravity, int x, int y) {

        initDataBeforeShow();

        if (flag) {
            backgroundAlpaha(0.5f);
        }

        //关闭软键盘
/*        final InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);*/

        if (parent != null && parent.getWindowToken() != null) {
            ppw.showAtLocation(parent, gravity, x, y);
        }

    }

    //显示popwindow在屏幕中间
    public void showPopwindowInScreenMiddle() {
        final View view = context.findViewById(android.R.id.content);
        showPopwindowMiddle(view);
    }

    //隐藏popwindow
    public void dimissPopwindow() {
        ppw.dismiss();
    }

    //初始化
    protected void init() {
        ppw.setOutsideTouchable(true);
        ppw.setFocusable(true);
        //透明的背景
        final ColorDrawable dw = new ColorDrawable();
        ppw.setBackgroundDrawable(dw);
    }

    //找寻控件
    protected View findPopViewById(int resourceId) {
        return view.findViewById(resourceId);
    }

    //给控件添加监听
    protected void addClickListener(View... views) {

        if (views == null || views.length == 0) {
            return;
        }

        for (int i = 0; i < views.length; i++) {
            views[i].setOnClickListener(this);
        }

    }

    //接口回调
    protected void transportAction(int type, Object o) {
        if (pa != null) {
            pa.onAction(type, o);
        }
    }

    //改变屏幕背景的透明度
    protected void backgroundAlpaha(float bgAlpha) {
        final WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * pop是否正在显示
     *
     * @return true pop正在显示  false pop没有在显示
     */
    public boolean popIsShowing() {
        if (ppw != null) {
            return ppw.isShowing();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        click(v.getId());
    }

    /**
     * pop show之前动态设定数据
     */
    protected void initDataBeforeShow() {
    }

    /**
     * 初始化Popwindow的固定数据
     */
    protected void initPopwindowData() {
    }

    /**
     * 获取popwindow主界面布局的资源id
     *
     * @return
     */
    protected abstract int getPopwindowViewId();

    /**
     * 初始化Popwindow视图
     */
    protected abstract void initPopwindowView();

    /**
     * 点击事件
     *
     * @param viewId 被点击的view的id
     */
    protected abstract void click(int viewId);


}

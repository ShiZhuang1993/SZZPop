package duck.szzpop;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CommentPop commentPop;
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    private boolean isHeight;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commentPop != null) {
            commentPop.destroy();
            commentPop = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar bar = getSupportActionBar();
        bar.hide();
        pop();

    }

    private void pop() {
        commentPop = new CommentPop(this);
        final TextView tv_text = (TextView) findViewById(R.id.tv_text);

        final View group = findViewById(R.id.rl_group);

        //监听布局变化
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (isHeight) {
                    return;
                }

                Rect r = new Rect();
                group.getWindowVisibleDisplayFrame(r);

                final int height = group.getHeight();
                final int h = r.bottom - r.top;
                final int keyHeight = height - h;

                if (keyHeight > height / 3) {
                    //软键盘弹出了
                    commentPop.keyBoradHeight = keyHeight;
                    isHeight = false;
                }
            }
        };

        group.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        //回调
        commentPop.setAction(new BasePopwindow.PopAction() {
            @Override
            public void onAction(int type, Object o) {

                //会把内容传出来 直接转 或者
                //调用commentPop.getText()这个方法实时获取内容也可以
                final String text = (String) o;

                //点击了发送
                if (type == BasePopwindow.BUTTON_SURE) {
                    tv_text.setText(text);
                    Toast.makeText(MainActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                    commentPop.hidePop();
                    return;
                }

                //点击了取消
                if (type == BasePopwindow.BUTTON_CANCEL) {
                    commentPop.hidePop();
                    return;
                }

                if (type == BasePopwindow.POP_DISMISS) {

                    tv_text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_text.setFocusable(true);
                            tv_text.requestFocus();
                            Utils.getInstance().hideKeyboard(tv_text);
                        }
                    }, 400);

                    return;
                }

            }
        });

        final View view = findViewById(R.id.bt);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_text.setText("");
                commentPop.showPop();
            }
        });
    }

}

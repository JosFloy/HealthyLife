package com.josfloy.healthlife.base;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.josfloy.healthlife.R;

/**
 * Created by Jos on 2019/7/14 0014.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private TextView title_center;
    private ImageView title_left, title_right;
    private RelativeLayout title_relRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutToView();
        initValues();
        setActivityTitle();
        initViews();
        setViewsListener();
        setViewsFunction();
    }

    /**
     * 初始化标题
     */
    public void initTitle() {
        title_center = findViewById(R.id.titles);
        title_left = findViewById(R.id.left_btn);
        title_right = findViewById(R.id.right_btn);
        title_left.setVisibility(View.INVISIBLE);
        title_right.setVisibility(View.INVISIBLE);
        title_relRelativeLayout = findViewById(R.id.title_back);
    }

    public void setMyBackground(int color) {
        title_relRelativeLayout.setBackgroundResource(color);
    }

    /**
     * 设置TextView的下滑线
     *
     * @param view
     */
    public void setTextViewUnderLine(TextView view) {
        Paint paint = view.getPaint();
        paint.setColor(getResources().getColor(R.color.btn_gray));
        paint.setAntiAlias(true);
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        view.invalidate();
    }

    /**
     * 初始化标题
     */
    protected abstract void setActivityTitle();

    /**
     * 初始化窗口
     */
    protected abstract void getLayoutToView();

    /**
     * 设置初始化的值和变量
     */
    protected abstract void initValues();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 初始化控件的监听
     */
    protected abstract void setViewsListener();

    /**
     * 设置相关管功能
     */
    protected abstract void setViewsFunction();

    /**
     * 设置标题的名称
     *
     * @param name
     */
    public void setTitle(String name) {
        title_center.setText(name);
        title_left.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置标题有返回功能 ->可以改变返回键的图标
     *
     * @param name
     * @param activity
     */
    public void setTitle(String name, final Activity activity) {
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    /**
     * 获取标题左边的按钮
     *
     * @param name
     * @return
     */
    public ImageView getTitleLeft(String name) {
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        return title_left;
    }

    /**
     * 设置标题左 中 右 全部显示
     *
     * @param name
     * @param activity
     * @param picID
     */
    public ImageView setTitle(String name, final Activity activity, int picID) {
        title_center.setText(name);
        title_left.setVisibility(View.VISIBLE);
        title_right.setVisibility(View.VISIBLE);
        if (picID != 0) {
            title_right.setImageResource(picID);
        }
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return title_right;
    }

    /**
     * 设置标题的文字颜色
     *
     * @param colorID
     */
    public void setTitleTextColor(int colorID) {
        title_center.setTextColor(colorID);
    }

    /**
     * 设置标题左侧图片按钮的图片
     *
     * @param picID
     */
    public void setTitleLeftImage(int picID) {
        title_left.setImageResource(picID);
    }

    /**
     * 设置标题右侧图片按钮的图片
     *
     * @param picID
     */
    public void setTitleRightImage(int picID) {
        title_right.setImageResource(picID);
    }
}

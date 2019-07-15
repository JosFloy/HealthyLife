package com.josfloy.healthlife.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.josfloy.healthlife.R;
import com.josfloy.healthlife.activity.PlayActivity;
import com.josfloy.healthlife.base.BaseFragment;
import com.josfloy.healthlife.entity.PMInfo;
import com.josfloy.healthlife.entity.TodayInfo;
import com.josfloy.healthlife.service.StepCounterService;
import com.josfloy.healthlife.utils.Constants;
import com.josfloy.healthlife.utils.HttpUtils;
import com.josfloy.healthlife.utils.SaveKeyValues;
import com.josfloy.healthlife.utils.StepDetector;
import mrkj.library.wheelview.circlebar.CircleBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * * 运动
 * * 功能介绍显示所在城市的空气质量和温度
 * * 展示走步的进度
 * * 展示已走的里程和消耗的热量
 * * 跳转界面的功能按钮
 */
public class SportFragment extends BaseFragment {

    private static final int WEATHER_MESSAGE = 1;//显示天气信息
    private static final int STEP_PROGRESS = 2;//显示步数信息

    private View view;//界面的布局
    private TextView city_name, city_temperature, city_air_quality;//展示天气相关控件
    //显示精度的圆形进度条
    private CircleBar circleBar;//进度条
    private TextView show_mileage, show_heat, want_steps;//显示里程和热量
    private ImageButton warm_btn;//跳转按钮
    //下载天气预报的相关信息
    private TodayInfo todayInfo;//今日的天气
    private PMInfo pmInfo;//今日空气质量
    private String weather_url;//天气接口
    private String query_city_name;//城市名称
    //展示进度、里程、热量的相关参数
    private int custom_steps;//用户的步数
    private int custom_step_length;//用户的步长
    private int custom_weight;//用户的体重
    private Thread get_step_thread; // 定义线程对象
    private Intent step_service;//计步服务
    private boolean isStop;//是否运行子线程
    private Double distance_values;// 路程：米
    private int steps_values;//步数
    private Double heat_values;//热量
    private int duration;//动画时间
    private Context context;

    //传值
    private Handler handler = new Handler(new Handler.Callback() {
        @SuppressLint("SetTextI18n")
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WEATHER_MESSAGE: //天气信息网络请求 结束后跳到这里
                    String jsonStr = (String) msg.obj;
                    //获取Json数据
                    if (jsonStr != null) {
                        //获取下载的Json数据并进行相应的设置
                        setDownLoadMessageToView(jsonStr);
                    }
                    break;
                case STEP_PROGRESS://步数跟进后会调到这里
                    //获取计数的步数
                    steps_values = StepDetector.CURRENT_STEP;
                    //把步数的进度显示在进度条上
                    circleBar.update(steps_values, duration);
                    duration = 0;
                    //存储当前的布数
                    SaveKeyValues.putIntValues("sport_steps", steps_values);

                    //计算里程
                    distance_values = steps_values * custom_step_length * 0.01 * 0.001;//km
                    show_mileage.setText(formatDouble(distance_values) + context.getString(R.string.km));
                    //存储值
                    SaveKeyValues.putStringValues("sprot_distance",
                            formatDouble(distance_values));
                    //消耗热量:跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
                    heat_values = custom_weight * distance_values * 1.036;
                    //展示信息
                    show_heat.setText(formatDouble(heat_values) + context.getString(R.string.cal));
                    //存值
                    SaveKeyValues.putStringValues("sport_heat",
                            formatDouble(heat_values));
                    break;
            }
            return false;
        }
    });

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * 把下载的数值解析后赋值给相关的控件
     *
     * @param resultStr 数值
     */
    @SuppressLint("SetTextI18n")
    private void setDownLoadMessageToView(String resultStr) {
        todayInfo = HttpUtils.parseNowJson(resultStr);// 获取当日的天气信息
        pmInfo = HttpUtils.parsePMInfoJson(resultStr);// 获取PM2.5的数据
        if (todayInfo != null && pmInfo != null) {
            if (isAdded()) {
                city_name.setText(context.getString(R.string.city) + query_city_name);
                city_temperature.setText(context.getString(R.string.temperature_hint) + todayInfo.getTemperature() + getString(R.string.temperature_unit));
                city_air_quality.setText(context.getString(R.string.quality) + pmInfo.getQuality());
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        circleBar = view.findViewById(R.id.show_progress);
        city_name = view.findViewById(R.id.city_name);
        city_temperature = view.findViewById(R.id.temperature);
        city_air_quality = view.findViewById(R.id.air_quality);
        warm_btn = view.findViewById(R.id.warm_up);
        show_mileage = view.findViewById(R.id.mileage_txt);
        show_heat = view.findViewById(R.id.heat_txt);
        want_steps = view.findViewById(R.id.want_steps);
    }

    /**
     * 设置相关属性
     */
    @SuppressLint("SetTextI18n")
    private void setNature() {
        //设置初始进度
        circleBar.setcolor(R.color.theme_blue_two);//设置进度条的颜色
        circleBar.setMaxstepnumber(custom_steps);//设置进度条的最大值
        getServiceValue();

        //跳转界面的按钮
        warm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "跳转热身界面！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), PlayActivity.class)
                        .putExtra("play_type", 0).putExtra("what", 0));
            }
        });
        want_steps.setText("今日目标：" + custom_steps + "步");
    }

    /**
     * 获取计步服务的信息
     */
    private void getServiceValue() {
        if (get_step_thread == null) {
            get_step_thread = new Thread() {// 子线程用于监听当前步数的变化

                @Override
                public void run() {
                    super.run();
                    while (!isStop) {
                        try {
                            //Log.e("进入子线程","进入方法体！");
                            Thread.sleep(1000);//每个一秒发送一条信息给UI线程
                            if (StepCounterService.FLAG) {
                                handler.sendEmptyMessage(STEP_PROGRESS);// 通知主线程
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            get_step_thread.start();
        }
    }

    /**
     * 计算并格式话doubles 数值，保留两位有效数字
     *
     * @param doubles 数值
     * @return 保留两位数的数值
     */
    private String formatDouble(Double doubles) {
        DecimalFormat format = new DecimalFormat("####.##");
        String distanceStr = format.format(doubles);
        return distanceStr.equals("0") ? "0.00" : distanceStr;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(get_step_thread);
        isStop = true;
        get_step_thread = null;
        steps_values = 0;
        duration = 800;
    }

    /**
     * 创建视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport, null);
        initView();//
        initValues();
        setNature();

        //提示
        if (StepDetector.CURRENT_STEP > custom_steps) {
            Toast.makeText(getContext(), "您已达到目标步数,请适量运动！"
                    , Toast.LENGTH_LONG).show();
        }
        //提示弹窗
        if (SaveKeyValues.getIntValues("do_hint", 0) == 1
                && (System.currentTimeMillis() > (SaveKeyValues
                .getLongValues("show_hint", 0) + Constants.DAY_FOR_24_HOURS))) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("提示")
                    .setMessage("你还有计划没有完成")
                    .setPositiveButton("点击确定不再提示", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SaveKeyValues.putIntValues("do_hint", 0);
                        }
                    })
                    .create()
                    .show();
        }
        return view;
    }

    /**
     * 初始化相关的属性
     */
    private void initValues() {
        //1、获取所在城市并获取该城市的天气信息
        query_city_name = SaveKeyValues.getStringValues("city", "北京");
        try {
            //使用URLEncoder这个方法
            // 请在gradle中依赖
            // compile 'org.apache.httpcomponents:httpcore:4.4.10'
            weather_url = String.format(Constants.GET_DATA,
                    URLEncoder.encode(query_city_name, "utf-8"));
            downLoadDataFromNet(weather_url);//下载网络数据
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //2、获取计算里程和热量的相关参数-->默认步数：1000、步长：70cm、体重：50kg
        isStop = false;
        duration = 800;
        //获取默认值用于计算公里数和消耗的热量
        custom_steps = SaveKeyValues.getIntValues("step_plan", 6000);//用户的步数
        custom_step_length = SaveKeyValues.getIntValues("length", 70);//用户的步长
        custom_weight = SaveKeyValues.getIntValues("weight", 50);//用户的体重
        //开启计步服务
        int history_values = SaveKeyValues.getIntValues("sport_steps", 0);
        int service_values = StepDetector.CURRENT_STEP;
        boolean isLaunch = getArguments().getBoolean("is_launch", false);
        if (isLaunch) {
            StepDetector.CURRENT_STEP = history_values + service_values;
        }

        //开启计步服务
        step_service = new Intent(getContext(), StepCounterService.class);
        getContext().startService(step_service);
    }

    /**
     * 下载数据
     */
    private void downLoadDataFromNet(final String urlString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //下载天气预报
                String str = HttpUtils.getJsonStr(urlString);
                Message message = Message.obtain();
                message.obj = str;
                message.what = WEATHER_MESSAGE;
                //handler 传值
                handler.sendMessage(message);
            }
        }).start();
    }

}

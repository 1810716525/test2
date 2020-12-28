package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /*************************定义变量************************/
    private int cnt = 0;        //记录打到的地鼠个数
    private ImageView mouse;    //定义ImageView对象
    private Handler handler;    //定义Handle对象
    private TextView info;      //定义TextView对象
    public int[][] position = new int[][]{
            {464,337},{1026,351},{1613,365},
            {368,634},{1036,642},{1626,630},
            {359,917},{1028,936},{1692,928}
    }; //表示洞口位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //不显示顶部栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);     //设置横屏模式

        /**************************绑定控件************************/
        mouse = findViewById(R.id.imageView_1);
        info = findViewById(R.id.info);

        /************************获取洞口位置**********************/
        info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    float x = event.getRawX();
                    float y = event.getRawY();
                    Log.i("x:"+x, "y:"+y);
                }
                return false;
            }
        });

        /*************************消息处理************************/
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 0x101) {
                    int index = msg.arg1;                   //获取位置索引值
                    mouse.setX(position[index][0]);         //设置X坐标
                    mouse.setY(position[index][1]);         //设置Y坐标
                    mouse.setVisibility(View.VISIBLE);      //显示地鼠
                }
                super.handleMessage(msg);
            }
        };

        /*********************实现地鼠随机出现*********************/
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;  //记录地鼠位置
                while (!Thread.currentThread().isInterrupted()) {
                    index = new Random().nextInt(position.length);      //生成随机数
                    Message m = handler.obtainMessage();                //创建消息对象
                    m.what = 0x101;                                     //设置消息标志
                    m.arg1 = index;                                     //保存地鼠位置索引值
                    handler.sendMessage(m);                             //发送消息通知
                    try {
                        Thread.sleep(new Random().nextInt(500) + 500);   //休眠一段时间
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /*******************点击地鼠后更新信息*******************/
        mouse.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                v.setVisibility(View.INVISIBLE);    //让地鼠消失
                cnt++;                              //打到地鼠数量+1
                Toast.makeText(MainActivity.this, "打到["+cnt+"]只地鼠", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}

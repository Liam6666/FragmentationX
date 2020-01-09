package me.liam.fragmentation.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import me.liam.fragmentation.demo.R;
import me.liam.fragmentation.demo.wechat.WeChatActivity;
import me.liam.fragmentation.demo.zhihu.ZhiHuActivity;
import me.liam.fragmentation.support.SupportActivity;

/**
 * Created by Augustine on 2019/12/30.
 * <p>
 * email:nice_ohoh@163.com
 */
public class MainActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_weChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WeChatActivity.class));
            }
        });
        findViewById(R.id.btn_zhihu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ZhiHuActivity.class));
            }
        });
    }
}

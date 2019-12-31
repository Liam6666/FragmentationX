package me.liam.fragmentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.liam.support.SupportActivity;
import wechat.WeChatActivity;

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
                startActivity(new Intent(MainActivity.this,WeChatActivity.class));
            }
        });
    }
}

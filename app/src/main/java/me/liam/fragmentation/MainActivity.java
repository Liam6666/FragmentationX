package me.liam.fragmentation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import me.liam.queue.Action;
import me.liam.queue.ActionQueue;
import me.liam.support.SupportActivity;
import me.liam.test.Fragment1;
import me.liam.test.Fragment2;
import me.liam.test.Fragment3;
import me.liam.test.FragmentRoot;
import me.liam.test.SettingsFragment;

public class MainActivity extends SupportActivity {

    private TextView tv1,tv2,tv3;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tv1 = findViewById(R.id.tv1);
//        tv2 = findViewById(R.id.tv2);
//        tv3 = findViewById(R.id.tv3);
//
//        if (findFragmentByClass(Fragment1.class) == null){
//            fragment1 = Fragment1.newInstance();
//            fragment2 = Fragment2.newInstance();
//            fragment3 = Fragment3.newInstance();
//            loadMultipleRootFragments(R.id.container,1,fragment1,fragment2,fragment3);
//        }
//
//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showHideAllFragment(fragment1);
//            }
//        });
//        tv2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showHideAllFragment(fragment2);
//            }
//        });
//        tv3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showHideAllFragment(fragment3);
//            }
//        });
        getSupportActionBar().hide();
        if (findFragmentByClass(Fragment1.class) == null){
            loadRootFragment(R.id.container, Fragment1.newInstance());
        }
    }
}

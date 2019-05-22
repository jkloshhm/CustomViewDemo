package com.example.jkl.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * 自动换行的标签Activity
 *
 * @author jack 2018-07-02
 */
public class LineBreakActivity extends AppCompatActivity {

    private LineBreakLayout lineBreakLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_break);

        lineBreakLayout1 = findViewById(R.id.line_break_layout);
        //lineBreakLayout2 = findViewById(R.id.line_break_layout_buxian);
        //lineBreakLayout1.addLabelAtFirstLabel("buxian", "不限");
        lineBreakLayout1.addLabel("000", "你的那段难", 0);
        lineBreakLayout1.addLabel("001", "阿达撒快快快", 1);
        lineBreakLayout1.addLabel("002", "你到的", 2);
        lineBreakLayout1.addLabel("003", "你的那大大大大大多段难", 3);
        lineBreakLayout1.addLabel("004", "你的intent", 4);
        lineBreakLayout1.setLabelOnClickListener(new LineBreakLayout.LabelOnClickListener() {
            @Override
            public void labelOnClick(View view, String key, String value, int index) {
                lineBreakLayout1.removeLabel(key, index);
                Toast.makeText(LineBreakActivity.this, "你删除了：" + value, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int randomNum = random.nextInt(1000);
                lineBreakLayout1.addLabel("004", "随机标签" + randomNum, 0);
            }
        });

    }
}

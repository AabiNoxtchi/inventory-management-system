package com.example.inventoryui.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryui.R;

public class TestActivity extends AppCompatActivity {

    private Button start;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_sse);
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
       // client = new OkHttpClient();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start();
                startOksse();
            }
        });

    }

    public void startOksse(){}



}
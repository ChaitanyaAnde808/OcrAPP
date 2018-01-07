package com.example.lenovo.ocrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txt=(TextView) findViewById(R.id.txtview);
         Intent ina=getIntent();
        txt.setText(ina.getStringExtra("txt"));
        txt.setMovementMethod(new ScrollingMovementMethod());
    }
}

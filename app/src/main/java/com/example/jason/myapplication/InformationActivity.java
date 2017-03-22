package com.example.jason.myapplication;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;


/**
 * Created by jason on 2017/3/20.
 */

public class InformationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bundle bundle =this.getIntent().getExtras();
        textView.setText("名稱： " + bundle.get("name"));
        textView2.setText("緯度： " + bundle.get("latitude"));
        textView3.setText("經度： " + bundle.get("longitude"));
        textView4.setText("介紹： " + bundle.get("information"));

        String name = (String)bundle.get("name");
        switch (name){
            case "台大體育館":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.gym));
                break;
            case "中正紀念堂":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.memorialhall));
                break;
            case "台北車站":
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.station));
                break;
        }






    }

}

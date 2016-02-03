package com.potato.potato.groceryshopping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScanResult extends ActionBarActivity{

    String dt,price="0";
    Button btn;
    TextView tv;
    ImageView iv;
    appGlobal app;
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        app = (appGlobal) getApplication();

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        dt = getIntent().getStringExtra("dt");

        //assign
        btn = (Button) findViewById(R.id.resAdd);
        tv = (TextView) findViewById(R.id.resDescribe);
        iv = (ImageView) findViewById(R.id.resImg);
        edit = (EditText) findViewById(R.id.quantityEdit);

        tv.setText(dt);

        //
        getSomething();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                int q = Integer.valueOf(edit.getText().toString());
                app.additem(dt,q,Double.valueOf(price));
                app.addPrice(Double.valueOf(price));
                intent.setClass(ScanResult.this,ShoppingCart.class);
                startActivity(intent);
            }
        });
    }

    public void getSomething(){
        new Thread(r).start();
        if(dt.equals("8888572920344")){
            iv.setBackground(getResources().getDrawable(R.drawable.notebook));
            //tv.setText("Name: Exercise notebook\n\nPrice: 1 SGD\n\nCategory: Notebook");
        }
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://50.116.1.90/grocery/getPrice.php?barcode=" + dt);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String result = "",buf = "";
                while ((buf=br.readLine())!=null){
                    result += buf;
                }
                String[] strarr = result.split("\\*\\*\\*");
                if(strarr.length<3){
                    result = "No such item in database";
                }else{
                    result = "\n";
                    result += "Name: "+strarr[0]+"\n\n";
                    result += "Price: "+strarr[1]+"\n\n";
                    result += "Category: "+strarr[2]+"\n\n";
                    price = strarr[1];
                    dt = result;
                }
                Message msg = new Message();
                msg.setTarget(handler);
                msg.sendToTarget();
            }catch (Exception e){
                System.out.println(e);
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv.setText(dt);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

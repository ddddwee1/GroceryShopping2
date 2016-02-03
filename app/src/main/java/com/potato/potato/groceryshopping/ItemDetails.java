package com.potato.potato.groceryshopping;

import android.content.ClipData;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemDetails extends ActionBarActivity {

    ImageView img;
    TextView tv;
    Button map;
    functions func;
    String dt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        dt = getIntent().getStringExtra("dt");

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        //assign the views
        img = (ImageView) findViewById(R.id.itemImg);
        tv = (TextView) findViewById(R.id.itemDescribe);
        map = (Button) findViewById(R.id.gotoMap);

        //
        getSomething();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ItemDetails.this,mapActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getSomething(){
        new Thread(r).start();
        if (dt.equals("milk")){
            img.setBackground(getResources().getDrawable(R.drawable.milk));
            //tv.setText("Brand:\n\n  AA\n\n  AB\n\n  AC\n\n  BB");
        }if(dt.equals("pen")){
            img.setBackground(getResources().getDrawable(R.drawable.pen));
            //tv.setText("Brand:\n\n  AA\n\n  AB\n\n  AC\n\n  BB");
        }
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            try{
                URL url = new URL("http://50.116.1.90/grocery/getBrand.php?category="+dt);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String result = "",buf = "";
                while ((buf = br.readLine())!=null){
                    result += buf;
                }
                String[] bufstr = result.split("\\*\\*\\*");
                result = "\nBrand:\n\n\n";
                for(int i=0;i<bufstr.length;i++){
                    result += (bufstr[i]+"\n\n");
                }
                Message msg = new Message();
                Bundle bd = new Bundle();
                bd.putString("tv",result);
                msg.setData(bd);
                msg.setTarget(handler);
                msg.sendToTarget();
            }catch (Exception e){

            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String str = msg.getData().getString("tv");
            tv.setText(str);
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
//using background picture here
//The brand information will be changed to the item description
//Add the quantity choice
//Send the data to the history (try using intermediate variables or reorganize the bundles)
//If I am using this structure, the database will change.(Every brand should have a description[By changing the item name to description])
//Then we are facing a problem of the clearance of database structure
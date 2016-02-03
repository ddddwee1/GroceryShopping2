package com.potato.potato.groceryshopping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.io.File;

public class MainActivity extends ActionBarActivity {

    RelativeLayout rl;
    ScrollView sv;
    LinearLayout ll;
    functions func;
    final private String historyDir = "sdcard/groceryShopping";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0,110,220)));

        //get the instance of elements
        rl = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        sv = (ScrollView) findViewById(R.id.mainScroll);
        ll = (LinearLayout) findViewById(R.id.mainLL);

        //
        rl.setBackgroundColor(Color.argb(255,180,250,247));
        //sv.setBackgroundColor(Color.argb(80,255,255,255));

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        //Generate the buttons
        createButton(0,this.getResources().getDrawable(R.drawable.announce),"Announce");
        createButton(1,this.getResources().getDrawable(R.drawable.searchpic),"Finder");
        createButton(2,this.getResources().getDrawable(R.drawable.scann),"Scanner");
        createButton(3,this.getResources().getDrawable(R.drawable.cart),"Cart");
        createButton(4,this.getResources().getDrawable(R.drawable.history),"History");

        //set the position of ScrollView
        //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, func.heightScale(0.65), 0, 0);
        //sv.setLayoutParams(lp);

        File f = new File(historyDir);
        if (!f.exists()){
            f.mkdirs();
        }
    }

    View.OnClickListener mainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case 0:
                    //intent.setClass(MainActivity.this,AnnouncementPage.class);
                    intent.setClass(MainActivity.this,mapActivity.class);
                    break;
                case 1:
                    intent.setClass(MainActivity.this, ItemFinder.class);
                    break;
                case 2:
                    intent.setClass(MainActivity.this,QRreader.class);
                    break;
                case 3:
                    intent.setClass(MainActivity.this,ShoppingCart.class);
                    break;
                case 4:
                    intent.setClass(MainActivity.this,BuyHistory.class);
                    break;
            }
            startActivity(intent);
        }
    };

    //Create buttons in code (PS. Sorry that I am not used to use XML file, coding is a simpler way I think)
    public void createButton(int id, Drawable dr,String text){
        Button btn = new Button(this);
        btn.setId(id);
        //Set drawables for buttons
        btn.setCompoundDrawablesWithIntrinsicBounds(dr, null, null, null);
        //Set onClickListener
        btn.setOnClickListener(this.mainListener);
        //Set Text for buttons
        btn.setText(text);
        btn.setBackgroundColor(Color.argb(100, 255, 255, 255));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,10,0,0);
        btn.setLayoutParams(lp);
        ll.addView(btn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
//change the color of drawables to make it more interface friendly
//Get some animation!
//After all it is better to get a background picture. lol
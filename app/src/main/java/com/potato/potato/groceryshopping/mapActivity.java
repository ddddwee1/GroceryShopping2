package com.potato.potato.groceryshopping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class mapActivity extends ActionBarActivity {

    RelativeLayout rl;
    functions func;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());
        //rl.setBackground(getDrawable(R.drawable.fakemap));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item1 = menu.add(0,0,0,"Scan");
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        intent.setClass(mapActivity.this,QRreader.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

}
//Save the pictures in the server (Numeric the shop maps)
//Using the functions to reform the size of the pictures to fit the different size of screens
//Read the key points (nodes) [for the route algorithm]
//draw routes (fucking trouble)
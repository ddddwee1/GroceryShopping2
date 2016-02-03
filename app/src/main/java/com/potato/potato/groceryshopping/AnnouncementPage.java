package com.potato.potato.groceryshopping;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AnnouncementPage extends ActionBarActivity {

    functions func;
    ImageView v1,v2,v3;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_page);

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        //assign the views
        v1 = (ImageView) findViewById(R.id.bigAncmt);
        v2 = (ImageView) findViewById(R.id.smallAncmt1);
        v3 = (ImageView) findViewById(R.id.smallAncmt2);
        ll = (LinearLayout) findViewById(R.id.ancll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

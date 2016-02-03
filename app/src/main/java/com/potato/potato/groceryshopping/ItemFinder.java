package com.potato.potato.groceryshopping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemFinder extends ActionBarActivity {

    SearchView sv;
    functions func;
    ListView lv;
    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> items2 = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_finder);

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));



        //assign the Search View
        sv = (SearchView) findViewById(R.id.searchItem);
        lv = (ListView) findViewById(R.id.searchList);

        //initialize items
        initializeItem();


        //set onClickListener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("dt",items2.get(position));
                intent.setClass(ItemFinder.this,ItemDetails.class);
                startActivity(intent);
            }
        });

        //Set query listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listFilter(newText);
                lv.setAdapter(new ArrayAdapter<String>(ItemFinder.this,android.R.layout.simple_expandable_list_item_1,items2));
                return false;
            }
        });
    }

    public void initializeItem(){
        new Thread(r).start();
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://50.116.1.90/grocery/getItemCategories.php");
                System.out.println("run");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String result = "",buf = "";
                while ((buf=br.readLine())!=null){
                    result += buf;
                }
                System.out.println(result);
                String[] bufstr = result.split("\\*\\*\\*");
                for(int i=0;i<bufstr.length;i++){
                    items.add(bufstr[i]);
                }
                items2 = items;
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
            lv.setAdapter(new ArrayAdapter<String>(ItemFinder.this, android.R.layout.simple_expandable_list_item_1, items));
        }
    };

    public void listFilter(String text){
        items2 = new ArrayList<String>();
        for(int i=0;i<items.size();i++){
            if(items.get(i).contains(text)){
                items2.add(items.get(i));
            }
        }
    }

    //check query
    class searchThread implements Runnable{
        String txt;
        public searchThread(String text){
            this.txt = text;
        }

        public void run(){
            if(txt.equals(sv.getQuery().toString())){

            }
        }
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
//using the spreadable listVie
//Modify the searchView (Default spreaded and cannot be closed)
//set a listener, listen to the "enter" event
//Get a tree-like sql (id, name, pid, description, childNum)
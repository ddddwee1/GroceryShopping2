package com.potato.potato.groceryshopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuyHistory extends ActionBarActivity {

    functions func;
    LinearLayout ll;
    final private String historyDir = "sdcard/groceryShopping";
    ArrayList<Details> details;
    boolean onDetail = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_history);

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        //assign
        ll = (LinearLayout) findViewById(R.id.hisLL);
        details = new ArrayList<>();

        //initialize the histories
        readFiles();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void initViews(){
        for (int i=0;i<details.size();i++){
            ll.addView(getDetailLayout(details.get(i)));
        }
    }

    private RelativeLayout getDetailLayout(final Details details){
        RelativeLayout rl = new RelativeLayout(BuyHistory.this);
        TextView dateTv = new TextView(BuyHistory.this);
        dateTv.setText(details.getDate());
        TextView priceTv = new TextView(BuyHistory.this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,100);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        priceTv.setLayoutParams(lp);
        priceTv.setText("Total:" + details.getTotal());
        rl.addView(dateTv);
        rl.addView(priceTv);
        rl.setBackgroundColor(Color.argb(255, 255, 255, 255));
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0,30,0,0);
        rl.setLayoutParams(lp2);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked");
                getDetailedLayout(details);
            }
        });
        rl.setBackgroundResource(R.drawable.coursebg);
        return rl;
    }

    private void getDetailedLayout(Details details){
        ll.removeAllViews();
        onDetail = true;
        ArrayList<String> names = details.getNames();
        ArrayList<Integer> quantities = details.getQuantities();
        ArrayList<Double> prices = details.getPrices();
        for (int i=0;i<names.size();i++){
            ll.addView(getDetailedItem(names.get(i),prices.get(i),quantities.get(i)));
        }

    }

    private RelativeLayout getDetailedItem(String name, double price, int quantity){
        Context context = BuyHistory.this;
        RelativeLayout rl = new RelativeLayout(context);
        TextView tv = new TextView(context);
        tv.setText(name);
        tv.setTextSize(25);
        tv.setPadding(35,20,35,20);
        tv.setId(View.generateViewId());
        rl.addView(tv);

        TextView priceTv = new TextView(context);
        priceTv.setText("Price:" + price + "(*" + quantity + ")");
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.BELOW, tv.getId());
        priceTv.setLayoutParams(lp1);
        priceTv.setId(View.generateViewId());
        rl.addView(priceTv);

        TextView totalTv = new TextView(context);
        totalTv.setText("Total:" + price * quantity);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.BELOW,priceTv.getId());
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        totalTv.setLayoutParams(lp2);
        rl.addView(totalTv);

        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.setMargins(0,30,0,0);
        rl.setLayoutParams(lp3);

        return rl;
    }


    public void readFiles(){
        File f = new File(historyDir);
        File[] fList = f.listFiles();
        for (int i=0;i<fList.length;i++){
            fromXML(fList[i]);
        }
    }

    private void fromXML(File f){
        String xml = fromFile(f);
        String buff = getTag("item", xml);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        ArrayList<Double> prices = new ArrayList<>();

        while(!buff.equals("")){
            System.out.println(xml);
            names.add(getTag("n", buff));
            prices.add(Double.valueOf(getTag("p",buff)));
            quantities.add(Integer.valueOf(getTag("q", buff)));

            xml = xml.replace(buff, "");
            buff = getTag("item",xml);
        }
        double total = Double.valueOf(getTag("total",xml));
        Details dt = new Details(names,prices,quantities,total,f.getName());
        details.add(dt);
    }

    private String fromFile(File f){
        String result = "";
        try {
            FileInputStream in = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String buff2="";
            while((buff2=br.readLine())!=null){
                result += buff2;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Result: " + result);
        return result;
    }

    private static String getTag(String key,String input){
        String ptnstr = "<"+key+">(.*?)</"+key+">";
        Pattern ptn = Pattern.compile(ptnstr);
        Matcher mtc = ptn.matcher(input);
        boolean a = mtc.find();
        if(a){
            return mtc.group(1);
        }else{
            return "";
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && onDetail){
            ll.removeAllViews();
            onDetail = false;
            initViews();
        }else {
            finish();
        }
        return true;
    }

    private class Details{
        private ArrayList<String> names;
        private ArrayList<Integer> quantities;
        private ArrayList<Double> prices;
        private double total=0;
        private String date;

        public Details(ArrayList<String> names, ArrayList<Double> prices, ArrayList<Integer> quantities, double total, String date){
            this.names = names;
            this.prices = prices;
            this.quantities  = quantities;
            this.total = total;
            this.date = date;
        }

        public ArrayList<String> getNames() {
            return names;
        }

        public ArrayList<Integer> getQuantities() {
            return quantities;
        }

        public ArrayList<Double> getPrices() {
            return prices;
        }

        public double getTotal() {
            return total;
        }

        public String getDate() {
            return date;
        }
    }
}

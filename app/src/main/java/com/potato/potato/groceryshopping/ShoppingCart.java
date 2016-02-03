package com.potato.potato.groceryshopping;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingCart extends ActionBarActivity {

    functions func;
    ScrollView sc;
    Button buyBtn,clear;
    appGlobal app;
    TextView tv;
    LinearLayout ll,mainll;
    double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        app = (appGlobal) getApplication();

        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 110, 220)));

        //initialize function class
        WindowManager wm = getWindowManager();
        func = new functions(wm.getDefaultDisplay().getHeight(),wm.getDefaultDisplay().getWidth());

        //assign views
        sc = (ScrollView) findViewById(R.id.cartScroll);
        buyBtn  = (Button) findViewById(R.id.buyBtn);
        clear = (Button) findViewById(R.id.clean);
        tv = (TextView) findViewById(R.id.cartPrice);
        ll = (LinearLayout) findViewById(R.id.cartitems);
        mainll = (LinearLayout) findViewById(R.id.cartMainLL);

        //tv.setBackgroundColor(Color.argb(200,250,250,250));

        //set buy action
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.writeToHistory();
                clean();
            }
        });

        //
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clean();
            }
        });

        additem(1.5,"abc",1);
        ll.setBackgroundColor(Color.argb(255,200,200,200));
    }

    public void additem(double price, String name, int quantity){
        app.additem(name,quantity,price);
        ll.addView(new CartItem(price, name, quantity).getCartItem());
        UpdateTV();
    }

    public void clean(){
        app.newCart();
        app.price = 0;
        ll.removeAllViews();
        UpdateTV();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void UpdateTV(){
        tv.setText("Total price: "+String.valueOf(app.price));
    }

    public class CartItem{
        String name;
        double price;
        int quantity;
        int index;
        public CartItem(double price, String name,int quantity){
            this.price = price;
            this.name = name;
            this.quantity = quantity;
            this.index = app.getCartStr().size()-1;
        }

        public Button incOrDecBtn(final boolean incdec){
            Button btn = new Button(ShoppingCart.this);
            if(incdec) {
                btn.setText("+");
            }else{
                btn.setText("-");
            }
            btn.setBackgroundColor(Color.argb(0,255,255,255));
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundColor(Color.argb(150, 200, 200, 200));
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.setBackgroundColor(Color.argb(0, 255, 255, 255));
                    }
                    return false;
                }
            });
            btn.setId(View.generateViewId());
            return btn;
        }

        public TextView getTv(String str) {
            TextView tv = new TextView(ShoppingCart.this);
            tv.setText(str);
            tv.setId(View.generateViewId());
            return tv;
        }


        public RelativeLayout getCartItem(){
            RelativeLayout rl = new RelativeLayout(ShoppingCart.this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tv = getTv(name);
            tv.setBackgroundColor(Color.argb(200,255,255,255));
            tv.setTextSize(25);
            tv.setPadding(35,20,35,20);

            tv.setLayoutParams(lp);
            rl.addView(tv);

            TextView PriceText = getTv("Price:");
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,120);
            PriceText.setGravity(Gravity.CENTER_VERTICAL);
            lp2.addRule(RelativeLayout.BELOW,tv.getId());
            lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);;
            lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            PriceText.setLayoutParams(lp2);
            rl.addView(PriceText);

            final TextView Price = getTv(String.valueOf(price*quantity));
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,120);
            Price.setGravity(Gravity.CENTER_VERTICAL);
            lp3.addRule(RelativeLayout.BELOW,tv.getId());
            lp3.addRule(RelativeLayout.RIGHT_OF,PriceText.getId());
            lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            Price.setLayoutParams(lp3);
            rl.addView(Price);

            Button plus = incOrDecBtn(true);
            //TextView plus = getTv("+");
            final TextView quanti = getTv(String.valueOf(quantity));
            Button minus = incOrDecBtn(false);
            //TextView minus = getTv("-");

            //RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(120,120);
            lp4.addRule(RelativeLayout.BELOW, tv.getId());
            lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            plus.setLayoutParams(lp4);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    app.quantities.remove(index);
                    app.quantities.add(index,quantity);
                    app.price += price;
                    UpdateTV();
                    quanti.setText(String.valueOf(quantity));
                    Price.setText(String.valueOf(quantity * price));
                }
            });
            rl.addView(plus);

            //RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            //RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(20,20);
            RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(90,120);
            lp5.addRule(RelativeLayout.BELOW, tv.getId());
            lp5.addRule(RelativeLayout.LEFT_OF, plus.getId());
            quanti.setGravity(Gravity.CENTER);
            //lp5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            quanti.setLayoutParams(lp5);
            rl.addView(quanti);

            //RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(120,120);
            lp6.addRule(RelativeLayout.BELOW,tv.getId());
            lp6.addRule(RelativeLayout.LEFT_OF, quanti.getId());
            lp6.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity <= 0) {
                        quantity = 0;
                    }else{
                        quantity--;
                        app.quantities.remove(index);
                        app.quantities.add(index,quantity);

                        app.price -= price;
                    }
                    UpdateTV();
                    quanti.setText(String.valueOf(quantity));
                    Price.setText(String.valueOf(quantity * price));
                }
            });
            minus.setLayoutParams(lp6);
            rl.addView(minus);

            System.out.println(quanti.getId());

            //quantity line2 right(change onClickListener to here)
            rl.setBackgroundColor(Color.argb(200, 240, 240, 240));
            LinearLayout.LayoutParams lpn = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpn.setMargins(0,20,0,20);
            rl.setLayoutParams(lpn);
            return rl;
        }
    }
}
//add a blue basket on the left of every item
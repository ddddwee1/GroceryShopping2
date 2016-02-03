package com.potato.potato.groceryshopping;

import android.app.Application;

import org.apache.http.cookie.CookieAttributeHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class appGlobal extends Application {

    ArrayList<String> cartStr = new ArrayList<String>();
    ArrayList<Double> prices = new ArrayList<>();
    ArrayList<Integer> quantities = new ArrayList<>();
    final private String historyDir = "sdcard/groceryShopping";


    double price = 0;

    public void additem(String in, int quantity, double p){
        int i = isInList(in);
        if(i==-1) {
            cartStr.add(in);
            prices.add(p);
            quantities.add(quantity);
        }else{
            int buff = quantities.get(i)+quantity;
            quantities.remove(i);
            quantities.add(i,buff);
        }
        price += quantity * p;
    }
    private int isInList(String in){
        for (int i=0;i<cartStr.size();i++){
            if (cartStr.get(i).equals(in)){
                return  i;
            }
        }
        return -1;
    }

    public void newCart(){
        cartStr = new ArrayList<String>();
        prices = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addPrice(double add){
        price += add;
    }

    public ArrayList<String> getCartStr() {
        return cartStr;
    }

    public double getPrice() {
        return price;
    }

    public String toXML(){
        String result = "";
        //result += "<history>";
        int max = cartStr.size();

        for(int i=0;i<max;i++){
            result += "<item>";
                result += "<p>";
                    result += String.valueOf(prices.get(i));
                result += "</p>";
                result += "<q>";
                    result += String.valueOf(quantities.get(i));
                 result += "</q>";
                result += "<n>";
                    result += cartStr.get(i);
                result += "</n>";
            result += "</item>";
        }
        result += "<total>"+ String.valueOf(price)+"</total>";
        //result += "</history>";
        return result;
    }

    public void writeToHistory(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String name = sdf.format(dt);
        String filename = historyDir+"/"+name;
        File f = new File(filename);
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            out.write(toXML().getBytes());
            out.close();
        }catch (Exception e){

        }
    }


}

package com.potato.potato.groceryshopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class QRreader extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreader);
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        intent.setClass(QRreader.this,ScanResult.class);
        intent.putExtra("dt",data.getStringExtra("SCAN_RESULT"));
        startActivity(intent);
        finish();
    }
}
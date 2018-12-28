package com.example.a93584.exercise;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Phone extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call);
        Button bt = (Button) findViewById(R.id.bt_call);
        bt.setOnClickListener(new MyListener());
    }

    class MyListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            EditText et = (EditText) findViewById(R.id.et_phone);
            String phone = et.getText().toString();
            Intent diaintent = new Intent();
            diaintent.setAction(Intent.ACTION_DIAL);
            diaintent.setData(Uri.parse("tel:" + phone));
            startActivity(diaintent);
        }
    }

}
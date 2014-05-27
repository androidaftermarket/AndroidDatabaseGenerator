package com.android.TestApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView tv = (TextView) findViewById(R.id.text);
        Hello user = new Hello();
        user.setName("Gian Aquino");
        user.setAge(21);
        HelloDAO.getInstance(this).insert(user);
        tv.setText(HelloDAO.getInstance(this).query(null, null, null, null,null).toString());


    }
}

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
        User user = new User();
        user.setName("Gian Aquino");
        user.setAge(21);
        UserDAO.getInstance(this).insert(user);
        tv.setText(UserDAO.getInstance(this).query(null, null, null, null,null).toString());


    }
}

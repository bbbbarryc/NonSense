package com.bcappslab.nonsense;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Barry on 2014-08-04.
 */
public class MyTestClass extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_class);

        TextView textView = (TextView) findViewById(R.id.testTextView);

        textView.setText(getApplicationContext().getExternalFilesDir(null).toString());
    }
}

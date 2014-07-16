package com.bcappslab.nonsense.testing;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bcappslab.nonsense.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ExampleReadWrite extends Activity {

    CheckBox saveToDb;
    EditText toWrite;
    TextView readText;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_readwrite);

        saveToDb = (CheckBox) findViewById(R.id.checkBox);
        toWrite = (EditText) findViewById(R.id.editText);
        readText = (TextView) findViewById(R.id.textView);
    }



    public void save(View v) {
        if (saveToDb.isChecked()) {
            // Save this to the database

        } else {
            // Save it to a file
            // Create a new file with a new filename
            Log.d("ExampleReadWrite", "Attempting to save a file");


            try {
                File myFile = new File(this.getFilesDir(), "myFile.txt");
                BufferedWriter out = new BufferedWriter(new FileWriter(myFile, true));
                out.write(toWrite.getText().toString() + "\r\n");
                out.flush();
                out.close();
            } catch (IOException ioe) {
                Log.e("ExampleReadWrite", "IOException" + ioe.getMessage());

            } /*catch (FileNotFoundException fnfe) {
                Log.e("ExampleReadWrite", "IOException" + fnfe.getMessage());
            }*/

            toWrite.setText("");

            // Writes to external storage if available
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                try {
                    File myFile = new File(this.getExternalFilesDir(null),"myFile.txt");
                    BufferedWriter out = new BufferedWriter(new FileWriter(myFile, true));
                    out.write(toWrite.getText().toString() + "\r\n");
                    out.flush();
                    out.close();
                } catch (IOException ioe) {
                    Log.e("ExampleReadWrite", "IOException" + ioe.getMessage());
                }
            }
        }
    }

    public void read(View v) {
        // now read it back and update the view
        readText.setText("");

        // Writes to internal storage
        try {
            File myFile = new File(this.getFilesDir(), "myFile.txt");
            BufferedReader in = new BufferedReader(new FileReader(myFile));

            String read;
            while ((read = in.readLine()) != null) {
                readText.append(read + "\r\n");
            }
        } catch (IOException ioe) {
            Log.e("ExampleReadWrite", "IOException" + ioe.getMessage());

        }


    }

}


package com.example.lukasz.whozzup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Button b = (Button) findViewById(R.id.button_next2);

        final Spinner spinner = (Spinner) findViewById(R.id.category_spinner);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spinner.getSelectedItem().equals("")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", (String) spinner.getSelectedItem());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "You cannot leave the name blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

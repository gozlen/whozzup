package com.example.lukasz.whozzup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TitleActivity extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent goToMainActivity = new Intent(getApplicationContext(),MainActivity.class);
        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(goToMainActivity);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button b = (Button) findViewById(R.id.button_next1);
        final EditText e = (EditText) findViewById(R.id.form_title);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String txt = e.getText().toString();
                if (!txt.equals("")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", txt);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "You cannot leave the name blank", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

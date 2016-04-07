package com.example.victoria.pearlhacks_1_2_3_4cast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity_Outfits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__outfits);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void endActivity(View view) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(Uri.parse("mailto:"));

        //EditText editText = (EditText) findViewById(R.id.editable_text);
        //String emailToBeSent = editText.getText().toString();

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sujeethjinesh@gmail.com", "hello@pearlhacks.com"});
        //intent.putExtra(Intent.EXTRA_EMAIL, emailToBeSent);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }

}

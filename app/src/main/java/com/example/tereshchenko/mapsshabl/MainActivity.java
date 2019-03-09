package com.example.tereshchenko.mapsshabl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button2:
                Intent intent = new Intent(this,MapsActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}

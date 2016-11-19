package com.freeman.pinnedheaderlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button button1;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.PinnedListViewActivity);
        button2 = (Button) findViewById(R.id.PinnedRecyclerViewActivity);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PinnedListViewActivity:
                Intent i = new Intent(this, PinnedListViewActivity.class);
                startActivity(i);
                break;
            case R.id.PinnedRecyclerViewActivity:
                Intent intent = new Intent(this, PinnedRecyclerViewActivity.class);
                startActivity(intent);
                break;
        }
    }
}

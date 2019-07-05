package com.nibiru.lbspuskesmas.ListPuskesmas.MainActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nibiru.lbspuskesmas.ListPuskesmas.ListActivity.ListPuskesmasActivity;
import com.nibiru.lbspuskesmas.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    private void findView(){
        findViewById(R.id.cv_daftarpuskesmas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListPuskesmasActivity.class));
            }
        });
    }
}

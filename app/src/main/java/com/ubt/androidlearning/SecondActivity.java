package com.ubt.androidlearning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ubt.androidlearning.processor.CustomAnnotation;


@CustomAnnotation
public class SecondActivity extends AppCompatActivity {


    @CustomAnnotation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SecondActivity","--------onCreate-------");
        setContentView(R.layout.activity_second);
    }

    @CustomAnnotation
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("SecondActivity","--------onStart-------");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("SecondActivity","--------onRestart-------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SecondActivity","--------onResume-------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("SecondActivity","--------onPause-------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SecondActivity","--------onStop-------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("SecondActivity","--------onDestroy-------");
    }
}

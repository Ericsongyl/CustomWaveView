package com.nicksong.customwaveview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nicksong.customwaveview.widget.CustomWaveView;

public class MainActivity extends AppCompatActivity {

    private CustomWaveView mWave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mWave = (CustomWaveView)findViewById(R.id.custom_wave);
        mWave.startWaving();
    }
}

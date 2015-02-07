package com.mmyo.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.scanner.ScanActivity;

public class MMyoActivity extends Activity {

    private TextView mLockedText;
    private Button btnScan;

    private DeviceListener mListener = new AbstractDeviceListener() {

        @Override
        public void onConnect(Myo myo, long timestamp) {
            mLockedText.setTextColor(Color.GREEN);
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            mLockedText.setTextColor(Color.RED);
        }

        @Override
        public void onUnlock(Myo myo, long timestamp) {
            mLockedText.setText("Unlocked!");
        }

        @Override
        public void onLock(Myo myo, long timestamp) {
            mLockedText.setText("Locked!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLockedText = (TextView) findViewById(R.id.mLockedText);
        btnScan = (Button) findViewById(R.id.btnScan);

        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            Toast.makeText(this, "couldn't initialise hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnScan.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onScanSelected();
                    }
                }
        );

        hub.addListener(mListener);
    }

    private void onScanSelected() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Hub.getInstance().removeListener(mListener);
        if (isFinishing()) {
            Hub.getInstance().shutdown();
        }
    }


}

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
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.scanner.ScanActivity;

public class MMyoActivity extends Activity {

    private TextView txtLocked;
    private TextView txtPose;
    private TextView txtRoll;
    private TextView txtPitch;
    private TextView txtYaw;
    private Button btnScan;
    private SpellGesture spell;
    private Quaternion startQuat;
    private Quaternion endQuat;

    private DeviceListener mListener = new AbstractDeviceListener() {

        @Override
        public void onConnect(Myo myo, long timestamp) {
            txtLocked.setTextColor(Color.GREEN);
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            txtLocked.setTextColor(Color.RED);
        }

        @Override
        public void onUnlock(Myo myo, long timestamp) {
            txtLocked.setText("Unlocked!");
        }

        @Override
        public void onLock(Myo myo, long timestamp) {
            txtLocked.setText("Locked!");
            txtPose.setTextColor(Color.GRAY);
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            if (spell == SpellGesture.TRIGGERED) {
                startQuat = rotation;
                spell = SpellGesture.TRACKING;
            } else if (spell == SpellGesture.FINISHED) {
                endQuat = rotation;
                spell = SpellGesture.DEAD;
            }

            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            txtRoll.setText(Float.toString(roll));
            txtPitch.setText(Float.toString(pitch));
            txtYaw.setText(Float.toString(yaw));
        }

        private void calculateSpell(Pose pose, Quaternion startQuat, Quaternion endQuat) {
            float totalRoll = (float) (Math.toDegrees(Quaternion.roll(endQuat))
                    - Math.toDegrees(Quaternion.roll(startQuat)));
            if (Math.abs(totalRoll) > 180) {
                totalRoll = (totalRoll > 180 ? totalRoll - 180 : totalRoll + 180);
            }
            float totalPitch = (float) (Math.toDegrees(Quaternion.pitch(endQuat))
                    - Math.toDegrees(Quaternion.pitch(startQuat)));
            if (Math.abs(totalPitch) > 180) {
                totalPitch = (totalPitch > 180 ? totalPitch - 180 : totalPitch + 180);
            }
            float totalYaw = (float) (Math.toDegrees(Quaternion.yaw(endQuat))
                    - Math.toDegrees(Quaternion.yaw(startQuat)));
            if (Math.abs(totalYaw) > 180) {
                totalYaw = (totalYaw > 180 ? totalYaw - 180 : totalYaw + 180);
            }
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            txtPose.setTextColor(Color.BLACK);
            txtPose.setText(pose.name());

            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                myo.unlock(Myo.UnlockType.HOLD);
                myo.notifyUserAction();
                spell = SpellGesture.TRIGGERED;
            } else {
                myo.unlock(Myo.UnlockType.TIMED);
                if (spell == SpellGesture.TRACKING)
                    spell = SpellGesture.FINISHED;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLocked = (TextView) findViewById(R.id.txtLocked);
        txtPose = (TextView) findViewById(R.id.txtPose);
        txtRoll = (TextView) findViewById(R.id.txtRoll);
        txtPitch = (TextView) findViewById(R.id.txtPitch);
        txtYaw = (TextView) findViewById(R.id.txtYaw);
        btnScan = (Button) findViewById(R.id.btnScan);
        spell = SpellGesture.WAITING;

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

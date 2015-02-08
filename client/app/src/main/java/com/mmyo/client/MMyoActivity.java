package com.mmyo.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mmyo.client.rest.MMyoService;
import com.mmyo.client.rest.MMyoServiceConstructor;
import com.mmyo.client.rest.response.Response;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

public class MMyoActivity extends Activity {

    private TextView txtLocked;
    private TextView txtPose;
    private TextView txtRoll;
    private TextView txtPitch;
    private TextView txtYaw;
    private TextView txtDirection;
    private boolean poseSet;
    private Pose spellPose;
    private Button btnScan;
    private SpellGesture spell;
    private Quaternion startQuat;
    private Quaternion endQuat;
    private MMyoService service;

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
            poseSet = false;
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            if (spell == SpellGesture.TRIGGERED) {
                startQuat = rotation;
                spell = SpellGesture.TRACKING;
            } else if (spell == SpellGesture.FINISHED) {
                endQuat = rotation;
                spell = SpellGesture.DEAD;
                calculateSpell(myo, spellPose, startQuat, endQuat);
            }

            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));

            txtRoll.setText(Float.toString(roll));
            txtPitch.setText(Float.toString(pitch));
            txtYaw.setText(Float.toString(yaw));
        }

        private void calculateSpell(Myo myo, Pose pose, Quaternion startQuat, Quaternion endQuat) {
            Direction d = Direction.NOTHING;
            boolean invert = myo.getXDirection() == XDirection.TOWARD_ELBOW;
            float totalRoll = (invert ? -1 : 1) * (float) (Math.toDegrees(Quaternion.roll(endQuat))
                    - Math.toDegrees(Quaternion.roll(startQuat)));
            if (Math.abs(totalRoll) > 180) {
                totalRoll = (totalRoll > 180 ? totalRoll - 360 : totalRoll + 360);
            }
            float totalPitch = (invert ? -1 : 1) * (float) (Math.toDegrees(Quaternion.pitch(endQuat))
                    - Math.toDegrees(Quaternion.pitch(startQuat)));
            if (Math.abs(totalPitch) > 180) {
                totalPitch = (totalPitch > 180 ? totalPitch - 360 : totalPitch + 360);
            }
            float totalYaw = (float) (Math.toDegrees(Quaternion.yaw(endQuat))
                    - Math.toDegrees(Quaternion.yaw(startQuat)));
            if (Math.abs(totalYaw) > 180) {
                totalYaw = (totalYaw > 180 ? totalYaw - 360 : totalYaw + 360);
            }

            if (Math.abs(totalPitch) > 20) {
                d = totalPitch > 0 ? Direction.DOWN : Direction.UP;
            } else if (Math.abs(totalYaw) > 30) {
                d = totalYaw > 0 ? Direction.LEFT : Direction.RIGHT;
            } else if (Math.abs(totalRoll) > 10) {
                d = totalRoll > 0 ? Direction.CLOCKWISE : Direction.ANTICLOCKWISE;
            }

            txtDirection.setText(d.toString());
            if (d != Direction.NOTHING) {
                service.castSpell(0, pose, d);
            }
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            txtPose.setTextColor(Color.BLACK);
            txtPose.setText(pose.name());

            if ((!poseSet || pose == spellPose) && pose != Pose.UNKNOWN && pose != Pose.REST) {
                poseSet = true;
                spellPose = pose;
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

        service = MMyoServiceConstructor.construct();

        txtLocked = (TextView) findViewById(R.id.txtLocked);
        txtPose = (TextView) findViewById(R.id.txtPose);
        txtRoll = (TextView) findViewById(R.id.txtRoll);
        txtPitch = (TextView) findViewById(R.id.txtPitch);
        txtYaw = (TextView) findViewById(R.id.txtYaw);
        txtDirection = (TextView) findViewById(R.id.txtDirection);
        btnScan = (Button) findViewById(R.id.btnScan);
        spell = SpellGesture.WAITING;
        poseSet = false;

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

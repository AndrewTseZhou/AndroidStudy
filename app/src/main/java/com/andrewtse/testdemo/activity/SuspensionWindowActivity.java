package com.andrewtse.testdemo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.andrewtse.testdemo.R;
import com.andrewtse.testdemo.utils.PermissionUtil;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class SuspensionWindowActivity extends AppCompatActivity {

    private AppCompatButton mFlatWindowButton;
    private AppCompatButton mAccessibilityButton;
    private static final int FLAT_REQUEST_CODE = 213;
    private static final int ACCESSIBILITY_REQUEST_CODE = 438;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_suspension_window);
        mFlatWindowButton = findViewById(R.id.btn_flatwindow);
        mAccessibilityButton = findViewById(R.id.btn_accessibility);
        flatWindowVisible();
    }

    private void flatWindowVisible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // > M,grant permission
            if (PermissionUtil.isCanDrawOverlays(this)) {
                // permission authorized,service go,button gone
                mFlatWindowButton.setVisibility(View.GONE);
                accessibilityVisible();
            } else {
                // permission unauthorized,button visible
                mFlatWindowButton.setVisibility(View.VISIBLE);
                Toast.makeText(this, getString(R.string.permission_flatwindow_), Toast.LENGTH_SHORT).show();
            }
        } else {
            // < M,service go,gone
            mFlatWindowButton.setVisibility(View.GONE);
            accessibilityVisible();
        }
    }

    private void accessibilityVisible() {
        if (PermissionUtil.isAccessibilityServiceEnable(this)) {
            Toast.makeText(this, getString(R.string.permission_notice), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mAccessibilityButton.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.permission_accessibility_), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goGetFlatWindow(View view) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        startActivityForResult(intent, FLAT_REQUEST_CODE);
    }

    public void goGetAccessibility(View view) {
        Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(accessibleIntent, ACCESSIBILITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAT_REQUEST_CODE) {
            flatWindowVisible();
        } else if (requestCode == ACCESSIBILITY_REQUEST_CODE) {
            accessibilityVisible();
        }
    }
}

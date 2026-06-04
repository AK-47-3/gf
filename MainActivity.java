package com.orion.floatinggirlfriend;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "মেসেঞ্জারের মতো চ্যাট হেডের জন্য পারমিশন অন করো ওরিয়ন!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 123);
        } else {
            startFloatingGirlfriend();
        }
    }

    private void startFloatingGirlfriend() {
        startService(new Intent(this, FloatingService.class));
        finish(); 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (Settings.canDrawOverlays(this)) {
                startFloatingGirlfriend();
            } else {
                Toast.makeText(this, "পারমিশন ছাড়া প্রেমিকা স্ক্রিনের বাইরে আসবে না! 😡", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

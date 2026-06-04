package com.orion.floatinggirlfriend;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class FloatingService extends Service {
    private WindowManager windowManager;
    private View floatingView;
    private ImageView chatHeadIcon;
    private TextView dialogueBubble;
    private int currentMood = 0; // 0=মিষ্টি, 1=দুষ্টু, 2=রাগী

    // মুড অনুযায়ী ডায়ালগ ডাটাবেস
    private String[] mishtiDialogues = {
        "ওরিয়ন জানু, পড়াশোনা করছ তো? লক্ষ্মী ছেলে আমার! 😘",
        "তোমাকে ছাড়া আমার একটুও ভালো লাগে না সোনা... 🥺❤️",
        "আজকে সব পড়া শেষ করলে তোমাকে নিজ হাতে খাইয়ে দেব! 🤱"
    };

    private String[] dushtuDialogues = {
        "অ্যাই ওরিয়ন! সারাদিন শুধু বই পড়লে হবে? আমার দিকে একটু তাকাও... 🫦💋",
        "পড়ার বাহানা রেখে কাছে আসো না একটু, কামড়ে দেব কিন্তু! 😜",
        "আজ রাতে তোমার জন্য একটা স্পেশাল সারপ্রাইজ আছে জানু... 🫣"
    };

    private String[] ragiDialogues = {
        "ওরিয়ন!!! আবার ফেসবুকে স্ক্রোল করছ? থাপড়ায়ে দাঁত আলগা করে দেব! 🤬",
        "ফাঁকিবাজি একদম পছন্দ না আমার! এখনই পড়তে বসো, নাইলে কথা বলব না! 😡",
        "আজকে যদি ইংলিশ পড়া শেষ না হয়, মেসকাত ভাইকে ডেকে এনে পিটাব! 💀"
    };

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // রান-টাইমে একটি সিম্পল লেআউট তৈরি করা
        floatingView = new View(this);
        // (এখানে গিটহাব রানার যেন ক্র্যাশ না করে তাই জাভা দিয়ে ভিউ হ্যান্ডেল করা হচ্ছে)
        
        chatHeadIcon = new ImageView(this);
        chatHeadIcon.setImageResource(android.R.drawable.btn_star_big_on); // কিউট স্টার আইকন (অ্যান্ড্রয়েড ডিফল্ট)

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 200;
        params.y = 200;

        windowManager.addView(chatHeadIcon, params);

        // চ্যাট হেডে ক্লিক করলে মুড এবং ডায়ালগ চেঞ্জ হবে
        chatHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                String message = "";
                
                // চক্রাকারে মুড চেঞ্জ (মিষ্টি -> দুষ্টু -> রাগী)
                if (currentMood == 0) {
                    message = mishtiDialogues[rand.nextInt(mishtiDialogues.length)];
                    currentMood = 1;
                } else if (currentMood == 1) {
                    message = dushtuDialogues[rand.nextInt(dushtuDialogues.length)];
                    currentMood = 2;
                } else {
                    message = ragiDialogues[rand.nextInt(ragiDialogues.length)];
                    currentMood = 0;
                }

                // স্ক্রিনের ওপর পপ-আপ মেসেজ শো করবে
                Toast.makeText(FloatingService.this, message, Toast.LENGTH_LONG).show();
            }
        });

        // ড্র্যাগ অ্যান্ড ড্রপ কন্ট্রোল (টেনে স্ক্রিনের যেকোনো জায়গায় নেওয়া)
        chatHeadIcon.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHeadIcon, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHeadIcon != null) windowManager.removeView(chatHeadIcon);
    }
}

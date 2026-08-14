package com.example.idleslayerautotap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SharedPreferences prefs;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 50, 36, 36);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(Color.rgb(18, 18, 22));

        TextView title = new TextView(this);
        title.setText("Idle Slayer Auto Tap");
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView info = new TextView(this);
        info.setText("\n用途：偵測你設定的橘色劍圖示，接近角色區域時執行一次點擊。\n\n這是本機自動化原型，不會連線控制其他玩家。");
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(16);
        root.addView(info, new LinearLayout.LayoutParams(-1, -2));

        Button accessibility = new Button(this);
        accessibility.setText("① 開啟 Android 輔助功能");
        accessibility.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        root.addView(accessibility, new LinearLayout.LayoutParams(-1, -2));

        Button start = new Button(this);
        start.setText("② 啟用自動偵測");
        start.setOnClickListener(v -> {
            prefs.edit().putBoolean("enabled", true).apply();
            updateStatus();
        });
        root.addView(start, new LinearLayout.LayoutParams(-1, -2));

        Button stop = new Button(this);
        stop.setText("③ 停止");
        stop.setOnClickListener(v -> {
            prefs.edit().putBoolean("enabled", false).apply();
            updateStatus();
        });
        root.addView(stop, new LinearLayout.LayoutParams(-1, -2));

        Button settings = new Button(this);
        settings.setText("偵測設定");
        settings.setOnClickListener(v -> startActivity(
                new Intent(this, SettingsActivity.class)));
        root.addView(settings, new LinearLayout.LayoutParams(-1, -2));

        status = new TextView(this);
        status.setTextColor(Color.CYAN);
        status.setTextSize(17);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, 35, 0, 0);
        root.addView(status, new LinearLayout.LayoutParams(-1, -2));

        setContentView(root);
        updateStatus();
    }

    private void updateStatus() {
        boolean enabled = prefs.getBoolean("enabled", false);
        status.setText(enabled ? "狀態：已啟用\n請切回遊戲" : "狀態：已停止");
    }
}
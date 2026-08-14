package com.example.idleslayerautotap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    private EditText x1, x2, y1, y2, cooldown;
    private android.content.SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(30, 40, 30, 30);
        root.setBackgroundColor(Color.rgb(18,18,22));

        TextView title = new TextView(this);
        title.setText("偵測區域設定（百分比）");
        title.setTextColor(Color.WHITE);
        title.setTextSize(23);
        root.addView(title);

        x1 = field(root, "X 起點", "5");
        x2 = field(root, "X 終點", "35");
        y1 = field(root, "Y 起點", "15");
        y2 = field(root, "Y 終點", "70");
        cooldown = field(root, "兩次點擊最短間隔(ms)", "450");

        Button save = new Button(this);
        save.setText("儲存");
        save.setOnClickListener(v -> {
            put("x1", x1, 5);
            put("x2", x2, 35);
            put("y1", y1, 15);
            put("y2", y2, 70);
            put("cooldown", cooldown, 450);
            finish();
        });
        root.addView(save);

        setContentView(root);
        load();
    }

    private EditText field(LinearLayout root, String label, String def) {
        TextView t = new TextView(this);
        t.setText(label);
        t.setTextColor(Color.LTGRAY);
        root.addView(t);
        EditText e = new EditText(this);
        e.setText(def);
        e.setTextColor(Color.WHITE);
        e.setInputType(2);
        root.addView(e);
        return e;
    }

    private void load() {
        x1.setText(String.valueOf(prefs.getInt("x1",5)));
        x2.setText(String.valueOf(prefs.getInt("x2",35)));
        y1.setText(String.valueOf(prefs.getInt("y1",15)));
        y2.setText(String.valueOf(prefs.getInt("y2",70)));
        cooldown.setText(String.valueOf(prefs.getInt("cooldown",450)));
    }

    private void put(String key, EditText e, int def) {
        try { prefs.edit().putInt(key, Integer.parseInt(e.getText().toString())).apply(); }
        catch (Exception ignored) { prefs.edit().putInt(key, def).apply(); }
    }
}
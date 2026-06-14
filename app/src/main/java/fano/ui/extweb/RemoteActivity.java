package fano.ui.extweb;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class RemoteActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    Button refresh, back, pgup, pgdown, pgleft, pgright, zmu, zmd;
    ImageView keyHandler;
    EditText hiddenInput;
    LinearLayout trackpad;

    private float lastX, lastY;
    private long lastDownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_layout);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.about_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.remoteToolbar);
        toolbar.setTitle("Remote Control");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        refresh = findViewById(R.id.refresh);
        back = findViewById(R.id.back);
        pgup = findViewById(R.id.pgup);
        pgdown = findViewById(R.id.pgdown);
        pgleft = findViewById(R.id.pgleft);
        pgright = findViewById(R.id.pgright);
        zmu = findViewById(R.id.zmu);
        zmd = findViewById(R.id.zmd);
        keyHandler = findViewById(R.id.key_handler);
        hiddenInput = findViewById(R.id.hidden_input);
        trackpad = findViewById(R.id.trackpad);

        refresh.setOnClickListener(v -> {
            if (MainActivity.pl != null) {
                MainActivity.pl.refreshWeb();
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Presentation not active", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(v -> {
            if (MainActivity.pl != null) {
                MainActivity.pl.goBck();
            } else {
                Toast.makeText(this, "Presentation not active", Toast.LENGTH_SHORT).show();
            }
        });

        pgup.setOnClickListener(v -> {
            if (MainActivity.pl != null) MainActivity.pl.pageUp();
        });
        pgdown.setOnClickListener(v -> {
            if (MainActivity.pl != null) MainActivity.pl.pageDown();
        });
        pgleft.setOnClickListener(v -> {
            if (MainActivity.pl != null) MainActivity.pl.pageLeft();
        });
        pgright.setOnClickListener(v -> {
            if (MainActivity.pl != null) MainActivity.pl.pageRight();
        });
        zmu.setOnClickListener(v -> {
            if (MainActivity.pl != null) MainActivity.pl.zoomIn();
        });
        zmd.setOnClickListener(v -> {
            if (MainActivity.pl != null) MainActivity.pl.zoomOut();
        });

        keyHandler.setOnClickListener(v -> {
            hiddenInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(hiddenInput, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        hiddenInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > before) {
                    String newText = s.subSequence(before, count).toString();
                    if (MainActivity.pl != null) {
                        MainActivity.pl.injectText(newText);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    s.clear();
                }
            }
        });

        hiddenInput.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (MainActivity.pl != null) MainActivity.pl.injectKey(KeyEvent.KEYCODE_DEL);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (MainActivity.pl != null) MainActivity.pl.injectKey(KeyEvent.KEYCODE_ENTER);
                    return true;
                }
            }
            return false;
        });

        setupTrackpad();
    }

    private void setupTrackpad() {
        trackpad.setOnTouchListener((v, event) -> {
            if (MainActivity.pl == null) return false;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    lastDownTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = event.getRawX() - lastX;
                    float dy = event.getRawY() - lastY;
                    
                    // Higher sensitivity for "hardware" feel
                    MainActivity.pl.moveCursor(dx * 1.8f, dy * 1.8f);
                    
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;

                case MotionEvent.ACTION_UP:
                    v.performClick();
                    // Detect a tap (short duration, little movement)
                    if (System.currentTimeMillis() - lastDownTime < 200) {
                        MainActivity.pl.clickAtCursor();
                    }
                    break;
            }
            return true;
        });
    }
}

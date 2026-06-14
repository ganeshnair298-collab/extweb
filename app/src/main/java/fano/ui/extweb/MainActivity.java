package fano.ui.extweb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingtoolbar.FloatingToolbarLayout;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MainActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextView dispInfo,dispsubtext;
    ConstraintLayout bginfo;
    @SuppressLint("StaticFieldLeak")
    public static PresentationLayout pl;
    Button remote,start,stop;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar=findViewById(R.id.materialToolbar);
        dispInfo=findViewById(R.id.displnfo);
        dispsubtext=findViewById(R.id.dispsubtext);
        bginfo=findViewById(R.id.bginfo);
        start=findViewById(R.id.button2);
        stop=findViewById(R.id.button3);
        setSupportActionBar(toolbar);

        remote = findViewById(R.id.remote);
        remote.setOnClickListener(v -> {
            if(pl != null){
                Intent i = new Intent(MainActivity.this, RemoteActivity.class);
                startActivity(i);
            }
            else{
                showExpressiveDialog("No External Display Found","Please connect an external display or projector to use remote");
            }
        });
        DisplayManager dm = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = dm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if (displays.length > 0) {
            bginfo.setBackground(androidx.appcompat.content.res.AppCompatResources.getDrawable(this, R.drawable.frame_disp_info_success));
            dispInfo.setText("Display Connected");
            dispsubtext.setText("You are good to go");
        }
        start.setOnClickListener(v -> {
            Display[] currentDisplays = dm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (currentDisplays.length > 0) {
                if (pl == null) {
                    pl = new PresentationLayout(this, currentDisplays[0]);
                    pl.show();
                }
            } else {
                showExpressiveDialog("No External Display Found", "Please connect an external display or projector to start the presentation mode.");
            }
        });
        stop.setOnClickListener(v -> {
            Display[] currentDisplays = dm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (currentDisplays.length > 0) {
                if (pl != null) {
                    pl.dismiss();
                    pl = null;
                }
            } else {
                showExpressiveDialog("Disconnected", "The external display is already disconnected or inactive.");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        int itemID= menuItem.getItemId();
        if(itemID == R.id.about){
            Intent in = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(in);
        }
        return true;

    }
    public void showExpressiveDialog(String title, String text) {
        new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog) // Uses global theme from themes.xml
                .setIcon(R.drawable.baseline_info_24)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("Got it", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }
}
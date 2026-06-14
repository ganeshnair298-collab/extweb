package fano.ui.extweb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextView textView5,textView7,github,instagram;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_section);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.about_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.aboutToolbar);
        textView5 = findViewById(R.id.textView5);
        textView7=findViewById(R.id.textView7);
        instagram=findViewById(R.id.instagram);
        github=findViewById(R.id.github);
        instagram.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.instagram.com/gnnr_28"));
            startActivity(i);
        });
        github.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://github.com/ganeshnair298-collab/"));
            startActivity(i);
        });
        toolbar.setTitle("About");
        setSupportActionBar(toolbar);
        textView5.setText("Package ID: "+getString(R.string.package_id));
        textView7.setText("Version ID: "+getString(R.string.ver_ID));
    }
}

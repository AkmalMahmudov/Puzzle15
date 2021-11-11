package uz.gita.puzzle15numbers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.play_button).setOnClickListener(v -> play());
        findViewById(R.id.about_button).setOnClickListener(v -> about());
        findViewById(R.id.exit_button).setOnClickListener(v -> exit());
    }
    private void play() {
        startActivity(new Intent(this, PlayActivity.class));
    }

    private void about() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void exit() {
        finishAffinity();
    }
}
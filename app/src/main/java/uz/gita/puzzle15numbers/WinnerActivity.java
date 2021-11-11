package uz.gita.puzzle15numbers;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class WinnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        TextView winnerTime = findViewById(R.id.time);
        TextView winnerSteps = findViewById(R.id.steps);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            winnerTime.setText(bundle.getString("time"));
            winnerSteps.setText(bundle.getString("step"));
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    public void clickExit(View view) {
        finishAffinity();
    }

    public void clickHome(View view) {
        finish();
    }
}
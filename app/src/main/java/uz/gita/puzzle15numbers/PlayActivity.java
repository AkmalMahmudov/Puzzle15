package uz.gita.puzzle15numbers;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class PlayActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button[][] buttons;
    private Coordinate empty;
    private ArrayList<String> numbers;
    private TextView steps;
    private int step = 0;
    private ViewGroup group;
    private Chronometer chronometer;
    private SharedPreferences preferences;
    private ImageView music;
    private long pauseTime;
    private int musicClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mediaPlayer = MediaPlayer.create(peekAvailableContext(), R.raw.yummy);
        musicClicked = 0;
        initializeButtons();
        initializeNums();
        loadNums();
        mediaPlayer = MediaPlayer.create(this, R.raw.yummy);
        mediaPlayer.setLooping(true);
        preferences = getPreferences(Context.MODE_PRIVATE);
        preferences.getString("steps", String.valueOf(step));
        preferences.getLong("time", 0);
        music = findViewById(R.id.musix);
    }

    @Override
    protected void onStart() {
        super.onStart();
        step = Integer.parseInt(preferences.getString("steps", String.valueOf(0)));
        chronometer.setBase(preferences.getLong("time", 0) + SystemClock.elapsedRealtime());
        if (pauseTime != 0) {
            chronometer.setBase(SystemClock.elapsedRealtime() + pauseTime);
        }
        chronometer.start();
        if (preferences.getInt("music", musicClicked) == 0) {
            mediaPlayer.start();
        } else music.setImageResource(R.drawable.music_off);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onStop() {
        super.onStop();
        pauseTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        musicClicked = 1;
        mediaPlayer.pause();
        preferences.edit().putString("steps", String.valueOf(step)).apply();
        preferences.edit().putLong("time", pauseTime).apply();
        if (musicClicked == 1) {
            preferences.edit().putInt("music", 1).apply();
        } else {
            preferences.edit().putInt("music", 0).apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void setDefaults() {
        empty.setX(3);
        empty.setY(3);
        buttons[3][3].setBackgroundResource(R.color.empty_button);
        buttons[3][3].setText("");
        steps.setText("0");
        step = 0;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void initializeButtons() {
        group = findViewById(R.id.layout);
        steps = findViewById(R.id.stepper);
        chronometer = findViewById(R.id.chronometer);
        int count = group.getChildCount();
        buttons = new Button[4][4];
        empty = new Coordinate(3, 3);
        for (int i = 0; i < count; i++) {
            Button button = (Button) group.getChildAt(i);
            int x = i % 4;
            int y = i / 4;
            buttons[x][y] = button;
            Coordinate coordinate = new Coordinate(x, y);
            button.setTag(coordinate);
            button.setOnClickListener(v -> clickButton(button));
        }
    }

    private void initializeNums() {
        numbers = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            numbers.add(i + "");
        }
    }

    public void loadNums() {
        Collections.shuffle(numbers);
        for (int i = 0; i < 15; i++) {
            int x = i / 4;
            int y = i % 4;
            Button button = buttons[x][y];
            button.setText(numbers.get(i));
            button.setBackgroundResource(R.drawable.bg);
        }
        setDefaults();
    }

    private void clickButton(Button button) {
        Coordinate c = (Coordinate) button.getTag();
        int dx = abs(empty.getX() - c.getX());
        int dy = abs(empty.getY() - c.getY());
        if (dx + dy == 1) {
            step++;
            Button emptyButton = buttons[empty.getX()][empty.getY()];
            emptyButton.setText(button.getText());
            emptyButton.setBackgroundResource(R.drawable.bg);
            button.setBackgroundResource(R.color.empty_button);
            button.setText("");
            empty.setX(c.getX());
            empty.setY(c.getY());
            steps.setText(String.valueOf(step));
        }
        win();
    }

    public void clickRestart(View view) {
        loadNums();
    }

    public void win() {
        if (check()) {
            winnerApp();
            chronometer.stop();
        }
    }

    public boolean check() {
        boolean t = true;
        for (int i = 0; i < 15; i++) {
            String c = (i + 1) + "";
            Button button = (Button) group.getChildAt(i);
            if (!(button.getText().equals(c))) {
                t = false;
                break;
            }
        }
        return t;
    }

    private void winnerApp() {
        Bundle timeBundle = new Bundle();
        Bundle stepBundle = new Bundle();
        timeBundle.putString("time", ((int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000)) + "s");
        stepBundle.putString("step", String.valueOf(step));
        Intent intent = new Intent(this, WinnerActivity.class);
        intent.putExtras(timeBundle);
        intent.putExtras(stepBundle);
        setDefaults();
        startActivity(intent);
        finish();
    }

    public void clickHome(View view) {
        finish();
    }

    public void clickMusic(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            music.setImageResource(R.drawable.music_off);
            musicClicked = 1;
        } else {
            mediaPlayer.start();
            music.setImageResource(R.drawable.music);
            musicClicked = 0;
        }
    }
}
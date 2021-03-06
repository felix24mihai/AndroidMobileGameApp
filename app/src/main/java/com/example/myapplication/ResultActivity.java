package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewResultInfo, textViewMyScore, textViewHighestScore;
    private Button playAgainButton, goToMainMenu;
    private int score, targetScore, level;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        targetScore = getIntent().getIntExtra("targetScore", 100);
        level = getIntent().getIntExtra("level", 1);

        textViewResultInfo = findViewById(R.id.textViewResultInfo);
        textViewHighestScore = findViewById(R.id.textViewHighestScore);
        textViewMyScore = findViewById(R.id.textViewMyScore);
        playAgainButton = findViewById(R.id.buttonPlayAgain);
        goToMainMenu = findViewById(R.id.buttonMainMenu);

        score = getIntent().getIntExtra("score", 0);
        sharedPreferences = this.getSharedPreferences("game", Context.MODE_PRIVATE);
        int prevScore = sharedPreferences.getInt("coins", 0);
        sharedPreferences.edit().putInt("coins", score + prevScore).apply();
        sharedPreferences.edit().putBoolean("userCanPlay", true).apply();
        textViewMyScore.setText("Your Score: " + score);

        int highestScore = sharedPreferences.getInt("highestScore", 0);
        highestScore = Math.max(highestScore, score);

        if (score >= targetScore) {
            textViewResultInfo.setText("You Won The Game!");
            if (level < 10) {
                sharedPreferences.edit().putInt("level", level + 1).apply();
            }
        } else {
            textViewResultInfo.setText("Sorry, You Lost..");
        }
        textViewHighestScore.setText("Highest Score: " + highestScore);
        sharedPreferences.edit().putInt("highestScore", highestScore).apply();

        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.putExtra("targetScore", targetScore);
            intent.putExtra("level", level  );
            startActivity(intent);
            finish();
        });

        goToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        builder.setTitle("");
        builder.setMessage("Are You Sure You Want To Quit The Game?");
        builder.setCancelable(false);
        builder.setNegativeButton("Quit Game", (dialog, which) -> {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });
        builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }
}
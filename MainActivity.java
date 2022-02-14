package com.example.mp7scorecountersettings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    protected static final String KEY_EXTRA_WINNER = "com.example.mp3scorecounterimplicit.extra.WINNER";
    protected static final String KEY_EXTRA_FINAL_SCORE = "com.example.mp3scorecounterimplicit.extra.FINAL_SCORE";
    protected static final String KEY_WINNER_BG = "com.example.mp3scorecounterimplicit.extra.WINNER_BG";
    protected static final String KEY_CONTACT = "com.example.mp3scorecounterimplicit.extra.CONTACT";

    private static final String BLUE_SCORE = "BLUE_Score";
    private static final String RED_SCORE = "RED_Score";



    private TextView favoriteTeam;
    private TextView blueTeam;
    private TextView redTeam;
    private RelativeLayout relativeLayout;

    private int blueScore = 0;
    private int redScore = 0;
    private String mainBG = "basketball";
    private String winnerBG = "medal";
    private String favTeam = "Red Team";
    private String contact = "tel:6464173041";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "inside onCreate method of MainActivity");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        favoriteTeam = findViewById(R.id.fav_team);
        blueTeam = findViewById(R.id.team_blue);
        redTeam = findViewById(R.id.team_red);

        getUserPreferences();
        setMainBG(mainBG);
        favoriteTeam.setText(favTeam);

        Log.d(TAG, "end of onCreate method of MainActivity");
    }

    private void getUserPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mainBG = sharedPreferences.getString("main_background", "basketball");
        favTeam = sharedPreferences.getString("favorite_team", "Red Team");
        winnerBG = sharedPreferences.getString("winner_background", "medal");
        contact = sharedPreferences.getString("contact", "6464173041");
    }

    private void setMainBG(String mainBG) {
        relativeLayout = findViewById(R.id.main_bg);
        switch (mainBG){
            case "basketball":
                relativeLayout.setBackgroundResource(R.drawable.slamdunk);
                break;
            case "monsters":
                relativeLayout.setBackgroundResource(R.drawable.godzilla_kong);
                break;
            case "comics":
                relativeLayout.setBackgroundResource(R.drawable.bvs);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "inside onRestart");
        getUserPreferences();
        setMainBG(mainBG);
        favoriteTeam.setText(favTeam);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "inside of onSaveInstanceState");

        outState.putInt(BLUE_SCORE, blueScore);
        outState.putInt(RED_SCORE, redScore);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "inside of onRestoreInstanceState");

        blueScore = savedInstanceState.getInt(BLUE_SCORE,0);
        redScore = savedInstanceState.getInt(RED_SCORE,0);

        if(blueTeam != null || redTeam != null) {
            blueTeam.setText("" + blueScore);
            redTeam.setText("" + redScore);
        }
    }

    public void blueIncrement(View view) {
        Log.d(TAG, "inside blueIncrement");

        blueScore++;
        blueTeam.setText("" + blueScore);

        if(blueScore == 5) {
            Intent intent = new Intent(this,WinnerActivity.class);
            int finalScore =  blueScore - redScore;
            clearOutScores();

            intent.putExtra(KEY_EXTRA_WINNER, "Blue");
            intent.putExtra(KEY_EXTRA_FINAL_SCORE, finalScore);
            intent.putExtra(KEY_WINNER_BG, winnerBG);
            intent.putExtra(KEY_CONTACT, contact);

            startActivity(intent);
        }
    }

    public void redIncrement(View view) {
        Log.d(TAG, "inside redIncrement");

        redScore++;
        redTeam.setText("" + redScore);

        if(redScore == 5) {
            Intent intent = new Intent(this,WinnerActivity.class);
            int finalScore = redScore - blueScore;
            clearOutScores();

            intent.putExtra(KEY_EXTRA_WINNER, "Red");
            intent.putExtra(KEY_EXTRA_FINAL_SCORE, finalScore);
            intent.putExtra(KEY_WINNER_BG, winnerBG);
            intent.putExtra(KEY_CONTACT, contact);

            startActivity(intent);
        }
    }

    private void clearOutScores() {
        blueScore = 0;
        redScore = 0;

        blueTeam.setText("" + blueScore);
        redTeam.setText("" + redScore);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
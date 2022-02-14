package com.example.mp7scorecountersettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WinnerActivity extends AppCompatActivity {

    public static final String TAG = "WinnerActivity";
    public static final int REQUEST_CALL_PHONE = 1;
    private TextView winningTeam;
    private Button buttonCall;
    private ImageView imageView;
    private String winner;
    private String winnerBG;
    private String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "inside of onCreate method of WinnerActivity");
        setContentView(R.layout.activity_winner);

        getReferencesToWidgets();
        setWinningTeamText();

        Log.d(TAG, "end of onCreate method of WinnerActivity");
    }

    private void getReferencesToWidgets(){
        Log.d(TAG, "inside of getReferencesToWidgets method");

        winningTeam = findViewById(R.id.winning_team);
        buttonCall = findViewById(R.id.button_call);

        Log.d(TAG, "end of getReferencesToWidgets method");
    }

    private void setWinningTeamText(){
        Log.d(TAG, "inside of setWinningTeamText method");
        Intent intent = getIntent();

        winner = intent.getStringExtra(MainActivity.KEY_EXTRA_WINNER);
        winnerBG = intent.getStringExtra(MainActivity.KEY_WINNER_BG);
        contact = intent.getStringExtra(MainActivity.KEY_CONTACT);

        setWinnerBG(winnerBG);

        int finalScore = intent.getIntExtra(MainActivity.KEY_EXTRA_FINAL_SCORE, 0);

        if(finalScore > 1) {
            winningTeam.setText(winner + " Team Won By " + finalScore + " Points!");
        }
        else {
            winningTeam.setText(winner + " Team Won By " + finalScore + " Point!");
        }
        Log.d(TAG, "end of setWinningTeamText method");
    }

    private void setWinnerBG(String winnerBG) {
        imageView = findViewById(R.id.winner_bg);
        switch (winnerBG){
            case "medal":
                imageView.setImageResource(R.drawable.medal);
                break;
            case "cup":
                imageView.setImageResource(R.drawable.cup);
                break;
            case "thumbs_up":
                imageView.setImageResource(R.drawable.thumbs_up);
                break;
        }
    }

    public void sendText(View view) {
        Log.d(TAG, "inside of sendText method");
        String textToSend = winningTeam.getText().toString();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Pick an app")
                .setText(textToSend)
                .startChooser();
        Log.d(TAG, "end of sendText method");
    }

    public void findLocation(View view) {
        Log.d(TAG, "inside of findLocation method");

        String loc = "basketball near me";
        Uri geoLoc = Uri.parse("geo:0,0?q="+loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, geoLoc);
        if(intent.resolveActivity(getPackageManager())   !=  null  ){
            startActivity(intent);
        }
        else{
            Log.d(TAG, "Cannot find location "+loc);
            Toast.makeText(this, "Cannot find location "+loc, Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "end of findLocation method");
    }

    public void callPhone(View view) {
        Log.d(TAG, "inside of callPhone method");

        //ACTION_CALL needs AndroidManifest permissions
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+contact));
        Log.d(TAG,"The phone number is " + contact);
        if(intent.resolveActivity(getPackageManager()) != null){
            //need to check permissions
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "need to request permission to CALL_PHONE");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                //need to override onRequestPermissionsResult method for when the REQUEST_CALL_PHONE triggers the callback method
            }
            else{
                // have the permission to CALL_PHONE
                startActivity(intent);
            }
        }
        else{
            Log.d(TAG, "Cannot call phone");
            Toast.makeText(this, "Cannot call phone", Toast.LENGTH_LONG).show();
        }

        Log.d(TAG, "end of callPhone method");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "inside onRequestPermissionsResult method");
        switch (requestCode){
            case REQUEST_CALL_PHONE:
                if( (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) ){
                    callPhone(buttonCall); //place call by calling our method again now that we have permission
                }
                else{
                    Log.d(TAG, "Permission to call not granted");
                }
                break;
        }
        Log.d(TAG, "end of onRequestPermissionsResult method");
    }
}
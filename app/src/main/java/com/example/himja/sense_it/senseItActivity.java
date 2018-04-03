package com.example.himja.sense_it;

import android.content.ActivityNotFoundException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import java.util.Locale;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneChatOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneInput;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import com.spotify.sdk.android.player.Metadata;


public class senseItActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInput;
    private ImageButton mSpeakButton;

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "ddb3ab95a0034be698313a1bd9b56d71";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "senseit-login://callback";
    private Player mPlayer;
    public PlaybackState mCurrentPlaybackState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sense_it);

        Button voiceAnalyzer = (Button)findViewById(R.id.voiceAnalyzer);
        voiceAnalyzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myView) {
               final String speechToText = mVoiceInput.getText().toString();
                final ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2018-03-11");

                toneAnalyzer.setUsernameAndPassword("d9df3dd4-be21-4e03-8704-42551cf8f0ab","5oKZqXK26RrZ");


                ToneOptions options = new ToneOptions.Builder().addTone(ToneOptions.Tone.EMOTION).html(speechToText).build();

                ServiceCall call =toneAnalyzer.tone(options);
                call.enqueue(new ServiceCallback<ToneAnalysis>() {
                    @Override
                    public void onResponse(ToneAnalysis response) {


                        List<ToneScore> scores = response.getDocumentTone().getTones();


                        Intent myIntent = new Intent(senseItActivity.this, MainActivity.class);

                        String detectedTones ="";
                        for(ToneScore score:scores){
                            if(score.getScore()>0.5f){
                                detectedTones+=scores.get(0).getToneName()+ " ";

                            }




                        }
                        final String toastMessage = "The following emotions were detected: \n\n" + detectedTones.toUpperCase();
                        if(toastMessage.contains("JOY")){
                            startActivity(myIntent);

                        }



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), toastMessage, Toast.LENGTH_LONG).show();
                            }
                        });




                    }



                    @Override
                    public void onFailure(Exception e) {

                    }
                });

               //Intent myIntent = new Intent(senseItActivity.this, MainActivity.class);
                //startActivity(myIntent);
            }
        });



        mVoiceInput = (TextView)findViewById(R.id.voiceInput);
        mSpeakButton = (ImageButton)findViewById(R.id.voiceButton);
        mSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

    }

    private void startVoiceInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi! How are you feeling today?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && null!= data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInput.setText(result.get(0));
                }
                break;
            }
        }
    }

    /*public void voiceAnalyzerButton(View view){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }*/
}

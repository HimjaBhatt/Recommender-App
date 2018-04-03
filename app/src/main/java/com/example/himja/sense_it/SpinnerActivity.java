package com.example.himja.sense_it;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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


/**
 * Created by Himja on 03/04/18.
 */

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private Player mPlayer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        //Creating an array adapter
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.songs_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Applying the adapter to the spinner
        spinner.setAdapter(adapter1);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String item = adapterView.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        String uri;
        switch (position){
            case 0:
                uri= "spotify:track:6NPVjNh8Jhru9xOmyQigds";
                break;
            case 1:
               uri = "spotify:track:7xHWNBFm6ObGEQPaUxHuKO";
               break;

            default:
                throw new IllegalArgumentException("View ID does not have an associated URI to play");

        }
        mPlayer.playUri(mOperationCallback, uri, 0, 0);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.e("MainActivity","OK!");
        }

        @Override
        public void onError(Error error) {
            Log.e("ERROR:" , "error");
        }
    };




}

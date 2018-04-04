package com.example.himja.sense_it;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ScrollView;
import android.text.TextUtils;
import android.widget.Toast;

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

import org.json.JSONException;

import java.io.IOException;


/**
 * Created by Himja on 03/04/18.
 */

 public class SongsActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback{
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "ddb3ab95a0034be698313a1bd9b56d71";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "senseit-login://callback";

    private static final String TEST_SONG_URI = "spotify:track:2TpxZ7JUBn3uw46aR7qd6V";

    public static final String textMessage = "com.example.himja.MESSAGE";

    private Player mPlayer;
    public PlaybackState mCurrentPlaybackState;
    private TextView mMetadataText;
    private Metadata mMetadata;
    private TextView mStatusText;
    private ScrollView mStatusTextScrollView;
    public static final String TAG = "SpotifySdkDemo";


    private static final int[] REQUIRES_INITIALIZED_STATE = {
            R.id.play_button,
            R.id.pause_button};


    private static final int REQUEST_CODE = 1337;

    public SongsActivity() throws IOException, JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_activity);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        Intent myIntent = getIntent();


    }
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.e("MainActivity","OK!");
        }

        @Override
        public void onError(Error error) {
            logStatus("ERROR:" + error);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(SongsActivity.this);
                        mPlayer.addNotificationCallback(SongsActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Disposing the player after playback is done to prevent leaking resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }


    public void onPlaybackButtonClicked(View view) {
        String uri;
        switch (view.getId()) {
            // Handle event type as necessary
            case R.id.pharell_song:
                uri= "spotify:track:6NPVjNh8Jhru9xOmyQigds";
                break;
            case R.id.sia_song:
                uri = "spotify:track:7xHWNBFm6ObGEQPaUxHuKO";
                break;
            case R.id.train_song:
                uri ="spotify:track:5i0eU4qWEhgsDcG6xO5yvy";
                break;
            case R.id.imagine_song:
                uri="spotify:track:7F3uo3304R2CTvOseU3H7u";
                break;
            case R.id.jason_song:
                uri ="spotify:track:4ot2lykiAQeZC1GAnNQNJ4";
                break;
            case R.id.cheap_song:
                uri ="spotify:track:3S4px9f4lceWdKf0gWciFu";
                break;
            case R.id.michael_song:
                uri ="spotify:track:5ChkMS8OtdzJeqyybCc9R5";
                break;
            case R.id.sugar_song:
                uri ="spotify:track:494OU6M7NOf4ICYb4zWCf5";
                break;
            case R.id.justin_song:
                uri ="spotify:track:5CtI0qwDJkDQGwXD1H1cLb";
                break;
            case R.id.mgmt_song:
                uri ="spotify:track:1jJci4qxiYcOHhQR247rEU";
                break;

            default:
                throw new IllegalArgumentException("View ID does not have an associated URI to play");

        }
        //logStatus("Starting playback for " + uri);
        mPlayer.playUri(mOperationCallback, uri, 0, 0);
    }

    public void onPauseButtonClicked(View view) {
        if (mCurrentPlaybackState != null && mCurrentPlaybackState.isPlaying) {
            mPlayer.pause(mOperationCallback);
        } else {
            mPlayer.resume(mOperationCallback);
        }
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {

    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        // mPlayer.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

   /* @Override
    public void onLoginFailed(int i) {
        Log.d("MainActivity", "Login failed");
    }*/

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    //Error handling
    private void logStatus(String status) {
        Log.i(TAG, status);
        if (!TextUtils.isEmpty(mStatusText.getText())) {
            mStatusText.append("\n");
        }
        mStatusText.append(">>>" + status);
        mStatusTextScrollView.post(new Runnable() {
            @Override
            public void run() {
                // Scroll to the bottom
                mStatusTextScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

}

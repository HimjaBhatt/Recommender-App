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

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import okhttp3.Request;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static junit.framework.Assert.assertEquals;
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

import static junit.framework.Assert.assertEquals;


public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback, AdapterView.OnItemSelectedListener
{

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
    Spinner spinner;




    public static final String TAG = "SpotifySdkDemo";


    private static final int[] REQUIRES_INITIALIZED_STATE = {
            R.id.play_button,
            R.id.pause_button};


    private static final int REQUEST_CODE = 1337;

    public MainActivity() throws IOException, JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        Intent myIntent = getIntent();

        List<Songs> songs = initializeData();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(songs, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addNotificationCallback(MainActivity.this);
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
            case R.id.play_button:
                uri= "spotify:track:6NPVjNh8Jhru9xOmyQigds";
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

   @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
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

    class Songs{

        String happy_songs_uri;
        String title;
        String artist;

        Songs(String happy_songs_uri, String title, String artist){
            this.happy_songs_uri = happy_songs_uri;
            this.title = title;
            this.artist = artist;
        }


        
    }

    public List<Songs> initializeData(){
       List<Songs> happy_songs = new ArrayList<>();
        //Pharell - Happy
        happy_songs.add(new Songs("spotify:track:6NPVjNh8Jhru9xOmyQigds","Happy", "Pharell"));
        //Sia ft. Kendrick Lamar - The Greatest
        happy_songs.add(new Songs("spotify:track:7xHWNBFm6ObGEQPaUxHuKO" ,"The Greatest", "Sia"));
        //
        happy_songs.add(new Songs("spotify:track:5i0eU4qWEhgsDcG6xO5yvy", "Hey, Soul Sister"," Train"));
        return happy_songs;
    }
    private SpotifyService mService;
    private OkHttpClient mClient = new OkHttpClient();

    public Response getTrack() throws Exception{

        Track payload =mService.getTrack("6NPVjNh8Jhru9xOmyQigds");
        Request request= new Request.Builder().get().url("https://api.spotify.com/v1/tracks/6NPVjNh8Jhru9xOmyQigds").build();

        Response response = mClient.newCall(request).execute();
        assertEquals(200, response.code());

     //TODO: Add a text view reference

        return response;
    }

    private Headers mAuthHeader;

    //private OkHttpClient mClient = new OkHttpClient();
    //final String happySong = "spotify:track:6NPVjNh8Jhru9xOmyQigds";
    //final int limit = 10;
    //private static final String LOG_TAG = MainActivity.class.getSimpleName();


   /* public void getRecommendations() throws Exception{
        Map<String, Object> options = new HashMap<>();
        options.put("seed_artists","4cJKxS7uOPhwb5UQ70sYpN,6UUrUCIZtQeOf8tC0WuzRy");
        SpotifyApi api = new SpotifyApi();
        SpotifyService service = api.getService();
        Recommendations payload = service.getRecommendations(options);
        Request requestme = new Request.Builder().get().headers(mAuthHeader).url("https://api.spotify.com/v1/recommendations?seed_artists=4cJKxS7uOPhwb5UQ70sYpN,6UUrUCIZtQeOf8tC0WuzRy").build();
        Response response = mClient.newCall(requestme).execute();
        assertEquals(200, response.code());

        Log.i(LOG_TAG, requestme.toString());

        TextView tv = (TextView)findViewById(R.id.recommend);
        tv.setText(payload.tracks.get(5).toString());


    }*/


/*Spinner spinner = (Spinner) findViewById(R.id.spinner);

        //Creating an array adapter
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.songs_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Applying the adapter to the spinner
       spinner.setAdapter(adapter1);

        spinner.setOnItemClickListener(this);*/


}








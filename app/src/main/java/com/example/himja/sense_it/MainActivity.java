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
import android.widget.Button;
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


public class MainActivity extends Activity
{
    Button song_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Songs> songs = initializeData();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(songs, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


        class Songs{

        String happy_songs_uri;
        String title;
        String artist;

        Songs(String title, String artist){
            this.happy_songs_uri = happy_songs_uri;
            this.title = title;
            this.artist = artist;
        }


        
    }

    public List<Songs> initializeData(){
       List<Songs> happy_songs = new ArrayList<>();
        //Pharell - Happy
        happy_songs.add(new Songs("Happy", "Pharell"));
        //Sia ft. Kendrick Lamar - The Greatest
        happy_songs.add(new Songs("The Greatest", "Sia"));
        //
        happy_songs.add(new Songs( "Hey, Soul Sister"," Train"));
        happy_songs.add(new Songs("Whatever It Takes","Imagine Dragons"));
        happy_songs.add(new Songs("The Other Side","Jason Derulo"));
        happy_songs.add(new Songs("Cheap Thrills","Sia"));
        happy_songs.add(new Songs("Michael Jackson","Billie Jean"));
        happy_songs.add(new Songs("Maroon V","Sugar"));
        happy_songs.add(new Songs("Despacito","Justin Bieber"));
        happy_songs.add(new Songs("Kids","MGMT"));
        return happy_songs;
    }

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








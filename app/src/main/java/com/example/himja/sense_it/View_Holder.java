package com.example.himja.sense_it;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Himja on 03/04/18.
 */

public class View_Holder extends RecyclerView.ViewHolder {
    TextView title;
    TextView artist;
    Button play_button;

    View_Holder(View itemView){
        super(itemView);
        title = (TextView)itemView.findViewById(R.id.title);
        artist = (TextView)itemView.findViewById(R.id.artist_name);
        play_button = (Button)itemView.findViewById(R.id.play_button);
    }
}

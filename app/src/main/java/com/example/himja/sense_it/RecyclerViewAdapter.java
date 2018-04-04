package com.example.himja.sense_it;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.security.acl.LastOwnerException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Himja on 03/04/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<View_Holder>{
    List<MainActivity.Songs> list = Collections.EMPTY_LIST;
    Context context;
    Button play_button;


    public RecyclerViewAdapter(List<MainActivity.Songs> list, Context context){
        this.list = list;
        this.context = context;
        this.play_button = play_button;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Initializing the view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.artist.setText(list.get(position).artist);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerview){
        super.onAttachedToRecyclerView(recyclerview);
    }

    //Inserting a new item in the recycler view
    public void insert(int position, MainActivity.Songs song){
        list.add(position, song);
        notifyItemInserted(position);
    }

    public void delete(MainActivity.Songs song){
        int position = list.indexOf(song);
        list.remove(position);
        notifyItemRemoved(position);
    }


}

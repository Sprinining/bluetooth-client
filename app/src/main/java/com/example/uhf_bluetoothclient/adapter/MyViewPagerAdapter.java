package com.example.uhf_bluetoothclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf_bluetoothclient.R;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends RecyclerView.Adapter<MyViewPagerAdapter.MyViewPagerViewHolder> {
    private List<String> titles = new ArrayList<>();

    public MyViewPagerAdapter(ArrayList<String> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public MyViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewPagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewPagerViewHolder holder, int position) {
        holder.tv.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    static class MyViewPagerViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public MyViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}

package com.example.uhf_bluetoothclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.TaginfoCellBinding;
import com.example.uhf_bluetoothclient.entity.TagCells;

import java.util.List;

public class TagInfoRecyclerViewAdapter extends RecyclerView.Adapter<TagInfoRecyclerViewAdapter.TagInfoViewHolder> {

    private final List<TagCells.TagCell> tagCellList;
    private Context context;

    public TagInfoRecyclerViewAdapter(Context context, List<TagCells.TagCell> tagCellList) {
        this.context = context;
        this.tagCellList = tagCellList;
    }

    @NonNull
    @Override
    public TagInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taginfo_cell, parent, false);
        return new TagInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagInfoViewHolder holder, int position) {
        if (holder.binding != null) {
            holder.binding.tvEpc.setText(tagCellList.get(position).getEpc());
            // recyclerView
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            holder.binding.rvTagDetail.setLayoutManager(layoutManager);
            TagInfoDetailRecyclerViewAdapter tagInfoDetailRecyclerViewAdapter = new TagInfoDetailRecyclerViewAdapter(tagCellList.get(position).getAntennaCells());
            holder.binding.rvTagDetail.setAdapter(tagInfoDetailRecyclerViewAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return tagCellList.size();
    }

    static class TagInfoViewHolder extends RecyclerView.ViewHolder {
        private final TaginfoCellBinding binding;

        public TagInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TaginfoCellBinding.bind(itemView);
        }
    }
}

package com.example.uhf_bluetoothclient.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.TaginfoDetailCellBinding;
import com.example.uhf_bluetoothclient.entity.TagCells;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TagInfoDetailRecyclerViewAdapter extends RecyclerView.Adapter<TagInfoDetailRecyclerViewAdapter.DetailViewHolder> {
    private final List<TagCells.TagCell.AntennaCell> antennaCells;

    public TagInfoDetailRecyclerViewAdapter(List<TagCells.TagCell.AntennaCell> antennaCells) {
        this.antennaCells = antennaCells;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taginfo_detail_cell, parent, false);
        return new DetailViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        if (holder.binding != null) {
            holder.binding.tvAntennaId.setText("" + antennaCells.get(position).getAntennaId());
            LocalDateTime localDateTime = antennaCells.get(position).getReadTime();
            holder.binding.tvFirstReadTime.setText(localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS")));
            holder.binding.tvCount.setText("" + antennaCells.get(position).getCount());
        }
    }

    @Override
    public int getItemCount() {
        return antennaCells == null ? 0 : antennaCells.size();
    }

    static class DetailViewHolder extends RecyclerView.ViewHolder {
        private final TaginfoDetailCellBinding binding;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TaginfoDetailCellBinding.bind(itemView);
        }
    }
}

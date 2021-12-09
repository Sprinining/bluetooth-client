package com.example.uhf_bluetoothclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.TaginfoCellBinding;
import com.example.uhf_bluetoothclient.entity.LogBaseEpcInfo;

import java.util.List;

public class TagInfoRecyclerViewAdapter extends RecyclerView.Adapter<TagInfoRecyclerViewAdapter.TagInfoViewHolder> {

    private final List<LogBaseEpcInfo> tagInfoList;

    public TagInfoRecyclerViewAdapter(List<LogBaseEpcInfo> tagInfoList) {
        this.tagInfoList = tagInfoList;
    }

    @NonNull
    @Override
    public TagInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taginfo_cell, parent, false);
        return new TagInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagInfoViewHolder holder, int position) {
        holder.tv_epc.setText(tagInfoList.get(position).getEpc());
        holder.id.setText(String.valueOf(position + 1));
        if (holder.taginfoCellBinding != null) {
            holder.taginfoCellBinding.setTagInfo(tagInfoList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return tagInfoList == null ? 0 : tagInfoList.size();
    }

    static class TagInfoViewHolder extends RecyclerView.ViewHolder {
        private final TaginfoCellBinding taginfoCellBinding;
        private final TextView tv_epc;
        private final TextView id;

        public TagInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            taginfoCellBinding = TaginfoCellBinding.bind(itemView);
            tv_epc = itemView.findViewById(R.id.tv_tagInfo_epc);
            id = itemView.findViewById(R.id.tv_tagInfo_id);
        }
    }
}

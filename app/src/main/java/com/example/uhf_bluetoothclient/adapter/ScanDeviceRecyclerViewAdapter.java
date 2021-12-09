package com.example.uhf_bluetoothclient.adapter;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf_bluetoothclient.R;

import java.util.List;

public class ScanDeviceRecyclerViewAdapter extends RecyclerView.Adapter<ScanDeviceRecyclerViewAdapter.MyViewHolder> {

    private int currentSelectedIndex = -1;
    private final List<BluetoothDevice> list;

    public ScanDeviceRecyclerViewAdapter(List<BluetoothDevice> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_found_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (list.get(position).getName() == null || list.get(position).getName().equals("")) {
            holder.tv_device_name.setText("无设备名");
        } else {
            holder.tv_device_name.setText(list.get(position).getName());
        }
        holder.tv_device_mac.setText(list.get(position).getAddress());

        if (currentSelectedIndex == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFC0C0C0"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }

        if (myClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                myClickListener.listen(holder.getBindingAdapterPosition());

                int lastIndex = currentSelectedIndex;
                currentSelectedIndex = holder.getBindingAdapterPosition();
                notifyItemChanged(currentSelectedIndex);
                if (lastIndex >= 0) {
                    notifyItemChanged(lastIndex);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_device_name;
        private final TextView tv_device_mac;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device_name = itemView.findViewById(R.id.tv_device_name);
            tv_device_mac = itemView.findViewById(R.id.tv_device_mac);
        }
    }

    private MyClickListener myClickListener;

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void listen(int position);
    }
}

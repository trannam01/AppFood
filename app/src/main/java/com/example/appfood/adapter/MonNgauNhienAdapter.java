package com.example.appfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appfood.R;
import com.example.lib.model.Mon;

import java.text.DecimalFormat;
import java.util.List;

public class MonNgauNhienAdapter extends RecyclerView.Adapter<MonNgauNhienAdapter.GetViewMonNgauNhien> {
    Context context;
    List<Mon> list;

    public MonNgauNhienAdapter(Context context, List<Mon> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetViewMonNgauNhien onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_mon_ngau_nhien,parent,false);
        GetViewMonNgauNhien getViewMonNgauNhien = new GetViewMonNgauNhien(view);
        return getViewMonNgauNhien;
    }

    @Override
    public void onBindViewHolder(@NonNull GetViewMonNgauNhien holder, int position) {
        Mon mon = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia.setText(decimalFormat.format(Double.parseDouble(mon.getGia()))+" đ");
        holder.tenmon.setText(mon.getTenmon());
        holder.mota.setText(mon.getMota());
        Glide.with(context).load(mon.getHinhmon())
                .placeholder(R.drawable.img_default)
                .error(R.drawable.img_error)
                .into(holder.hinhmon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GetViewMonNgauNhien extends  RecyclerView.ViewHolder{
        TextView gia,tenmon,mota;
        ImageView hinhmon;
        public GetViewMonNgauNhien(@NonNull View itemView) {
            super(itemView);
            gia = itemView.findViewById(R.id.gia);
            tenmon = itemView.findViewById(R.id.tenmon);
            mota = itemView.findViewById(R.id.mota);
            hinhmon = itemView.findViewById(R.id.hinhmon);
        }
    }
}

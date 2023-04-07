package com.example.appfood.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.lib.InterfaceResponsitory.ItemClickOptions;
import com.example.lib.model.BinhLuan;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.GetViewMonNgauNhien> {
    Context context;
    List<BinhLuan.Result> list;
    ArrayList<BinhLuan> arrayMon;

    public BinhLuanAdapter(Context context, List<BinhLuan.Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetViewMonNgauNhien onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_binhluan,parent,false);
        GetViewMonNgauNhien getViewMonNgauNhien = new GetViewMonNgauNhien(view);
        return getViewMonNgauNhien;
    }

    @Override
    public void onBindViewHolder(@NonNull GetViewMonNgauNhien holder, int position) {
        BinhLuan.Result monResult = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        String coverPhone = monResult.getSdt().toString();
        coverPhone = coverPhone.substring(0, 7);
        holder.userphone.setText(coverPhone + "***");
        holder.content.setText(monResult.getNoidung());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GetViewMonNgauNhien extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userphone, content;
        private ItemClickOptions itemClickOptions;
        public GetViewMonNgauNhien(@NonNull View itemView) {
            super(itemView);
            userphone = itemView.findViewById(R.id.userphone);
            content = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        public void setItemClickOptions(ItemClickOptions itemClickOptions) {
            this.itemClickOptions = itemClickOptions;
        }


        @Override
        public void onClick(View view) {
            itemClickOptions.onClickOptions(view, getAbsoluteAdapterPosition(),0);
        }
    }
}

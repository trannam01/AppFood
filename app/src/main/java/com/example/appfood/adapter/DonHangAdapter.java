package com.example.appfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.activity.ChiTietDonHangActivity;
import com.example.lib.InterfaceResponsitory.ItemClickOptions;
import com.example.lib.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.GetViewDonHang>{

    Context context;
    List<DonHang.Result> list;

    public DonHangAdapter(Context context, List<DonHang.Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetViewDonHang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_don_hang, parent, false);
        GetViewDonHang getViewDonHang = new GetViewDonHang(view);
        return getViewDonHang;
    }

    @Override
    public void onBindViewHolder(@NonNull GetViewDonHang holder, int position) {
        DonHang.Result donHangResult = list.get(position);
        holder.madonhang.setText(String.valueOf(donHangResult.getId()));
        holder.tenkh.setText(donHangResult.getTenkhachhang());
        holder.diachi.setText(donHangResult.getDiachi());
        holder.sodienthoai.setText(donHangResult.getSodienthoai());
        holder.tongtien.setText(Double.toString(donHangResult.getTongtien()));


        holder.setItemClickOptions(new ItemClickOptions() {
            @Override
            public void onClickOptions(View view, int pos, int value) {
                Intent intent = new Intent(context, ChiTietDonHangActivity.class);
                intent.putExtra("chitietdonhang",donHangResult);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GetViewDonHang extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView madonhang, tenkh, diachi, sodienthoai, tongtien;
        ImageView hinhdonhang;
        private ItemClickOptions itemClickOptions;

        public GetViewDonHang(@NonNull View itemView) {
            super(itemView);
            hinhdonhang = itemView.findViewById(R.id.hinhdonhang);
            madonhang = itemView.findViewById(R.id.madonhang);
            tenkh = itemView.findViewById(R.id.chudon);
            diachi = itemView.findViewById(R.id.diachi);
            sodienthoai = itemView.findViewById(R.id.lienhe);
            tongtien = itemView.findViewById(R.id.tongtien);


            itemView.setOnClickListener(this);
        }

        public void setItemClickOptions(ItemClickOptions itemClickOptions) {
            this.itemClickOptions = itemClickOptions;
        }

        @Override
        public void onClick(View view) {
            itemClickOptions.onClickOptions(view, getAdapterPosition(),0);
        }
    }
}

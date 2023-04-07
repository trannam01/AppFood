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
import com.example.lib.InterfaceResponsitory.ItemClickOptions;
import com.example.lib.model.ChiTietDonHang;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.GetViewChiTietDonHang>{

    Context context;
    List<ChiTietDonHang.Result> list;

    public ChiTietDonHangAdapter(Context context, List<ChiTietDonHang.Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetViewChiTietDonHang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chi_tiet_don_hang, parent, false);
        GetViewChiTietDonHang getViewChiTietDonHang = new GetViewChiTietDonHang(view);
        return getViewChiTietDonHang;
    }

    @Override
    public void onBindViewHolder(@NonNull GetViewChiTietDonHang holder, int position) {
        ChiTietDonHang.Result donhangResult = list.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia_donhang.setText(decimalFormat.format(Double.parseDouble(donhangResult.getGia()))+" đ");
        holder.tenmon_donhang.setText(donhangResult.getTenmon());
        holder.soluong_donhang.setText(donhangResult.getSoluong());

        Glide.with(context).load(donhangResult.getHinhmon())
                .placeholder(R.drawable.img_default)
                .error(R.drawable.img_error)
                .into(holder.hinhmon_donhang);
        /*holder.setItemClickOptions(new ItemClickOptions() {
            @Override
            public void onClickOptions(View view, int pos, int value) {
                Intent intent = new Intent(context, ChiTietMonActivity.class);
                intent.putExtra("chitietmondh",donhangResult);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GetViewChiTietDonHang extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView gia_donhang, tenmon_donhang, soluong_donhang;
        ImageView hinhmon_donhang;
        private ItemClickOptions itemClickOptions;

        public GetViewChiTietDonHang(@NonNull View itemView) {
            super(itemView);
            gia_donhang = itemView.findViewById(R.id.gia_donhang);
            tenmon_donhang = itemView.findViewById(R.id.tenmon_donhang);
            soluong_donhang = itemView.findViewById(R.id.soluong_donhang);
            hinhmon_donhang = itemView.findViewById(R.id.hinhmon_donhang);
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

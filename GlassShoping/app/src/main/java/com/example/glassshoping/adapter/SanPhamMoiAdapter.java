package com.example.glassshoping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glassshoping.R;
import com.example.glassshoping.model.SanPham;
import com.example.glassshoping.model.SanPhamModel;
import com.example.glassshoping.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> array;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtname.setText(sanPhamMoi.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        holder.txtgia.setText("Giá: "+decimalFormat.format(sanPhamMoi.getPrice())+"VNĐ");
        Glide.with(context).load(sanPhamMoi.getImage()).into(holder.imghinhanh);
    }

    @Override
    public int getItemCount() {
        return array != null ? array.size() : 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtgia, txtname;
        ImageView imghinhanh;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtgia=itemView.findViewById(R.id.itemsp_price);
            txtname = itemView.findViewById(R.id.itemsp_name);
            imghinhanh = itemView.findViewById(R.id.itemsp_image);
        }
    }
}

package com.example.glassshoping.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glassshoping.R;
import com.example.glassshoping.model.Item;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {
    List<Item> itemlist;

    public ChiTietAdapter(List<Item> itemlist) {
        this.itemlist = itemlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtten.setText(itemlist.get(position).getTensp());
        holder.txtsoluong.setText("Số lượng: " + itemlist.get(position).getSoluong());

        // Nếu bạn dùng Glide/Picasso để load ảnh:
        Glide.with(holder.imgchitiet.getContext())
                .load(itemlist.get(position).getHinhanh())
                .placeholder(R.drawable.newimage)
                .into(holder.imgchitiet);
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgchitiet;
        TextView txtten, txtsoluong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgchitiet = itemView.findViewById(R.id.item_chitiet_img);
            txtten = itemView.findViewById(R.id.item_tensanpham_chitiet);
            txtsoluong= itemView.findViewById(R.id.item_soluong_chitiet);
        }
    }
}

package com.manager.glassshoping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.glassshoping.Interface.ItemClickListener;
import com.manager.glassshoping.R;
import com.manager.glassshoping.activity.ChitietActivity;
import com.manager.glassshoping.model.EventBus.SuaXoaEvent;
import com.manager.glassshoping.model.SanPham;
import com.manager.glassshoping.model.SanPhamModel;
import com.manager.glassshoping.model.SanPhamMoi;
import com.manager.glassshoping.utils.Utils;

import org.greenrobot.eventbus.EventBus;

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
        SanPham sanPham= new SanPham(sanPhamMoi.getId(),sanPhamMoi.getName(),sanPhamMoi.getDescription(),sanPhamMoi.getCategory_id(),sanPhamMoi.getPrice(),sanPhamMoi.getStock(),sanPhamMoi.getImage());
        holder.txtname.setText(sanPhamMoi.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        holder.txtgia.setText("Giá: "+decimalFormat.format(sanPhamMoi.getPrice())+"VNĐ");
        if(sanPham.getImage().contains("http")){
            Glide.with(context).load(sanPhamMoi.getImage()).into(holder.imghinhanh);
        }else {
            String hinh = Utils.BASE_URL+"images/"+sanPham.getImage();
            Glide.with(context).load(hinh).into(holder.imghinhanh);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    //click
                    Intent intent = new Intent(context, ChitietActivity.class);
                    intent.putExtra("chitiet",sanPham);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    EventBus.getDefault().postSticky(new SuaXoaEvent(sanPhamMoi));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array != null ? array.size() : 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtgia, txtname;
        ImageView imghinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtgia=itemView.findViewById(R.id.itemsp_price);
            txtname = itemView.findViewById(R.id.itemsp_name);
            imghinhanh = itemView.findViewById(R.id.itemsp_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(),false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0,0,getAdapterPosition(),"Sửa");
            menu.add(0,0,getAdapterPosition(),"Xoá");
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(),true);
            return false;
        }
    }
}

package com.manager.glassshoping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.glassshoping.Interface.ItemClickListener;
import com.manager.glassshoping.R;
import com.manager.glassshoping.activity.ChitietActivity;
import com.manager.glassshoping.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;

public class KinhRamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPham> array;
    private static  final int VIEW_TYPE_DATA=0;
    private static  final int VIEW_TYPE_LOADING=1;

    public KinhRamAdapter(Context context, List<SanPham> array) {
        this.context = context;
        this.array = array;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kinh_ram,parent,false);
            return new MyviewHolder(view);
        }else{
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof KinhRamAdapter.MyviewHolder){
            KinhRamAdapter.MyviewHolder myviewHolder = (KinhRamAdapter.MyviewHolder) holder;
            SanPham sanPham = array.get(position);
            myviewHolder.tensp.setText(sanPham.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myviewHolder.giasp.setText("Giá: "+decimalFormat.format(sanPham.getPrice())+"VNĐ");
            myviewHolder.mota.setText(sanPham.getDescription());
            Glide.with(context).load(sanPham.getImage()).into(myviewHolder.hinhanh);
            myviewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        //click
                        Intent intent = new Intent(context, ChitietActivity.class);
                        intent.putExtra("chitiet",sanPham);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            KinhRamAdapter.LoadingViewHolder loadingViewHolder = (KinhRamAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return array.get(position)==null?VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }
    @Override
    public int getItemCount() {
        return array.size();
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar= itemView.findViewById(R.id.progressbar);
        }
    }
    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp, giasp, mota;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyviewHolder(@NonNull View itemView){
            super(itemView);
            tensp = itemView.findViewById(R.id.itemkr_ten);
            giasp = itemView.findViewById(R.id.itemkr_gia);
            mota = itemView.findViewById(R.id.itemkr_mota);
            hinhanh = itemView.findViewById(R.id.itemkr_image);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(),false);
        }
    }
}



package com.example.glassshoping.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glassshoping.R;
import com.example.glassshoping.model.DonHang;
import com.example.glassshoping.model.DonHangModel;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private  RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<DonHang> listdonhang;
    Context context;

    public DonHangAdapter(List<DonHang> listdonhang, Context context) {
        this.listdonhang = listdonhang;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.txtdonhang.setText("Đơn hàng: "+donHang.getId()+" ");
        holder.txttrangthai.setText(trangThaiDon(donHang.getTrangthai()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        //adapter chitiet
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(donHang.getItem());
        holder.reChitiet.setLayoutManager(layoutManager);
        Log.d("TEST", "getOders: "+chiTietAdapter.getItemCount());
        holder.reChitiet.setAdapter(chiTietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);
    }
    private String trangThaiDon(int i){
        String result = "";
        switch (i){
            case 0:
                result="Đơn hàng đang được xử lí";
                break;
            case 1:
                result="Đơn hàng đã chấp nhận";
                break;
            case 2:
                result="Đơn hàng đã giao cho đơn vị vẫn chuyển";
                break;
            case 3:
                result="Thành công";
                break;
            case 4:
                result="Đã huỷ";
                break;
        }
        return result;
    }
    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtdonhang,txttrangthai;
        RecyclerView reChitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            txttrangthai= itemView.findViewById(R.id.trangthaidonhang);
            reChitiet = itemView.findViewById(R.id.recycleview_chitiet_donhang);
        }
    }
}

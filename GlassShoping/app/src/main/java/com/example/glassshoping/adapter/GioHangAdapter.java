package com.example.glassshoping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.glassshoping.Interface.IImangeClickListener;
import com.example.glassshoping.R;
import com.example.glassshoping.model.EventBus.TinhTongEvent;
import com.example.glassshoping.model.GioHang;
import com.example.glassshoping.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
            GioHang gioHang = gioHangList.get(position);
            holder.item_giohang_name.setText(gioHang.getTensp());
            holder.item_giohang_soluong.setText(gioHang.getSoluong()+" ");
            Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        holder.item_giohang_price.setText("Giá: "+decimalFormat.format(gioHang.getGiasp())+"VND");
        Double gia = gioHang.getGiasp()*gioHang.getSoluong();
        holder.item_giohang_totalprice.setText(decimalFormat.format(gia)+"VND");
        holder.setListener(new IImangeClickListener() {
            @Override
            public void onItemClick(View view, int pos, int giatri) {
                if(giatri==1){
                    if(gioHangList.get(pos).getSoluong()>1){
                        int soluong = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(soluong);
                        holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+" ");
                        Double gia = gioHangList.get(pos).getGiasp()*gioHangList.get(pos).getSoluong();
                        holder.item_giohang_totalprice.setText(decimalFormat.format(gia)+"VND");
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else if(gioHangList.get(pos).getSoluong()==1){
                        AlertDialog.Builder dBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                        dBuilder.setTitle("Thông báo");
                        dBuilder.setMessage("Bạn có muốn xoá sản phẩm khỏi giỏ hàng?");
                        dBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        dBuilder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dBuilder.show();

                    }
                }else if(giatri==2){
                    if(gioHangList.get(pos).getSoluong()<11){
                        int soluong = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(soluong);
                    }
                    holder.item_giohang_soluong.setText(gioHangList.get(pos).getSoluong()+" ");
                    Double gia = gioHangList.get(pos).getGiasp()*gioHangList.get(pos).getSoluong();
                    holder.item_giohang_totalprice.setText(decimalFormat.format(gia)+"VND");
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image, itemtru,itemcong;
        TextView item_giohang_name,item_giohang_price,item_giohang_soluong, item_giohang_totalprice;
        IImangeClickListener listener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image= itemView.findViewById(R.id.item_giohang_img);
            item_giohang_name= itemView.findViewById(R.id.item_giohang_name);
            item_giohang_price= itemView.findViewById(R.id.item_giohang_price);
            item_giohang_soluong= itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_totalprice= itemView.findViewById(R.id.item_giohang_totalprice);
            itemtru= itemView.findViewById(R.id.item_giohang_tru);
            itemcong = itemView.findViewById(R.id.item_giohang_cong);
            //eventclick
            itemcong.setOnClickListener(this);
            itemtru.setOnClickListener(this);
        }

        public void setListener(IImangeClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(v==itemtru){
                listener.onItemClick(v,getAdapterPosition(),1);
            }else if(v== itemcong){
                listener.onItemClick(v,getAdapterPosition(),2);
            }


        }
    }
}

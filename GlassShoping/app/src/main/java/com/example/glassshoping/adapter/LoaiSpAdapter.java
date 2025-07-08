package com.example.glassshoping.adapter;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.glassshoping.R;
import com.example.glassshoping.model.LoaiSp;
import com.example.glassshoping.model.SanPham;

import java.util.List;

public class LoaiSpAdapter extends BaseAdapter {
    List<LoaiSp> array;
    Context context;

    public LoaiSpAdapter(Context context, List<LoaiSp> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_sanpham, null);
            viewHolder.textTenSP=convertView.findViewById(R.id.item_tensanpham);
            viewHolder.imghinhanh= convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();

        }
        viewHolder.textTenSP.setText(array.get(position).getName());
        Glide.with(context).load(array.get(position).getImage()).into(viewHolder.imghinhanh);
        return convertView;
    }
    public class ViewHolder{
        TextView textTenSP;
        ImageView imghinhanh;

    }
}

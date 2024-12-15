package com.khaleghian.masi.imgmemlib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    List<ItemObject> listStorage;
    Context context;

    public CustomAdapter(LayoutInflater layoutInflater, List<ItemObject> listStorage, Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listStorage = listStorage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if (convertView==null)
        {
            listViewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.listview_with_text_image,parent,false);
            listViewHolder.textInListView=convertView.findViewById(R.id.textView);
            listViewHolder.imageInListView=convertView.findViewById(R.id.imageView);
            convertView.setTag(listViewHolder);
        }
        else
        {
            listViewHolder=(ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getContent());
        int imageResourceId=this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),"drawable",this.context.getPackageName());
        listViewHolder.imageInListView.setImageResource(imageResourceId);
        return convertView;
    }

    private class ViewHolder {
        TextView textInListView;
        ImageView imageInListView;
    }
}

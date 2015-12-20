package com.dasunica.offroute.map_data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasunica.offroute.R;

import java.util.List;

/**
 * Created by fran on 19/01/15.
 */
public class MapAdapter extends BaseAdapter {

    private Context context;
    private List<MapItem> items;

    public MapAdapter(Context context, List<MapItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.map_item,null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.map_name);
            holder.imageView = (ImageView) view.findViewById(R.id.map_file_icon);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        MapItem item = items.get(i);
        holder.txtTitle.setText(item.getMapName());
        if (item.getMapPath().endsWith(".map")) {
            holder.imageView.setImageResource(R.drawable.ic_map_file);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_folder);
        }
        return view;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }
}

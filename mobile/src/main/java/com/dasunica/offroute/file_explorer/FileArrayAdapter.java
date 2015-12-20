package com.dasunica.offroute.file_explorer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dasunica.offroute.R;

import java.io.File;
import java.util.List;

/**
 * Created by fran on 5/10/14.
 */
public class FileArrayAdapter extends ArrayAdapter<File> {

    private Context context;

    public FileArrayAdapter(Context context, int resource,
                             int textViewResourceId, List<File> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }

    public FileArrayAdapter(Context context, int resource,
                             int textViewResourceId, File[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
    }

    public FileArrayAdapter(Context context, int resource,
                             int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
    }

    public FileArrayAdapter(Context context, int textViewResourceId,
                             List<File> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    public FileArrayAdapter(Context context, int resource, File[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public FileArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        File file = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_chooser_layout,
                    null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitle.setText(file.getName());
        if (file.isDirectory()) {
            holder.imageView.setImageResource(R.drawable.ic_folder);
        } else if (file.getName().endsWith(".gpx")) {
            holder.imageView.setImageResource(R.drawable.ic_route_file);
        }else if (file.getName().endsWith(".map")) {
            holder.imageView.setImageResource(R.drawable.ic_map_file);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_unknown_file);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }
}

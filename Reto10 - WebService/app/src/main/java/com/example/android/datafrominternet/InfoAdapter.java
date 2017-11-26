package com.example.android.datafrominternet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.datafrominternet.Info;
import com.example.android.datafrominternet.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Osman on 29/10/2017.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    public ArrayList<Info> rooms;
    public Context context;

    public InfoAdapter(ArrayList<Info> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Info r = rooms.get(position);
        holder.nameText.setText("Distrito: " + r.name);
        holder.addresText.setText("Direccion: " + r.address);
        holder.cityText.setText("Ciudad: " + r.city);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityText, addresText, nameText;

        public ViewHolder(View itemView) {
            super(itemView);
            cityText = (TextView) itemView.findViewById(R.id.city);
            nameText = (TextView) itemView.findViewById(R.id.name);
            addresText = (TextView) itemView.findViewById(R.id.address);
        }
    }
}

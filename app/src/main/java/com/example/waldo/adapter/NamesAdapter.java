package com.example.waldo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waldo.R;

public class NamesAdapter extends RecyclerView.Adapter<NamesAdapter.Holder>{

    Context context;

    public NamesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.names_adapter,parent,false);
        return new NamesAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 1;
    }

     class Holder extends RecyclerView.ViewHolder{

        TextView txt_name;
        LinearLayout sds;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}



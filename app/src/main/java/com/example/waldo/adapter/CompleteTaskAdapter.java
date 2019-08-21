package com.example.waldo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.waldo.R;
import com.example.waldo.fragments.TaskDetailFragment;

import butterknife.ButterKnife;

public class CompleteTaskAdapter extends RecyclerView.Adapter<CompleteTaskAdapter.Holder> {

   Context context;
   ClickItem clickItem;

    public interface ClickItem{
        void click();
    }

    public void clickListerner(ClickItem clickItem){
        this.clickItem= clickItem;
    }

    public CompleteTaskAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.complete_task_adapter,viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.item_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new TaskDetailFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class Holder extends RecyclerView.ViewHolder{

      LinearLayout item_click;

      public Holder(@NonNull View itemView) {
          super(itemView);

          item_click = itemView.findViewById(R.id.item_click);
      }
  }
}

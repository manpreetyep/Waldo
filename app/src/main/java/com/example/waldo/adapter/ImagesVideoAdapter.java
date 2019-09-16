package com.example.waldo.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appunite.appunitevideoplayer.PlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.waldo.R;
import com.example.waldo.Utils.SquareLayout;
import com.example.waldo.models.ImageVideoModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class ImagesVideoAdapter extends RecyclerView.Adapter<ImagesVideoAdapter.Holder> {

    Context context;
    ClickItem clickItem;
    ArrayList<ImageVideoModel> list;

    public interface ClickItem{
        void click(int pos,String type);
    }

    public void clickListerner(ClickItem clickItem){
        this.clickItem= clickItem;
    }

    public ImagesVideoAdapter(Context context, ArrayList<ImageVideoModel> list) {
        this.context = context;
        this.list =list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_video_adapter,viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
      Glide.with(context)
                                    .load(list.get(i).thumbnail)
                                    .thumbnail(0.5f)
                                    .apply(new RequestOptions().placeholder(R.drawable.dummy)
                                    .error(R.drawable.dummy))
                                    .into(holder.img);

      if(getExtension(list.get(i).file_name).equalsIgnoreCase(".mov")){
          holder.icon_play.setVisibility(View.VISIBLE);
      }

        Log.e("extension",""+getExtension(list.get(i).file_name));

      holder.itemClick.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if(getExtension(list.get(i).file_name).equalsIgnoreCase(".mov")){
                  context.startActivity(PlayerActivity.getVideoPlayerIntent(context,
                          list.get(i).file_name,
                          ""));
              }else{

                  if(clickItem!=null){
                      clickItem.click(i,"image");
                  }


              }

          }
      });

      holder.edit_img.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if(clickItem!=null){
                  clickItem.click(i,"edit");
              }
          }
      });
    }

    private String getExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
         SquareLayout itemClick;
         ImageView img,icon_play,edit_img;
         TextView txt_read_more,txt_date,txt_des;
      public Holder(@NonNull View itemView) {
          super(itemView);
          ButterKnife.bind(this,itemView);
          itemClick = itemView.findViewById(R.id.itemClick);
          img = itemView.findViewById(R.id.img);
          icon_play = itemView.findViewById(R.id.icon_play);
          edit_img = itemView.findViewById(R.id.edit_img);

      }
  }
}

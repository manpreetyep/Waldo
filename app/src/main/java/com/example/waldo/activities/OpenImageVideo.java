package com.example.waldo.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.waldo.R;

public class OpenImageVideo extends AppCompatActivity {
    ImageView cancel_img,show_image;
    String image_link="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image_video);

        cancel_img = findViewById(R.id.cancel_img);
        show_image = findViewById(R.id.show_image);
        image_link = getIntent().getStringExtra("image_link");
        Glide.with(OpenImageVideo.this)
                                    .load(image_link)
                                    .thumbnail(0.5f)
                                    .apply(new RequestOptions().placeholder(R.drawable.dummy)
                                    .error(R.drawable.dummy))
                                    .into(show_image);

        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}

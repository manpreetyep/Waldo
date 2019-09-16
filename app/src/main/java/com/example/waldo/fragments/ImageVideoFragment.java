package com.example.waldo.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waldo.R;
import com.example.waldo.Rest.ApiInterFace;
import com.example.waldo.Rest.Rest;
import com.example.waldo.Utils.SessionManager;
import com.example.waldo.activities.OpenImageVideo;
import com.example.waldo.adapter.ImagesVideoAdapter;
import com.example.waldo.models.ImageVideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ImageVideoFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tittle)
    TextView toolbar_tittle;

    @BindView(R.id.back_button)
    TextView back_button;

    @BindView(R.id.list_rv)
    RecyclerView list_rv;

    @BindView(R.id.camera_layout)
    LinearLayout camera_layout;

    @BindView(R.id.video_layout)
    LinearLayout video_layout;

    GridLayoutManager manager;
    private Unbinder unbinder;
    ImagesVideoAdapter adapter;
    SessionManager sessionManager;
    String type="",propertyId="";
    ProgressDialog progressDialog;
    ArrayList<ImageVideoModel> list = new ArrayList<>();

    MultipartBody.Part fileToUpload;
    File file;
    String selectedFilePath;
    Bitmap bitmap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.image_video_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getActivity());
        init();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(){
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if(sessionManager.getCategoryName().equalsIgnoreCase("complete")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_green));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.green));
            type="property";
            toolbar_tittle.setText(sessionManager.getPropertyAddress());
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("pending")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.title_light_blue));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
            type="property";
            toolbar_tittle.setText(sessionManager.getPropertyAddress());
        }
        else if(sessionManager.getCategoryName().equalsIgnoreCase("working")){
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.light_title_bar));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.title_bar));
            type="property";
            toolbar_tittle.setText(sessionManager.getPropertyAddress());
        }else{
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.dark_red));
            toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.dark_red));
            toolbar_tittle.setText("Images / Video");
            type="profile";
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);

        adapter = new ImagesVideoAdapter(getActivity(),list);
        manager = new GridLayoutManager(getActivity(),3);
        list_rv.setLayoutManager(manager);
        list_rv.setAdapter(adapter);
        GetImagesVideos(type);
        listerner();
    }

    private void listerner() {
        adapter.clickListerner(new ImagesVideoAdapter.ClickItem() {
            @Override
            public void click(int pos,String type) {
                if(type.equalsIgnoreCase("edit")){
                    openDialog();
                }else{
                    startActivity(new Intent(getActivity(), OpenImageVideo.class).putExtra("image_link",list.get(pos).file_name));
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            }
        });
    }

    private void openDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.edit_dialog_img_video);
        ImageView cross = dialog.findViewById(R.id.cancel_img);
        EditText ed_des = dialog.findViewById(R.id.ed_des);
        TextView txt_save = dialog.findViewById(R.id.txt_save);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_des.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter something", Toast.LENGTH_SHORT).show();
                }else{
                    EditDescription(type,ed_des.getText().toString());
                }

            }
        });
        dialog.show();
    }

    @OnClick({R.id.camera_layout,R.id.video_layout})
    void onClick(View v){
        if(v==camera_layout){

            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 10);

        }else if(v==video_layout){

            Intent intent = new Intent("android.media.action.VIDEO_CAMERA");
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 11 );

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void GetImagesVideos(String type) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", sessionManager.getId());
        map.put("user_type", sessionManager.getUserType());

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        if(type.equalsIgnoreCase("profile"))
            call = apiInterFace.getProfileImages(sessionManager.getId(),sessionManager.getUserType());
        else{
            ///RequestBody body = RequestBody.create(MediaType.parse("application/json"), new JSONObject(map).toString());
            call = apiInterFace.getImages(Rest.BASE_URL+"Api/getImages/"+sessionManager.getPropertyId());
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = "";

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (response.body() == null) {

                    Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        json = response.body().string();
                        Log.e("Images video..",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {
                        list.clear();
                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int j=0;j<data.length();j++){
                                JSONObject object = data.getJSONObject(j);
                                ImageVideoModel model = new ImageVideoModel();
                                model.file_name =object.getString("file_name");
                                model.thumbnail =object.getString("thumbnail");
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.e("exception..",e.toString());

                        if (progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }



    public void EditDescription(String type,String descriptiion) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> map = new HashMap<>();
        map.put("propertyId", "");
        map.put("id", "");
        map.put("description", descriptiion);

        Log.e("descriptiion","descriptiion"+descriptiion);
        Log.e("descriptiion","id  "+sessionManager.getId());
        Log.e("descriptiion","sessionManager.getUserType()  "+sessionManager.getUserType());

        ApiInterFace apiInterFace = Rest.getClient().create(ApiInterFace.class);
        Call<ResponseBody> call= null;
        if(type.equalsIgnoreCase("profile"))
            call = apiInterFace.editProfileImages(sessionManager.getId(),sessionManager.getUserType(),descriptiion);
        else{
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), new JSONObject(map).toString());
            call = apiInterFace.editDescription(body);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = "";

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (response.body() == null) {

                    Toast.makeText(getActivity(), "No response from server", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        json = response.body().string();
                        Log.e("add description..",json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("exception...",e.toString());
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(json);
                        String status = jsonObject.getString("status");

                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.e("exception..",e.toString());

                        if (progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();

    }
    public static int CAMERA_REQUEST = 123;
    public static int MY_CAMERA_PERMISSION_CODE = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (requestCode == 10 && resultCode == RESULT_OK && data !=null ) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    getPath(getImageUri(photo));
                    Log.e("paht",""+data.getExtras().get("data"));
                    //uploadImagesVideo(getPath(uri));
                }
                break;
        }
    }
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        Log.e("sdffd",""+path);
        return Uri.parse(path);
    }
    private String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        selectedFilePath = cursor.getString(column_index);
        String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
        Log.e("Selected Image Path", selectedFilePath);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        bitmap = BitmapFactory.decodeFile(selectedFilePath, options);

        //select_image.setImageBitmap(bitmap);

        Log.e("result", String.valueOf(bitmap));
        uploadImagesVideo(selectedFilePath);
        return selectedFilePath;
    }


    public void uploadImagesVideo(String path){
        progressDialog.show();
        file =  new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);
        fileToUpload = MultipartBody.Part.createFormData("file",file.getName() , requestBody);
        ApiInterFace interFace = Rest.getClient().create(ApiInterFace.class);

        RequestBody u_id = RequestBody.create(MediaType.parse("text/plain"),sessionManager.getId());
        RequestBody u_type =RequestBody.create(MediaType.parse("text/plain"),sessionManager.getUserType());
        RequestBody des =RequestBody.create(MediaType.parse("text/plain"),"");

        Call<ResponseBody> call = interFace.profileImageupload(fileToUpload,u_id,u_type,des);

        call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body()==null){
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                        if (progressDialog!=null && progressDialog.isShowing())
                            progressDialog.dismiss();

                    }else {
                        try {
                            if (progressDialog!=null && progressDialog.isShowing())
                                progressDialog.dismiss();

                            String json = response.body().string();
                            JSONObject object = new JSONObject(json);
                            Log.e("uploaded","Image  response "+object);


                        } catch (IOException e) {
                            e.printStackTrace();
                            if (progressDialog!=null && progressDialog.isShowing())
                                progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (progressDialog!=null && progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (progressDialog!=null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
    }



    public void uploadImagesFromDetail(String path){
        progressDialog.show();
        file =  new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"),file);
        fileToUpload = MultipartBody.Part.createFormData("file",file.getName() , requestBody);
        ApiInterFace interFace = Rest.getClient().create(ApiInterFace.class);

        RequestBody propertyId = RequestBody.create(MediaType.parse("text/plain"),"");
        RequestBody des =RequestBody.create(MediaType.parse("text/plain"),"");

        Call<ResponseBody> call = interFace.imageUploadFromDetail(fileToUpload,propertyId,des);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.body()==null){
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    if (progressDialog!=null && progressDialog.isShowing())
                        progressDialog.dismiss();

                }else {
                    try {
                        if (progressDialog!=null && progressDialog.isShowing())
                            progressDialog.dismiss();

                        String json = response.body().string();
                        JSONObject object = new JSONObject(json);
                        Log.e("uploaded","Image  response "+object);


                    } catch (IOException e) {
                        e.printStackTrace();
                        if (progressDialog!=null && progressDialog.isShowing())
                            progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (progressDialog!=null && progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (progressDialog!=null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

}


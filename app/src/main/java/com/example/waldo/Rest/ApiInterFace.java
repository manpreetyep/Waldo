package com.example.waldo.Rest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiInterFace {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("email") String email,
                                @Field("password") String password);


    @FormUrlEncoded
    @POST("dashboard")
    Call<ResponseBody> dashboard(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/getprofile")
    Call<ResponseBody> getprofile(@Field("user_id") String user_id,
                                @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("Api/properties")
    Call<ResponseBody> propertiesAdmin(@Field("user_id") String user_id,
                                @Field("status") String status);

    @FormUrlEncoded
    @POST("Api/getEmployeeProperties")
    Call<ResponseBody> getpropertiesEmployee(@Field("user_id") String user_id,
                                       @Field("status") String status);

    @FormUrlEncoded
    @POST("api/getProfileNotes")
    Call<ResponseBody> getProfileNotes(@Field("user_id") String user_id,
                                  @Field("user_type") String user_type);



    @FormUrlEncoded
    @POST("api/addProfileNote")
    Call<ResponseBody> addProfileNote(@Field("user_id") String user_id,
                                     @Field("user_type") String user_type ,
                                      @Field("note_description") String note_description);

    @FormUrlEncoded
    @POST("Api/addNote")
    Call<ResponseBody> addNote(@Field("user_id") String user_id,
                               @Field("propID") String propID,
                               @Field("note_description") String note_description);


    @GET
    Call<ResponseBody> getNotes(@Url String userUrl);

    @FormUrlEncoded
    @POST("api/getProfileImages")
    Call<ResponseBody> getProfileImages(@Field("user_id") String user_id,
                                        @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("api/getProfileDocuments")
    Call<ResponseBody> getProfileDocuments(@Field("user_id") String user_id,
                                        @Field("user_type") String user_type);

//    @FormUrlEncoded
//    @POST("Api/getproperty")
//    Call<ResponseBody> getpropertyDetail(@Field("property_id") String property_id
//                                        );

    @FormUrlEncoded
    @POST("api/editProfileImages")
    Call<ResponseBody> editProfileImages(@Field("user_id") String user_id,
                                        @Field("user_type") String user_type,
                                         @Field("description") String description
                                         );

    @FormUrlEncoded
    @POST("api/startlog")
    Call<ResponseBody> startlog(@Field("user_id") String user_id,
                                        @Field("propID") String propID,
                                         @Field("start_date") String start_date
                                         );

    @FormUrlEncoded
    @POST("api/endlog")
    Call<ResponseBody> endlog(@Field("user_id") String user_id,
                                @Field("propID") String propID,
                                @Field("end_date") String end_date,
                                @Field("log_id") String log_id
    );

    @Multipart
    @POST("Api/profileImageupload")
    Call<ResponseBody> profileImageupload(@Part MultipartBody.Part image,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("user_type") RequestBody user_type,
                                          @Part("description") RequestBody description
                                         );

    @Multipart
    @POST("Api/imageUpload")
    Call<ResponseBody> imageUploadFromDetail(@Part MultipartBody.Part image,
                                          @Part("propID") RequestBody user_id,
                                          @Part("description") RequestBody description
                                         );

    @Multipart
    @POST("Api/videoUpload")
    Call<ResponseBody> videoUploadFromDetail(@Part MultipartBody.Part image,
                                             @Part("propID") RequestBody user_id,
                                             @Part("description") RequestBody description);

    @FormUrlEncoded
    @POST("Api/profileVideoupload")
    Call<ResponseBody> profileVideoupload(@Part MultipartBody.Part video,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("user_type") RequestBody user_type,
                                          @Part("description") RequestBody description);

    @POST("Api/Api/editDescription")
    Call<ResponseBody> editDescription(@Body RequestBody jsonObject);

    @POST("api/addNewProperty")
    Call<ResponseBody> addNewProperty(@Body RequestBody jsonObject);

    @GET
    Call<ResponseBody> editDescription(@Url String userUrl);

    @GET
    Call<ResponseBody> getpropertyDetail(@Url String userUrl);

    @GET
    Call<ResponseBody> getPropertyData(@Url String userUrl);


    @GET
    Call<ResponseBody> getImages(@Url String userUrl);

}

package com.atguigu.springcloud.other.http.retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface ComfyuiApi {

    @GET("/system_stats")
    Call<Map<String, Object>> getSystemStats();

    @GET("group/{id}/users")
    Call<List<Demo>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("users/new")
    Call<Demo> createUser(@Body Demo user);

    @Multipart
    @PUT("user/photo")
    Call<Demo> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);

    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("users/{username}")
    Call<Demo> getUser(@Path("username") String username);

    @GET("user")
    Call<Demo> getUserOne(@Header("Authorization") String authorization);

    @GET("user")
    Call<Demo> getUserTwo(@HeaderMap Map<String, String> headers);

}

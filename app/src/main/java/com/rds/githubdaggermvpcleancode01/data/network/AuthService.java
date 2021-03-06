package com.rds.githubdaggermvpcleancode01.data.network;

import android.content.Context;

import com.rds.githubdaggermvpcleancode01.BuildConfig;
import com.rds.githubdaggermvpcleancode01.data.network.model.LoginResponse;
import com.rds.githubdaggermvpcleancode01.data.network.model.RegisterResponse;

import java.io.IOException;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/user/login")
    @FormUrlEncoded
    Observable<LoginResponse> goLogin(@Field("email") String email, @Field("password") String password);

    @POST("/user/register")
    @FormUrlEncoded
    Observable<RegisterResponse> doRegister(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    class Factory {
        public static AuthService makeLoginService(Context context) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            Request request = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                                    .build();

                            Response response = chain.proceed(request);
                            response.cacheResponse();
                            // Customize or return the response
                            return response;

                        }
                    })

                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.LOGIN_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(AuthService.class);
        }
    }

}

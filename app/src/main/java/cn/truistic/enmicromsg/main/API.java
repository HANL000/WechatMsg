package cn.truistic.enmicromsg.main;


import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.truistic.enmicromsg.common.StringConverter.StringConverterFactory;
import cn.truistic.enmicromsg.info.ResultBean;
import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public class API {
    private static final String BASE_URL = "http://61.144.121.138:8090/UpData/";
    private static RetrofitAPI retrofitAPI;
    private static OkHttpClient okHttpClient;

    static {
        okHttpClient =
                ProgressManager.getInstance().with(new OkHttpClient().newBuilder())
                .connectTimeout(300, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(300, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(300, TimeUnit.SECONDS)//设置写入超时时间
                .retryOnConnectionFailure(true)

                .build();
    }

    public static RetrofitAPI Retrofit() {
        if (retrofitAPI == null) {
            retrofitAPI = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(RetrofitAPI.class);
        }
        return retrofitAPI;
    }

    public interface RetrofitAPI {

        //图片上传
        @Multipart
        @POST("ReceiveImg.aspx")
        Call<ResultBean.DataBean.FileUploadBean> updateImage(@PartMap Map<String, RequestBody> params);


    }


}
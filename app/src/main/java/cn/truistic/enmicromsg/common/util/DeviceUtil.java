package cn.truistic.enmicromsg.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 设备相关工具类
 */
public class DeviceUtil {
    private static String phoneimei;

    /**
     * 获取设备ID(the IMEI for GSM and the MEID or ESN for CDMA phones)
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneimei =tm.getDeviceId();
        return phoneimei;
    }

    /**
     * 检测应用是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
     *
     * @param createTime 消息创建时间
     * @return
     */

    public static String formatTime(String createTime) {
        // 将微信传入的CreateTime转换成long类型
        Long msgCreateTime = Long.parseLong(createTime);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date(msgCreateTime));
    }

    public static void postJson(String url, String s) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        RequestBody body=RequestBody.create(JSON,s);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("DDDBBB----后台服务", "onFailure: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("DDDBBB----后台服务", "onResponse: " + response.body().string());
                }
            }

        });
    }


}

package cn.truistic.enmicromsg.common.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.truistic.enmicromsg.R;
import okhttp3.Call;
import okhttp3.Headers;



/**
 * 设备相关工具类
 */
public class DeviceUtil {
    private final static String TAG = "DDDBBB-----后台服务";
    private static String phoneimei;
    private static String temp;
    public static final String KEY_APP_KEY = "WXJ_APPKEY";

    /**
     * 获取设备ID(the IMEI for GSM and the MEID or ESN for CDMA phones)
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneimei = tm.getDeviceId();
        return phoneimei; //864446031135841
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


    /**
     * 上传json字符串
     *
     */

    public static void postJson(String url, String s, final Context context) {
        OkGo.<String>post(url)
                    .upJson(s)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            handleResponse(response,context);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            handleError(response,context);
                    }
                });
    }


    /**
     * 请求成功的处理
     */

    private static <T> void handleResponse(Response<T> response, Context context) {

        StringBuilder sb;
        Call call = response.getRawCall();
        if (call != null) {
            //Log.i(TAG, "请求成功  请求方式：" + call.request().method());
            String url = String.valueOf(call.request().url());
            temp = url.substring(url.indexOf("Receive"), url.lastIndexOf(".as"));
        } else {
            Log.i(TAG, "************CALL = NULL*************");
        }
        T body = response.body();
        if (body == null) {
            Log.i(TAG, "***********返回结果 = null**************");
        } else {
            if (body instanceof String) {
                Log.i(TAG, temp+"=====返回结果===="+body.toString()+"=====");
                if (temp.equals("ReceiveMessage")){
                    SharedPerfUtil.setParam(context,"response",body);
                    Log.i(TAG,body+"被保存了");
                }
            } else if (body instanceof Bitmap) {
                Log.i(TAG, "图片的内容即为数据");
            } else {
                Log.i(TAG, "body==="+body.toString());
            }
        }

    }

    /**
     *请求失败的处理
     *
     */

    private static <T> void handleError(Response<T> response, Context context) {
        if (response == null) return;
        if (response.getException() != null) response.getException().printStackTrace();
        StringBuilder sb;
        Call call = response.getRawCall();
        if (call != null) {
            Log.i(TAG,"请求失败  请求方式：" + call.request().method());
            Headers requestHeadersString = call.request().headers();
            Set<String> requestNames = requestHeadersString.names();
            sb = new StringBuilder();
            for (String name : requestNames) {
                sb.append(name).append(" ：").append(requestHeadersString.get(name)).append("\n");
            }

            if (!response.body().toString().isEmpty()){
                SharedPerfUtil.setParam(context,"response",response.body().toString());
            }
            Log.i(TAG,"Error :\n"+sb.toString());

        } else {
            Log.i(TAG,"*************Error***************");
        }


    }


    public static SQLiteDatabaseHook gethock(){
        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        return hook;
    }

    /**
     * 判断本应用是否存活
     * 如果需要判断本应用是否在后台还是前台用getRunningTask
     * */
    public static boolean isAPPALive(Context mContext, String packageName){
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        // 遍历，进程名即包名
        for(ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList){
            if(packageName.equals(appInfo.processName)){
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }



    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getVerName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }
}

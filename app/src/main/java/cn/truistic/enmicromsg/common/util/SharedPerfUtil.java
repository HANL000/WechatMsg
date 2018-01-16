package cn.truistic.enmicromsg.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.main.MainMVP;

import static android.content.ContentValues.TAG;
import static android.util.Log.d;

/**
 * SharedPerferences工具类
 */
public class SharedPerfUtil {
    private static final String FILE_NAME = "wechat_file";


    // 是否第一次启动应用
    public static void saveIsFirstStart(Context context, boolean isFirstStart) {
        SharedPreferences.Editor editor = getAppSharedPerf(context).edit();
        editor.putBoolean(context.getString(R.string.saved_app_is_first_start), isFirstStart);
        editor.commit();
    }

    // 是否第一次启动应用
    public static boolean getIsFirstStart(Context context) {
        return getAppSharedPerf(context).getBoolean(context.getString(R.string.saved_app_is_first_start), true);
    }

    // 获取状态--------------IHomeView
    public static int getState(Context context, MainMVP.IHomeView.Progress progress) {
        SharedPreferences sp = getAppSharedPerf(context);
        return sp.getInt(progress.name(), 0);
    }

    // 保存状态-------------IHomeView
    public static void saveProgressState(Context context, MainMVP.IHomeView.Progress progress, int state) {
        SharedPreferences.Editor editor = getAppSharedPerf(context).edit();
        editor.putInt(progress.name(), state);
        editor.commit();
    }

    // 获取状态--------------IHomeView
    public static int getState(Context context, MainMVP.IExportView.Progress progress) {
        SharedPreferences sp = getAppSharedPerf(context);
        return sp.getInt(progress.name(), 0);
    }

    // 保存状态-------------IHomeView
    public static void saveProgressState(Context context, MainMVP.IExportView.Progress progress, int state) {
        SharedPreferences.Editor editor = getAppSharedPerf(context).edit();
        editor.putInt(progress.name(), state);
        editor.commit();
    }

    // 获取微信数据库（账号）数量
    public static int getDbNum(Context context) {
        return getDataSharedPerf(context).getInt(context.getString(R.string.saved_db_num), 0);
    }

    // 保存微信数据库（账号）数量
    public static void saveDbNum(Context context, int num) {
        SharedPreferences.Editor editor = getDataSharedPerf(context).edit();
        editor.putInt(context.getString(R.string.saved_db_num), num);
        editor.commit();
    }

    // 获取数据库密码
    public static String getDbPwd(Context context) {
        return getDataSharedPerf(context).getString(context.getString(R.string.saved_db_pwd), null);
    }

    // 保存数据库密码
    public static void savedbPwd(Context context, String pwd) {
        SharedPreferences.Editor editor = getDataSharedPerf(context).edit();
        editor.putString(context.getString(R.string.saved_db_pwd), pwd);
        editor.commit();
    }

    // 获取uin
    public static int getUin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("system_config_prefs", Context.MODE_PRIVATE);
        return sp.getInt("default_uin", 0);
    }

    private static SharedPreferences getAppSharedPerf(Context context) {
        return context.getSharedPreferences(context.getString(R.string.shared_perf_app), Context.MODE_PRIVATE);
    }

    private static SharedPreferences getDataSharedPerf(Context context) {
        return context.getSharedPreferences(context.getString(R.string.shared_perf_data), Context.MODE_PRIVATE);
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        @SuppressWarnings("deprecation")
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        Log.d(TAG, "set方法：上下文对象:" + context + ",保存的数据名：" + key + ",保存的数据值："
                + object);

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        @SuppressWarnings("deprecation")
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_MULTI_PROCESS);

        Log.d(TAG, "get方法:上下文对象:" + context + ",获取的数据名：" + key + ",获取到的数据值："
                + defaultObject);

        if ("String".equals(type)) {
            Log.d(TAG, "key = " + key + "value = "
                    + (String) defaultObject);
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            Log.d(TAG, "key = " + key + "value = "
                    + (Integer) defaultObject);
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            Log.d(TAG, "key = " + key + "value = "
                    + (Boolean) defaultObject);
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            Log.d(TAG, "key = " + key + "value = "
                    + (Float) defaultObject);
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            Log.d(TAG, "key = " + key + "value = " + (Long) defaultObject);
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }


}

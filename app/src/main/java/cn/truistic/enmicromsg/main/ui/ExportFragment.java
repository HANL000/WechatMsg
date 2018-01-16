package cn.truistic.enmicromsg.main.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.common.db.DBUtil;
import cn.truistic.enmicromsg.common.util.PathUtils;
import cn.truistic.enmicromsg.main.MainMVP;
import cn.truistic.enmicromsg.main.presenter.ExportPresenter;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;


/**
 * 导出聊天记录-----图片
 */
public class ExportFragment extends Fragment implements MainMVP.IExportView {

    private ImageView iv_img;
    private TextView tv_state_img,tv_downloadSize,tv_netSpeed,tv_Progress;
    private Button fragment_btn_detect;
    private RotateAnimation animation;
    private MainMVP.IExportPresenter exportPresenter;
    private NumberProgressBar mProgressBar;
    private String UPLOAD_URL = "http://61.144.121.138:8090/UpData/ReceiveImg.aspx";


    private ProgressInfo mLastUploadingingInfo;
    private Handler mHandler;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export, container, false);
        exportPresenter = new ExportPresenter(getActivity(),this);
        PathUtils.initImage(getContext());
        DBUtil.getWechatFile(getContext());
        mHandler = new Handler();
        initView(view);
        initListen();
        return view;

    }

    private void initListen() {
        //Okhttp/Retofit 上传监听
        ProgressManager.getInstance().addRequestLisenter(UPLOAD_URL, getUploadListener());
    }


    private void initView(final View view) {
        iv_img = (ImageView)view.findViewById(R.id.export_iv_img);
        tv_state_img = (TextView)view.findViewById(R.id.export_tv_state_img);
        tv_downloadSize = (TextView)view.findViewById(R.id.downloadSize);
        tv_netSpeed = (TextView)view.findViewById(R.id.netSpeed);
        tv_Progress = (TextView)view.findViewById(R.id.tvProgress);
        mProgressBar = (NumberProgressBar)view.findViewById(R.id.pbProgress);
        fragment_btn_detect = (Button)view.findViewById(R.id.fragment_btn_detect);
        fragment_btn_detect.setOnClickListener(v -> {
            fragment_btn_detect.setText(R.string.home_detect_detecting);
            fragment_btn_detect.setClickable(false);
            exportPresenter.detect();

        });
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(600);

    }


    @Override
    public void onDetectStop() {
        fragment_btn_detect.setText(R.string.home_detect_start);
        fragment_btn_detect.setClickable(true);
    }

    @Override
    public void setProgressState(Progress progress, State state) {
        switch (progress) {
            case UPLOAD_IMG:
                setImgState(state);
                break;

        }
    }

    private void setImgState(State state) {
        setIcon(iv_img, state);
        switch (state) {
            case UNDETECTED:
                tv_state_img.setText(R.string.home_state_not_get);
                break;
            case TRUE:
                tv_state_img.setText(R.string.home_state_acquired);
                break;
            case FALSE:
                tv_state_img.setText(R.string.home_state_undetected);
                break;
            case DETECTING:
                tv_state_img.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setIcon(ImageView iv, State state) {
        switch (state) {
            case UNDETECTED:
                iv.clearAnimation();
                iv.setImageResource(R.drawable.ic_info_24dp);
                break;
            case DETECTING:
                iv.clearAnimation();
                iv.setAnimation(animation);
                iv.setImageResource(R.drawable.ic_sync_24dp);
                animation.startNow();
                break;
            case TRUE:
                iv.clearAnimation();
                iv.setImageResource(R.drawable.ic_check_24dp);
                break;
            case FALSE:
                iv.clearAnimation();
                iv.setImageResource(R.drawable.ic_error_24dp);
                break;
        }
    }

    //监听上传进度
    @NonNull
    private ProgressListener getUploadListener() {
        return new ProgressListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                // 如果你不屏蔽用户重复点击上传或下载按钮,就可能存在同一个 Url 地址,上一次的上传或下载操作都还没结束,
                // 又开始了新的上传或下载操作,那现在就需要用到 id(请求开始时的时间) 来区分正在执行的进度信息
                // 这里我就取最新的上传进度用来展示,顺便展示下 id 的用法

                if (mLastUploadingingInfo == null) {
                    mLastUploadingingInfo = progressInfo;
                }

                //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
                if (progressInfo.getId() < mLastUploadingingInfo.getId()) {
                    return;
                } else if (progressInfo.getId() > mLastUploadingingInfo.getId()) {
                    mLastUploadingingInfo = progressInfo;
                }


                int progress = mLastUploadingingInfo.getPercent();
                mProgressBar.setProgress(progress);
                tv_Progress.setText(progress + "%");
                String speed = Formatter.formatFileSize(getContext(), mLastUploadingingInfo.getSpeed());
                tv_netSpeed.setText(String.format("%s/s", speed));
                String downloadLength = Formatter.formatFileSize(getContext(), mLastUploadingingInfo.getCurrentbytes());
                String totalLength = Formatter.formatFileSize(getContext(), mLastUploadingingInfo.getContentLength());
                tv_downloadSize.setText(downloadLength + "/" + totalLength);


                fragment_btn_detect.setText(R.string.home_detect_detecting);
                fragment_btn_detect.setClickable(false);

                Log.d("DDDBBB", "--Upload-- " + progress);
                if (mLastUploadingingInfo.isFinish()) {
                    //说明已经上传完成
                    fragment_btn_detect.setText(R.string.home_detect_start);
                    fragment_btn_detect.setClickable(true);

                }

            }

            @Override
            public void onError(long id, Exception e) {
                mHandler.post(() -> {
                    mProgressBar.setProgress(0);
                    tv_Progress.setText("error");
                });
            }

        };
    }
}

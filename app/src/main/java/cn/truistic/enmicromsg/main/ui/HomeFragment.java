package cn.truistic.enmicromsg.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.truistic.enmicromsg.R;
import cn.truistic.enmicromsg.main.MainMVP;
import cn.truistic.enmicromsg.main.presenter.HomePresenter;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements MainMVP.IHomeView {

    private ImageView iv_wechat, iv_root, iv_permission, iv_data_get, iv_data_analysis,
    iv_data_content,iv_data_user,iv_service,iv_data_upload;
    private TextView tv_state_wechat, tv_state_root, tv_state_permission, tv_state_data_get,
    tv_state_data_analysis,tv_state_data_content,tv_state_data_user,tv_state_service,
    tv_state_data_upload;
    private Button btn_detect;
    private RotateAnimation animation;
    private MainMVP.IHomePresenter homePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homePresenter = new HomePresenter(getActivity(), this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_wechat = (ImageView) view.findViewById(R.id.home_iv_wechat);
        iv_root = (ImageView) view.findViewById(R.id.home_iv_root);
        iv_service = (ImageView)view.findViewById(R.id.home_iv_service);
        iv_permission = (ImageView) view.findViewById(R.id.home_iv_permission);
        iv_data_get = (ImageView) view.findViewById(R.id.home_iv_data_get);
        iv_data_analysis = (ImageView) view.findViewById(R.id.home_iv_data_analysia);
        iv_data_content = (ImageView) view.findViewById(R.id.home_iv_data_content);
        iv_data_user = (ImageView)view.findViewById(R.id.home_iv_data_user);
        iv_data_upload = (ImageView)view.findViewById(R.id.home_iv_data_upload);
        tv_state_wechat = (TextView) view.findViewById(R.id.home_tv_state_wechat);
        tv_state_root = (TextView) view.findViewById(R.id.home_tv_state_root);
        tv_state_service = (TextView)view.findViewById(R.id.home_tv_state_service);
        tv_state_permission = (TextView) view.findViewById(R.id.home_tv_state_permission);
        tv_state_data_get = (TextView) view.findViewById(R.id.home_tv_state_data_get);
        tv_state_data_analysis = (TextView) view.findViewById(R.id.home_tv_state_data_analysis);
        tv_state_data_content = (TextView)view.findViewById(R.id.home_tv_state_data_content);
        tv_state_data_user = (TextView)view.findViewById(R.id.home_tv_state_data_user);
        tv_state_data_upload = (TextView)view.findViewById(R.id.home_tv_state_data_upload);

        btn_detect = (Button) view.findViewById(R.id.home_btn_detect);
        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_detect.setText(R.string.home_detect_detecting);
                btn_detect.setClickable(false);
                homePresenter.detect();
            }
        });
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(600);
    }

    @Override
    public void onDetectStop() {
        btn_detect.setText(R.string.home_detect_start);
        btn_detect.setClickable(true);
    }

    @Override
    public void setProgressState(Progress progress, State state) {
        switch (progress) {
            case DETECT_WECHAT:
                setWeChatState(state);
                break;
            case DETECT_ROOT:
                setRootState(state);
                break;
            case DETECT_PERMISSION:
                setPermissionState(state);
                break;
            case REQUEST_DATA:
                setRequestDataState(state);
                break;
            case ANALYSIS_DATA:
                setAnalysisDataState(state);
                break;
            case ANALYSIS_CONNTENT:
                setAnalysisContentState(state);
                break;
            case ANALYSIS_USER:
                setAnalysisUserState(state);
                break;
            case DETECT_SERVICE:
                setServiceState(state);
                break;
            case UPLOAD_DATA:
                setUploadDataState(state);
                break;
        }
    }


    private void setWeChatState(State state) {
        setIcon(iv_wechat, state);
        switch (state) {
            case UNDETECTED:
                tv_state_wechat.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_wechat.setText(R.string.home_state_installed);
                break;
            case FALSE:
                tv_state_wechat.setText(R.string.home_state_uninstalled);
                break;
            case DETECTING:
                tv_state_wechat.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setRootState(State state) {
        setIcon(iv_root, state);
        switch (state) {
            case UNDETECTED:
                tv_state_root.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_root.setText(R.string.home_state_rooted);
                break;
            case FALSE:
                tv_state_root.setText(R.string.home_state_unrooted);
                break;
            case DETECTING:
                tv_state_root.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setPermissionState(State state) {
        setIcon(iv_permission, state);
        switch (state) {
            case UNDETECTED:
                tv_state_permission.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_permission.setText(R.string.home_state_authorized);
                break;
            case FALSE:
                tv_state_permission.setText(R.string.home_state_unauthorized);
                break;
            case DETECTING:
                tv_state_permission.setText(R.string.home_state_detecting);
                break;
        }
    }
    private void setServiceState(State state) {
        setIcon(iv_service, state);
        switch (state) {
            case UNDETECTED:
                tv_state_service.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_service.setText("已开启");
                break;
            case FALSE:
                tv_state_service.setText("未开启");
                break;
            case DETECTING:
                tv_state_service.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setUploadDataState(State state) {
        setIcon(iv_data_upload, state);
        switch (state) {
            case UNDETECTED:
                tv_state_data_upload.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_data_upload.setText(R.string.home_state_upload);//已上传
                break;
            case FALSE:
                tv_state_data_upload.setText(R.string.home_state_not_up);//未上传
                break;
            case DETECTING:
                tv_state_data_upload.setText("上传中");
                break;
        }
    }

    private void setRequestDataState(State state) {
        setIcon(iv_data_get, state);
        switch (state) {
            case UNDETECTED:
                tv_state_data_get.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_data_get.setText(R.string.home_state_acquired);
                break;
            case FALSE:
                tv_state_data_get.setText(R.string.home_state_not_get);
                break;
            case DETECTING:
                tv_state_data_get.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setAnalysisDataState(State state) {
        setIcon(iv_data_content, state);
        switch (state) {
            case UNDETECTED:
                tv_state_data_content.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_data_content.setText(R.string.home_state_acquired);
                break;
            case FALSE:
                tv_state_data_content.setText(R.string.home_state_not_get);
                break;
            case DETECTING:
                tv_state_data_content.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setAnalysisContentState(State state) {
        setIcon(iv_data_analysis, state);
        switch (state) {
            case UNDETECTED:
                tv_state_data_analysis.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_data_analysis.setText(R.string.home_state_acquired);
                break;
            case FALSE:
                tv_state_data_analysis.setText(R.string.home_state_not_get);
                break;
            case DETECTING:
                tv_state_data_analysis.setText(R.string.home_state_detecting);
                break;
        }
    }

    private void setAnalysisUserState(State state) {
        setIcon(iv_data_user, state);
        switch (state) {
            case UNDETECTED:
                tv_state_data_user.setText(R.string.home_state_undetected);
                break;
            case TRUE:
                tv_state_data_user.setText(R.string.home_state_acquired);
                break;
            case FALSE:
                tv_state_data_user.setText(R.string.home_state_not_get);
                break;
            case DETECTING:
                tv_state_data_user.setText(R.string.home_state_detecting);
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


}
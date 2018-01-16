package cn.truistic.enmicromsg.main.model;

import android.content.Context;

import cn.truistic.enmicromsg.common.util.SharedPerfUtil;
import cn.truistic.enmicromsg.main.MainMVP;

/**
 * Created by Administrator on 2017/7/24.
 */

public class ExportModel implements MainMVP.IExportModel {

    private Context context;
    private MainMVP.IExportPresenter exportPresenter;

    public ExportModel(MainMVP.IExportPresenter exportPresenter, Context context) {
        this.exportPresenter = exportPresenter;
        this.context = context;
    }

    @Override
    public MainMVP.IExportView.State getState(MainMVP.IExportView.Progress progress) {
        switch (SharedPerfUtil.getState(context, progress)) {
            case 0:
                return MainMVP.IExportView.State.UNDETECTED;
            case 1:
                return MainMVP.IExportView.State.DETECTING;
            case 2:
                return MainMVP.IExportView.State.TRUE;
            case 3:
                return MainMVP.IExportView.State.FALSE;
        }
        return MainMVP.IExportView.State.UNDETECTED;

    }

    @Override
    public void saveState(MainMVP.IExportView.Progress progress, MainMVP.IExportView.State state) {
        int stateInt = 0;
        switch (state) {
            case UNDETECTED:
                stateInt = 0;
                break;
            case DETECTING:
                stateInt = 1;
                break;
            case TRUE:
                stateInt = 2;
                break;
            case FALSE:
                stateInt = 3;
                break;
        }
        SharedPerfUtil.saveProgressState(context, progress, stateInt);
    }

    @Override
    public int getDbNum() {
        return SharedPerfUtil.getDbNum(context);
    }

    @Override
    public void saveDbNum(int num) {
        SharedPerfUtil.saveDbNum(context, num);
    }

    @Override
    public String getDbPwd() {
        return SharedPerfUtil.getDbPwd(context);
    }

    @Override
    public void saveDbPwd(String pwd) {
        SharedPerfUtil.savedbPwd(context, pwd);
    }
}

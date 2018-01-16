package cn.truistic.enmicromsg.main;


/**
 * MainMVP接口
 */
public interface MainMVP {

    interface IMainView {

    }

    interface IHomeView {

        enum Progress {DETECT_WECHAT, DETECT_ROOT, DETECT_PERMISSION, REQUEST_DATA,DETECT_INTERNET,
            ANALYSIS_DATA,ANALYSIS_CONNTENT,ANALYSIS_USER,DETECT_SERVICE,UPLOADIMAGE_DATA}

        enum State {UNDETECTED, DETECTING, TRUE, FALSE}

        void onDetectStop();

        void setProgressState(Progress progress, State state);
    }

    interface IExportView {
        enum Progress {UPLOAD_IMG}

        enum State {UNDETECTED, DETECTING, TRUE, FALSE}

        void onDetectStop();

        void setProgressState(Progress progress, State state);

       // void setProgress(int progress);
    }

    interface IGroupsView {

    }

    interface IMainPresenter {

    }

    interface IHomePresenter {
        void detect();
    }

    interface IGroupsPresenter {

    }

    interface IExportPresenter {
        void detect();

    }

    interface IHomeModel {
        IHomeView.State getState(IHomeView.Progress progress);

        void saveState(IHomeView.Progress progress, IHomeView.State state);

        int getDbNum();

        void saveDbNum(int num);

        String getDbPwd();

        void saveDbPwd(String pwd);
    }

    interface IExportModel {

        IExportView.State getState(IExportView.Progress progress);

        void saveState(IExportView.Progress progress, IExportView.State state);

        int getDbNum();

        void saveDbNum(int num);

        String getDbPwd();

        void saveDbPwd(String pwd);
    }

    interface ChangeText{
        void onChangeText(String s);
    }


}
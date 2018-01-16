package cn.truistic.enmicromsg.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by 韩领
 */
public class PathUtils {

    private final static String TAG = "DDDBBB---PathUtils";
    private static List<String> voice_wechat_paths;
    private static List<String> image_wechat_paths;
    private static String export_dir;
    private static List<Long> fileLastModifieds;

    public static void initVoice(Context context) {
        voice_wechat_paths = new ArrayList<>();

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            File f = new File(dir + "/tencent/MicroMsg");

            if (f.exists() && f.canRead() && f.isDirectory()) {
                File[] files = f.listFiles();
                if (files == null || files.length == 0) {
                    return;
                }

                for (File f0 : files) {
                    if (f0.isDirectory() && f0.getName().length() > 24) {
                        voice_wechat_paths.add(f0.getAbsolutePath() + "/voice2");
                    }
                }
            }

//            File exportDir = new File(dir, "Wechat_mp3");
//            if (!exportDir.exists()) {
//                exportDir.mkdirs();
//            }
//
//            export_dir = exportDir.getAbsolutePath();
        }
    }

    public static void initImage(Context context) {
        image_wechat_paths = new ArrayList<>();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = Environment.getExternalStorageDirectory();
            File f = new File(dir + "/tencent/MicroMsg");

            if (f.exists() && f.canRead() && f.isDirectory()) {
                File[] files = f.listFiles();
                if (files == null || files.length == 0) {
                    return;
                }

                for (File f0 : files) {
                    if (f0.isDirectory() && f0.getName().length() > 24) {


                        image_wechat_paths.add(f0.getAbsolutePath() + "/image2");

                    }
                }

            }

            File exportDir = new File(dir, "Wechat_img");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            export_dir = exportDir.getAbsolutePath();
        }
    }

    private static List<String> getVoiceFiles_WeChat(Context context) {
        return voice_wechat_paths;
    }

    public static List<String> getImageFiles_WeChat(Context context) {
        return image_wechat_paths;
    }

    //根据获取微信最后修改时间戳     获取最后修改的图片文件夹
    private static List<String> getLastimgpath(Context context){
        List<String> lastimgpath = new ArrayList<>();
        for (int i = 0; i < image_wechat_paths.size(); i++) {
            if (getImagePathlasttime(context).equals(FileUtil.getFileLastModified
                    (image_wechat_paths.get(i)))){
                lastimgpath.add(image_wechat_paths.get(i));
            }
        }
        return lastimgpath;
    }

    //获取微信图片文件的最后修改时间戳
    private static Long getImagePathlasttime(Context context) {
        List<String> paths = PathUtils.image_wechat_paths;
        fileLastModifieds = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            fileLastModifieds.add(FileUtil.getFileLastModified(paths.get(i)));
        }
        Log.d(TAG,"最后时间戳"+Collections.max(fileLastModifieds));
        return Collections.max(fileLastModifieds);
    }

    public static String getExportDir() {
        return export_dir;
    }

    //获取微信语音文件夹路径
    public static List<String> getVoicePaths(Context context) {
        List<String> paths = PathUtils.getVoiceFiles_WeChat(context);
        ArrayList<String> mVoicePaths = new ArrayList<>();

        if (paths != null && paths.size() > 0) {
            File file;
            for (String path : paths) {
                if (path != null) {
                    file = new File(path);
                    if (file.exists() && file.isDirectory()) {
                        Stack<String> stack = new Stack<>();
                        stack.push(path);
                        while (!stack.empty()) {
                            File[] fs = null;
                            String parent = stack.pop();
                            if (parent != null) {
                                file = new File(parent);
                                if (file.isDirectory()) { // ignore file, FIXME
                                    fs = file.listFiles();
                                } else {
                                    continue;
                                }
                            }
                            if (fs == null || fs.length == 0) continue;
                            for (File f : fs) {
                                final String name = f.getName();
                                if (f.isDirectory() && !name.equals(".")
                                        && !name.equals("..")) {
                                    stack.push(f.getPath());
                                } else if (f.isFile()) {
                                    if (name.endsWith(".amr")) {
                                        mVoicePaths.add(f.getAbsolutePath());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Log.i(TAG, "voiceSize"+paths.size()+"****"+paths.toString());
        Log.i(TAG, "voiceSize"+mVoicePaths.size());
        return mVoicePaths;
    }

    //获取微信图片文件夹路径
    public static List<String> getImagePaths(Context context) {
        //List<String> paths = PathUtils.getImageFiles_WeChat(context);
        List<String> paths = PathUtils.getLastimgpath(context);
        ArrayList<String> mImagePaths = new ArrayList<>();

        if (paths != null && paths.size() > 0) {
            File file;
            for (String path : paths) {
                if (path != null) {
                    file = new File(path);
                    if (file.exists() && file.isDirectory()) {
                        Stack<String> stack = new Stack<>();
                        stack.push(path);
                        while (!stack.empty()) {
                            File[] fs = null;
                            String parent = stack.pop();
                            if (parent != null) {
                                file = new File(parent);
                                if (file.isDirectory()) { // ignore file, FIXME
                                    fs = file.listFiles();
                                } else {
                                    continue;
                                }
                            }
                            if (fs == null || fs.length == 0) continue;
                            for (File f : fs) {
                                final String name = f.getName();
                                if (f.isDirectory() && !name.equals(".") && !name.equals("..")) {
                                    stack.push(f.getPath());
                                } else if (f.isFile()) {
                                    if ( name.startsWith("th_") || name.endsWith(".jpg")) {
                                        mImagePaths.add(f.getAbsolutePath());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Log.i(TAG, "imageSize"+paths.size()+"****"+paths.toString());
        Log.i(TAG, "imageSize"+mImagePaths.size());
        return mImagePaths;
    }

    //1.获取配置文件，用于获取uin   // 2.获取数据库文件
    public static ArrayList<String> getUinAndDir(Context context){
        @SuppressLint("SdCardPath")
        String sharedPerfsPath = "/data/data/cn.truistic.enmicromsg/shared_prefs/system_config_prefs.xml";
        // 1.获取配置文件，用于获取uin
        RootUtil.execCmds(new String[]{"cp /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml "
                + sharedPerfsPath, "chmod 777 " + sharedPerfsPath});
        File sharedPerfsFile = new File(sharedPerfsPath);
        if (!sharedPerfsFile.exists()) {
            return null;
        }
        // 2.获取数据库文件
        ArrayList<String> list;
        list = RootUtil.execCmdsforResult(new String[]{"cd /data/data/com.tencent.mm/MicroMsg", "ls -R"});
        ArrayList<String> dirs = new ArrayList<>();
        String dir = null;
        String item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i);
            if (item.startsWith("./") && item.length() == 35) {
                dir = item;
            } else if (item.equals("EnMicroMsg.db")) {
                if (dir != null) {
                    dirs.add(dir.substring(2, 34));
                }
            }
        }
        if (dirs.size() != 0) {
            for (int i = 0; i < dirs.size(); i++) {
                RootUtil.execCmds(new String[]{"cp /data/data/com.tencent.mm/MicroMsg/" + dirs.get(i)
                        + "/EnMicroMsg.db " + context.getFilesDir() + "/EnMicroMsg" + i + ".db",
                        "chmod 777 " + context.getFilesDir() + "/EnMicroMsg" + i + ".db"});
            }
        }
        return dirs;
    }


    public class PathConfig {

        /**
         * 微信配置文件保存路径
         * @param context
         * @return
         */
        public  String getMMSharedPerfsPath(Context context) {
            return context.getFilesDir() + "/system_config_prefs.xml";
        }

    }

}

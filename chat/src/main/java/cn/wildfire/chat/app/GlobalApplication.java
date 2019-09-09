package cn.wildfire.chat.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.io.File;

import cn.wildfire.chat.app.third.location.viewholder.LocationMessageContentViewHolder;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.conversation.message.viewholder.MessageViewHolderManager;
import cn.wildfirechat.chat.BuildConfig;

/**
 * created by key  on 2019/8/23
 */
public class GlobalApplication extends Application {
    private WfcUIKit wfcUIKit;
    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static long mMainThreadId;//主线程id
    private static Handler mHandler;//主线程Handler

    @Override
    public void onCreate() {
        super.onCreate();

        //对全局属性赋值
        mContext = getApplicationContext();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
        if (getCurProcessName(this).equals("com.key.myapplication")) {
            wfcUIKit = new WfcUIKit();
            wfcUIKit.init(this);
            MessageViewHolderManager.getInstance().registerMessageViewHolder(LocationMessageContentViewHolder.class);
            setupWFCDirs();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        GlobalApplication.mContext = mContext;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    private void setupWFCDirs() {
        File file = new File(Config.VIDEO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Config.AUDIO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Config.FILE_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Config.PHOTO_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}

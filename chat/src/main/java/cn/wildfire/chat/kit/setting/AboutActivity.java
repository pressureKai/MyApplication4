package cn.wildfire.chat.kit.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcWebViewActivity;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.chat.R2;

public class AboutActivity extends WfcBaseActivity {

    @BindView(R2.id.infoTextView)
    TextView infoTextView;

    @Override
    protected int contentLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void afterViews() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            String info = packageInfo.packageName + "\n"
                    + packageInfo.versionCode + " " + packageInfo.versionName + "\n"
                    + Config.IM_SERVER_HOST + " " + Config.IM_SERVER_PORT + "\n"
                    + Config.APP_SERVER_HOST + " " + Config.APP_SERVER_PORT + "\n"
                    + Config.ICE_ADDRESS + " " + Config.ICE_USERNAME + " " + Config.ICE_PASSWORD + "\n";

            infoTextView.setText(info);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R2.id.introOptionItemView)
    public void intro() {
        WfcWebViewActivity.loadUrl(this, "野火IM功能介绍", "http://docs.wildfirechat.cn/");
    }

    @OnClick(R2.id.agreementOptionItemView)
    public void agreement() {
        WfcWebViewActivity.loadUrl(this, "野火IM用户协议", "http://www.wildfirechat.cn/firechat_user_agreement.html");
    }

    @OnClick(R2.id.privacyOptionItemView)
    public void privacy() {
        WfcWebViewActivity.loadUrl(this, "野火IM个人信息保护政策", "http://www.wildfirechat.cn/firechat_user_privacy.html");
    }
}

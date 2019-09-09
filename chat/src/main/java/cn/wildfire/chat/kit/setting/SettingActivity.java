package cn.wildfire.chat.kit.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import butterknife.OnClick;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.app.main.SplashActivity;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.chat.R2;

public class SettingActivity extends WfcBaseActivity {

    @Override
    protected int contentLayout() {
        return R.layout.setting_activity;
    }

    @OnClick(R2.id.exitOptionItemView)
    void exit() {
        ChatManagerHolder.gChatManager.disconnect(true);
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R2.id.newMsgNotifyOptionItemView)
    void notifySetting() {

    }

    @OnClick(R2.id.aboutOptionItemView)
    void about() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}

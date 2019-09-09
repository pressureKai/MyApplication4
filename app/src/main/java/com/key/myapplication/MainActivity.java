package com.key.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wildfire.chat.app.Config;
import cn.wildfire.chat.app.login.model.LoginResult;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.conversationlist.ConversationListFragment;
import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewpager;
    private List<Fragment> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewpager = findViewById(R.id.conversation_fragment);
        findViewById(R.id.login).setOnClickListener(view -> login());
        mViewpager = findViewById(R.id.conversation_fragment);
        findViewById(R.id.button_1).setOnClickListener(view -> mViewpager.setCurrentItem(0));
        findViewById(R.id.button_2).setOnClickListener(view -> mViewpager.setCurrentItem(1));
        findViewById(R.id.button_3).setOnClickListener(view -> mViewpager.setCurrentItem(2));
        findViewById(R.id.button_4).setOnClickListener(view -> mViewpager.setCurrentItem(3));
        mList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            ConversationListFragment conversationListFragment = new ConversationListFragment();
            mList.add(conversationListFragment);
        }
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return  mList.get(position);
            }

            @Override
            public int getCount() {
                return mList.size();
            }
        });
    }


    void login() {
        String phoneNumber = "15816522608";
        String authCode ="66666";
        String url = "http://" + Config.APP_SERVER_HOST + ":" + Config.APP_SERVER_PORT + "/login";
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNumber);
        params.put("code", authCode);
        try {
            params.put("clientId", ChatManagerHolder.gChatManager.getClientId());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        OKHttpHelper.post(url, params, new SimpleCallback<LoginResult>() {
            @Override
            public void onUiSuccess(LoginResult loginResult) {
                ChatManagerHolder.gChatManager.connect(loginResult.getUserId(), loginResult.getToken());
                SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
                sp.edit()
                        .putString("id", loginResult.getUserId())
                        .putString("token", loginResult.getToken())
                        .apply();
            }

            @Override
            public void onUiFailure(int code, String msg) {

            }
        });
    }
}

package cn.wildfire.chat.kit.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.optionitemview.OptionItemView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.contact.newfriend.InviteFriendActivity;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.qrcode.QRCodeActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.chat.R2;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class UserInfoFragment extends Fragment {
    @BindView(R2.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R2.id.nameTextView)
    TextView nameTextView;
    @BindView(R2.id.mobileTextView)
    TextView mobileTextView;
    @BindView(R2.id.nickyName)
    TextView nickyNameTextView;
    @BindView(R2.id.chatButton)
    Button chatButton;
    @BindView(R2.id.voipChatButton)
    Button voipChatButton;
    @BindView(R2.id.inviteButton)
    Button inviteButton;
    @BindView(R2.id.aliasOptionItemView)
    OptionItemView aliasOptionItemView;

    @BindView(R2.id.qrCodeOptionItemView)
    OptionItemView qrCodeOptionItemView;

    private UserInfo userInfo;
    private UserViewModel userViewModel;
    private ContactViewModel contactViewModel;

    public static UserInfoFragment newInstance(UserInfo userInfo) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("userInfo", userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        userInfo = args.getParcelable("userInfo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        userViewModel = WfcUIKit.getAppScopeViewModel(UserViewModel.class);
        contactViewModel = WfcUIKit.getAppScopeViewModel(ContactViewModel.class);
        String selfUid = userViewModel.getUserId();
        if (selfUid.equals(userInfo.uid)) {
            // self
            chatButton.setVisibility(View.GONE);
            voipChatButton.setVisibility(View.GONE);
            inviteButton.setVisibility(View.GONE);
            qrCodeOptionItemView.setVisibility(View.VISIBLE);
            aliasOptionItemView.setVisibility(View.VISIBLE);
        } else if (contactViewModel.isFriend(userInfo.uid)) {
            // friend
            chatButton.setVisibility(View.VISIBLE);
            voipChatButton.setVisibility(View.VISIBLE);
            inviteButton.setVisibility(View.GONE);
        } else {
            // stranger
            chatButton.setVisibility(View.GONE);
            voipChatButton.setVisibility(View.GONE);
            inviteButton.setVisibility(View.VISIBLE);
            aliasOptionItemView.setVisibility(View.GONE);
        }

        setUserInfo(userInfo);
        userViewModel.userInfoLiveData().observe(this, userInfos -> {
            for (UserInfo info : userInfos) {
                if (userInfo.uid.equals(info.uid)) {
                    userInfo = info;
                    setUserInfo(info);
                    break;
                }
            }
        });
        userViewModel.getUserInfo(userInfo.uid, true);
    }

    private void setUserInfo(UserInfo userInfo) {
        Glide.with(this).load(userInfo.portrait).apply(new RequestOptions().placeholder(R.mipmap.avatar_def).centerCrop()).into(portraitImageView);
        nameTextView.setText(userInfo.name);
        nameTextView.setVisibility(View.GONE);
        nickyNameTextView.setText(userViewModel.getUserDisplayName(userInfo));
        if (ChatManager.Instance().isMyFriend(userInfo.uid)) {
            mobileTextView.setText("电话:" + userInfo.mobile);
            mobileTextView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R2.id.chatButton)
    void chat() {
        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        Conversation conversation = new Conversation(Conversation.ConversationType.Single, userInfo.uid, 0);
        intent.putExtra("conversation", conversation);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R2.id.voipChatButton)
    void voipChat() {
        WfcUIKit.onCall(getActivity(), userInfo.uid, true, false);
    }

    @OnClick(R2.id.aliasOptionItemView)
    void alias() {
        String selfUid = userViewModel.getUserId();
        if (selfUid.equals(userInfo.uid)) {
            Intent intent = new Intent(getActivity(), ChangeMyNameActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SetAliasActivity.class);
            intent.putExtra("userId", userInfo.uid);
            startActivity(intent);
        }
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    @OnClick(R2.id.portraitImageView)
    void portrait() {
        if (userInfo.uid.equals(userViewModel.getUserId())) {
            updatePortrait();
        } else {
            // TODO show big portrait
        }
    }

    private void updatePortrait() {
        ImagePicker.picker().pick(this, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            String imagePath = ImageUtils.genThumbImgFile(images.get(0).path).getAbsolutePath();
            MutableLiveData<OperateResult<Boolean>> result = userViewModel.updateUserPortrait(imagePath);
            result.observe(this, booleanOperateResult -> {
                if (booleanOperateResult.isSuccess()) {
                    Toast.makeText(getActivity(), "更新头像成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "更新头像失败: " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R2.id.inviteButton)
    void invite() {
        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R2.id.qrCodeOptionItemView)
    void showMyQRCode() {
        UserInfo userInfo = userViewModel.getUserInfo(userViewModel.getUserId(), false);
        String qrCodeValue = WfcScheme.QR_CODE_PREFIX_USER + userInfo.uid;
        startActivity(QRCodeActivity.buildQRCodeIntent(getActivity(), "二维码", userInfo.portrait, qrCodeValue));
    }
}

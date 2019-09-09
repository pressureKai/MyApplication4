package cn.wildfire.chat.kit.user;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import butterknife.BindView;
import butterknife.OnTextChanged;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.chat.R2;


public class SetAliasActivity extends WfcBaseActivity {

    private String userId;
//    private Friend mFriend;

    @BindView(R2.id.aliasEditText)
    EditText aliasEditText;

    private MenuItem menuItem;
    private UserViewModel userViewModel;

    @Override
    protected int contentLayout() {
        return R.layout.contact_set_alias_activity;
    }

    @Override
    protected void afterViews() {
        userId = getIntent().getStringExtra("userId");
        if (TextUtils.isEmpty(userId)) {
            finish();
            return;
        }
        userViewModel = WfcUIKit.getAppScopeViewModel(UserViewModel.class);
        String alias = userViewModel.getFriendAlias(userId);
        if (!TextUtils.isEmpty(alias)) {
            aliasEditText.setHint(alias);
        }
    }

    @Override
    protected int menu() {
        return R.menu.user_set_alias;
    }

    @Override
    protected void afterMenus(Menu menu) {
        menuItem = menu.findItem(R.id.save);
        menuItem.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            changeAlias();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnTextChanged(R2.id.aliasEditText)
    void onAliasEditTextChange() {
        menuItem.setEnabled(aliasEditText.getText().toString().trim().length() > 0 ? true : false);
    }

    private void changeAlias() {
        String displayName = aliasEditText.getText().toString().trim();
        if (TextUtils.isEmpty(displayName)) {
            UIUtils.showToast(UIUtils.getString(R.string.alias_no_empty));
            return;
        }
        userViewModel.setFriendAlias(userId, displayName).observe(this, new Observer<OperateResult<Integer>>() {
            @Override
            public void onChanged(OperateResult<Integer> integerOperateResult) {
                if (integerOperateResult.isSuccess()) {
                    Toast.makeText(SetAliasActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SetAliasActivity.this, "修改别名错误：" + integerOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
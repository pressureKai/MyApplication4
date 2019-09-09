package cn.wildfire.chat.kit.contact.viewholder.header;

import android.view.View;

import androidx.fragment.app.Fragment;
import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.contact.UserListAdapter;
import cn.wildfire.chat.kit.contact.model.GroupValue;
import cn.wildfirechat.chat.R2;

@SuppressWarnings("unused")
@LayoutRes(resId = "contact_header_group")
public class GroupViewHolder extends HeaderViewHolder<GroupValue> {

    public GroupViewHolder(Fragment fragment, UserListAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(GroupValue groupValue) {

    }
}
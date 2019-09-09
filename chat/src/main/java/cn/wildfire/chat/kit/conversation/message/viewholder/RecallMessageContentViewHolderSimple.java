package cn.wildfire.chat.kit.conversation.message.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.chat.R2;
import cn.wildfirechat.message.notification.RecallMessageContent;

@MessageContentType(RecallMessageContent.class)
@LayoutRes(resId = "conversation_item_notification")
public class RecallMessageContentViewHolderSimple extends SimpleNotificationMessageContentViewHolder {
    @BindView(R2.id.notificationTextView)
    TextView notificationTextView;

    public RecallMessageContentViewHolderSimple(FragmentActivity activity, RecyclerView.Adapter adapter, View itemView) {
        super(activity, adapter, itemView);
    }

    @Override
    protected void onBind(UiMessage message) {
        notificationTextView.setText(message.message.digest());
    }
}

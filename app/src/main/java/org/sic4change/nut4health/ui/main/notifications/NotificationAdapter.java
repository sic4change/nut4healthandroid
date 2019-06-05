package org.sic4change.nut4health.ui.main.notifications;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Notification;

public class NotificationAdapter extends PagedListAdapter<Notification, NotificationViewHolder> {

    private ItemAction mItemOnClickAction;

    private Context context;
    private String userId;

    public NotificationAdapter(Context context, String userId) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public interface ItemAction {
        void onClick(String id);
    }

    public void setItemOnClickAction(ItemAction itemOnClickAction) {
        mItemOnClickAction = itemOnClickAction;
    }

    public Notification getNotificationAtPosition(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = getItem(position);
        if (userId != null && notification != null) {
            holder.bindTo(userId, notification, mItemOnClickAction);
        } else {
            holder.clear();
        }
    }

    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Notification>() {
                @Override
                public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return (oldItem.getId() == newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return oldItem == newItem;
                }
            };


}

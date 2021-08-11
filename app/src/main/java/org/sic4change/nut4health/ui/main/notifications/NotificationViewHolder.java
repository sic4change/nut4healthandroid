package org.sic4change.nut4health.ui.main.notifications;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Notification;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;


public class NotificationViewHolder extends RecyclerView.ViewHolder {


private CardView cvNotification;
private TextView tvTitle;
private TextView tvDate;
private Notification mNotification;
private NotificationAdapter.ItemAction itemAction;
private Context context;

    NotificationViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvDate = itemView.findViewById(R.id.tvDate);
        cvNotification = itemView.findViewById(R.id.cvNotification);
    }

    public Notification getNotification() {
        return mNotification;
    }

    void bindTo(String userId, Notification notification, final NotificationAdapter.ItemAction itemAction) {
        mNotification = notification;
        tvTitle.setText(mNotification.getText());
        try {
            if (notification.getRead().contains(userId)) {
                tvTitle.setTextColor(context.getResources().getColor(R.color.common_google_signin_btn_text_light_default));
            } else {
                tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
        } catch (Exception e) {
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        Date date = new Date(notification.getCreationDate());
        Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        String text = TimeAgo.using(date.getTime(), messages);
        tvDate.setText(text);
        setClickAction(this.itemAction);
        cvNotification.setOnClickListener(v -> itemAction.onClick(getNotification().getId()));
    }

    void clear() {
        itemView.invalidate();
        tvTitle.invalidate();
        tvDate.invalidate();
    }

    void setClickAction(NotificationAdapter.ItemAction itemClickAction) {
        itemAction = itemClickAction;
    }
}

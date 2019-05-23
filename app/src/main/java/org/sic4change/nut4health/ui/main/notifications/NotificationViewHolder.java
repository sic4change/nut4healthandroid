package org.sic4change.nut4health.ui.main.notifications;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Notification;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.ui.main.payments.PaymentAdapter;
import org.sic4change.nut4health.ui.main.ranking.RankingAdapter;


public class NotificationViewHolder extends RecyclerView.ViewHolder {


private CardView cvNotification;
private TextView tvTitle;
private com.github.curioustechizen.ago.RelativeTimeTextView tvDate;
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
        if (notification.getRead().contains(userId)) {
            tvTitle.setTextColor(context.getResources().getColor(R.color.common_google_signin_btn_text_light_default));
        } else {
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        tvDate.setReferenceTime(mNotification.getDate());
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

package org.sic4change.nut4health.ui.main.payments;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.ui.main.ranking.RankingAdapter;


import java.util.Date;
import java.util.Locale;


public class PaymentViewHolder extends RecyclerView.ViewHolder {


private CardView cvPayment;
private TextView tvTitle;
private TextView tvQuantity;
private TextView tvStatus;
private Button btnContractDetail;
private TextView tvDate;
private Payment mPayment;
private RankingAdapter.ItemAction itemAction;
private Context context;

    PaymentViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvQuantity = itemView.findViewById(R.id.tvQuantity);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvDate = itemView.findViewById(R.id.tvDate);
        btnContractDetail = itemView.findViewById(R.id.btnContractDetail);
        cvPayment = itemView.findViewById(R.id.cvRanking);
    }

    public Payment getPayment() {
        return mPayment;
    }

    void bindTo(Payment payment, String money, final PaymentAdapter.ItemAction itemAction) {
        mPayment = payment;

        if (mPayment.getType().equals("Diagnosis")) {
            tvTitle.setText(context.getResources().getString(R.string.bonus_text));
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            btnContractDetail.setVisibility(View.GONE);
        } else if (mPayment.getType().equals("Month")) {
            tvTitle.setText(context.getResources().getString(R.string.monthly_payment_text));
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            btnContractDetail.setVisibility(View.GONE);
        } else {
            tvTitle.setText(context.getResources().getString(R.string.confirmation_text));
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            btnContractDetail.setVisibility(View.VISIBLE);
        }
        if (mPayment.getStatus().equals("CREATED")) {
            tvStatus.setText(context.getResources().getString(R.string.status_created));
            tvStatus.setTextColor(context.getResources().getColor(R.color.orange));
        } else if (mPayment.getStatus().equals("PAID")) {
            tvStatus.setText(context.getResources().getString(R.string.status_paid));
            tvStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            tvStatus.setText(context.getResources().getString(R.string.status_cancel));
            tvStatus.setTextColor(context.getResources().getColor(R.color.error));
        }
        tvQuantity.setText(mPayment.getQuantity() + " " + money);
        Date date = new Date(mPayment.getCreationDateMiliseconds());
        Locale LocaleBylanguageTag = Locale.forLanguageTag(Locale.getDefault().getLanguage());
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        String text = TimeAgo.using(date.getTime(), messages);
        tvDate.setText(text);
        setClickAction(this.itemAction);
        btnContractDetail.setOnClickListener(v -> itemAction.onClick(getPayment().getContractId()));
    }

    void clear() {
        itemView.invalidate();
        tvTitle.invalidate();
        tvQuantity.invalidate();
        tvDate.invalidate();
    }

    void setClickAction(RankingAdapter.ItemAction itemClickAction) {
        itemAction = itemClickAction;
    }
}

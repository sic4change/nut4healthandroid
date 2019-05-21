package org.sic4change.nut4health.ui.main.payments;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.ui.main.ranking.RankingAdapter;


public class PaymentViewHolder extends RecyclerView.ViewHolder {


private CardView cvPayment;
private TextView tvTitle;
private TextView tvQuantity;
private com.github.curioustechizen.ago.RelativeTimeTextView tvDate;
private Payment mPayment;
private RankingAdapter.ItemAction itemAction;
private Context context;

    PaymentViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvQuantity = itemView.findViewById(R.id.tvQuantity);
        tvDate = itemView.findViewById(R.id.tvDate);
        cvPayment = itemView.findViewById(R.id.cvRanking);
    }

    public Payment getPayment() {
        return mPayment;
    }

    void bindTo(Payment payment, final PaymentAdapter.ItemAction itemAction) {
        mPayment = payment;

        if (mPayment.getType().toUpperCase().equals(Payment.Status.BONUS.name())) {
            tvTitle.setText(context.getResources().getString(R.string.bonus_text));
        } else {
            tvTitle.setText(context.getResources().getString(R.string.confirmation_text));
        }
        tvQuantity.setText(mPayment.getQuantity() + " euro");
        tvDate.setReferenceTime(mPayment.getDate());
        setClickAction(this.itemAction);
        //cvPayment.setOnClickListener(v -> itemAction.onClick(getPayment().getContractId()));
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

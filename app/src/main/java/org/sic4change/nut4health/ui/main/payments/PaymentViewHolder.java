package org.sic4change.nut4health.ui.main.payments;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Payment;
import org.sic4change.nut4health.ui.main.ranking.RankingAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PaymentViewHolder extends RecyclerView.ViewHolder {


private CardView cvPayment;
private TextView tvTitle;
private TextView tvQuantity;
private Button btnContractDetail;
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
        btnContractDetail = itemView.findViewById(R.id.btnContractDetail);
        cvPayment = itemView.findViewById(R.id.cvRanking);
    }

    public Payment getPayment() {
        return mPayment;
    }

    void bindTo(Payment payment, final PaymentAdapter.ItemAction itemAction) {
        mPayment = payment;

        if (mPayment.getType().equals(Payment.Status.Bonus.name())) {
            tvTitle.setText(context.getResources().getString(R.string.bonus_text));
            tvTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            btnContractDetail.setVisibility(View.GONE);
        } else {
            tvTitle.setText(context.getResources().getString(R.string.confirmation_text));
            tvTitle.setTextColor(context.getResources().getColor(R.color.ms_errorColor));
            btnContractDetail.setVisibility(View.VISIBLE);
        }
        tvQuantity.setText(mPayment.getQuantity() + " euro");
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = formatter.parse(mPayment.getCreationDate());
            tvDate.setReferenceTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

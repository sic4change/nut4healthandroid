package org.sic4change.nut4health.ui.main.payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Payment;

public class PaymentAdapter extends PagedListAdapter<Payment, PaymentViewHolder> {

    private ItemAction mItemOnClickAction;

    private String money;

    Context context;

    public PaymentAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }


    public interface ItemAction {
        void onClick(String contractId);
    }

    public void setItemOnClickAction(ItemAction itemOnClickAction) {
        mItemOnClickAction = itemOnClickAction;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Payment getPaymentAtPosition(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = getItem(position);
        if (payment != null) {
            holder.bindTo(payment, money,mItemOnClickAction);
        } else {
            holder.clear();
        }
    }

    private static final DiffUtil.ItemCallback<Payment> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Payment>() {
                @Override
                public boolean areItemsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
                    return (oldItem.getId() == newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Payment oldItem, @NonNull Payment newItem) {
                    return oldItem == newItem;
                }
            };


}

package org.sic4change.nut4health.ui.main.contracts.child;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.paging.PagingDataAdapter;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

public class ContractsAdapter extends PagingDataAdapter<Contract, ContractViewHolder> {

    private ItemAction mItemOnClickAction;

    Context context;
    String patient = "";

    public ContractsAdapter(Context context, String patient) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.patient = patient;
    }

    public interface ItemAction {
        void onClick(int position, String id);
    }

    public void setItemOnClickAction(ItemAction itemOnClickAction) {
        mItemOnClickAction = itemOnClickAction;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_contract, parent, false);
        return new ContractViewHolder(itemView, context, patient);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        Contract contract = getItem(position);
        if (contract != null) {
            holder.bindTo(position, contract, mItemOnClickAction);
        } else {
            holder.clear();
        }
    }

    private static final DiffUtil.ItemCallback<Contract> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Contract>() {
                @Override
                public boolean areItemsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }
            };


}

package org.sic4change.nut4health.ui.main.contracts;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

public class ContractsAdapter extends PagedListAdapter<Contract, ContractViewHolder> {

    private ItemAction mItemOnClickAction;

    Context context;

    public ContractsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public interface ItemAction {
        void onClick(String id);
    }

    public void setItemOnClickAction(ItemAction itemOnClickAction) {
        mItemOnClickAction = itemOnClickAction;
    }

    public Contract getContractAtPosition(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_contract, parent, false);
        return new ContractViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHolder holder, int position) {
        Contract contract = getItem(position);
        if (contract != null) {
            holder.bindTo(contract, mItemOnClickAction);
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
                    return oldItem == newItem;
                }
            };


}
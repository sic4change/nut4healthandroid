package org.sic4change.nut4health.ui.main.contracts.fefa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

public class FEFAContractsAdapter extends PagedListAdapter<Contract, FEFAContractViewHolder> {

    private ItemAction mItemOnClickAction;

    Context context;

    public FEFAContractsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public interface ItemAction {
        void onClick(int position, String id);
    }

    public void setItemOnClickAction(ItemAction itemOnClickAction) {
        mItemOnClickAction = itemOnClickAction;
    }

    @NonNull
    @Override
    public FEFAContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_fefa_contract, parent, false);
        return new FEFAContractViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FEFAContractViewHolder holder, int position) {
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

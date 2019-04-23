package org.sic4change.nut4health.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;


public class ContractViewHolder extends RecyclerView.ViewHolder {

private TextView nChildName;
private TextView nChildSurname;
private TextView nChildLocation;
private TextView nPercentage;
private RelativeTimeTextView nDate;
private Contract mContract;
private ContractsAdapter.ItemAction itemAction;
private Context context;

    ContractViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        nChildName = itemView.findViewById(R.id.tvNameItem);
        nChildSurname = itemView.findViewById(R.id.tvSurnameItem);
        nChildLocation = itemView.findViewById(R.id.tvLocationItem);
        nPercentage = itemView.findViewById(R.id.tvPercentageItem);
        nDate = itemView.findViewById(R.id.tvDateItem);
    }

    public Contract getContract() {
        return mContract;
    }

    void bindTo(Contract contract, final ContractsAdapter.ItemAction itemAction) {
        mContract = contract;
        nChildName.setText(contract.getChildName());
        nChildSurname.setText(contract.getChildSurname());
        nChildLocation.setText(contract.getChildAddress());
        nPercentage.setText(contract.getPercentage() + "%");
        if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name())) {
            nPercentage.setTextColor(context.getResources().getColor(R.color.ms_errorColor));
        } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
            nPercentage.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            nPercentage.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        nDate.setReferenceTime(contract.getDate());
        setClickAction(this.itemAction);
        nChildName.setOnClickListener(v -> itemAction.onClick(getContract().getId()));
    }

    void clear() {
        itemView.invalidate();
        nChildName.invalidate();
        nChildSurname.invalidate();
        nChildLocation.invalidate();
    }

    void setClickAction(ContractsAdapter.ItemAction itemClickAction) {
        itemAction = itemClickAction;
    }
}

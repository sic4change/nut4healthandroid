package org.sic4change.nut4health.ui.main.contracts;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;


public class ContractViewHolder extends RecyclerView.ViewHolder {

private TextView nChildName;
private TextView nChildLocation;
private CircleView nPercentage;
private RelativeTimeTextView nDate;
private RelativeTimeTextView nConfirmationDate;
private CardView cvContract;
private Contract mContract;
private ContractsAdapter.ItemAction itemAction;
private Context context;

    ContractViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        nChildName = itemView.findViewById(R.id.tvNameItem);
        nChildLocation = itemView.findViewById(R.id.tvLocationItem);
        nPercentage = itemView.findViewById(R.id.tvPercentageItem);
        cvContract = itemView.findViewById(R.id.cvContractItem);
        nDate = itemView.findViewById(R.id.tvDateItem);
        nConfirmationDate = itemView.findViewById(R.id.tvDateConfirmationItem);
    }

    public Contract getContract() {
        return mContract;
    }

    void bindTo(int position, Contract contract, final ContractsAdapter.ItemAction itemAction) {
        mContract = contract;
        nChildName.setText(contract.getChildName() + " " + contract.getChildSurname());
        nChildLocation.setText(contract.getChildAddress());
        nPercentage.setTitleText(contract.getPercentage() + "%");
        if (contract.getStatus().equals(Contract.Status.DIAGNOSIS.name())) {
            nPercentage.setFillColor(context.getResources().getColor(R.color.ms_errorColor));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.ms_errorColor));
            nConfirmationDate.setVisibility(View.GONE);
        } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
            nPercentage.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nConfirmationDate.setVisibility(View.GONE);
        } else {
            nPercentage.setFillColor(context.getResources().getColor(R.color.colorAccent));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.colorAccent));
            nConfirmationDate.setReferenceTime(contract.getMedicalDate());
            nConfirmationDate.setVisibility(View.VISIBLE);
        }
        nDate.setReferenceTime(contract.getDate());
        setClickAction(this.itemAction);
        cvContract.setOnClickListener(v -> itemAction.onClick(position, getContract().getId()));
    }

    void clear() {
        itemView.invalidate();
        nChildName.invalidate();
        nChildLocation.invalidate();
    }

    void setClickAction(ContractsAdapter.ItemAction itemClickAction) {
        itemAction = itemClickAction;
    }
}

package org.sic4change.nut4health.ui.main.contracts;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ContractViewHolder extends RecyclerView.ViewHolder {

private TextView nChildName;
private TextView nChildLocation;
private CircleView nPercentage;
private TextView nStatus;
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
        nStatus = itemView.findViewById(R.id.tvStatus);
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
            nStatus.setText(context.getResources().getString(R.string.diagnosis));
            nStatus.setTextColor(context.getResources().getColor(R.color.ms_errorColor));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        } else if (contract.getStatus().equals(Contract.Status.NO_DIAGNOSIS.name())) {
            nPercentage.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nStatus.setText(context.getResources().getString(R.string.no_diagnosis));
            nStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        } else if (contract.getStatus().equals(Contract.Status.PAID.name())) {
            nPercentage.setFillColor(context.getResources().getColor(R.color.colorAccent));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.colorAccent));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
            try {
                Date date = formatter.parse(contract.getMedicalDate());
                nConfirmationDate.setReferenceTime(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nStatus.setText(context.getResources().getString(R.string.paid));
            nStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
            nConfirmationDate.setVisibility(View.VISIBLE);
        } else if (contract.getStatus().equals(Contract.Status.FINISH.name())) {
            nPercentage.setFillColor(context.getResources().getColor(R.color.orange));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.orange));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
            try {
                Date date = formatter.parse(contract.getMedicalDate());
                nConfirmationDate.setReferenceTime(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            nStatus.setText(context.getResources().getString(R.string.finished));
            nStatus.setTextColor(context.getResources().getColor(R.color.orange));
            nConfirmationDate.setVisibility(View.VISIBLE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
        try {
            Date date = formatter.parse(contract.getCreationDate());
            nDate.setReferenceTime(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

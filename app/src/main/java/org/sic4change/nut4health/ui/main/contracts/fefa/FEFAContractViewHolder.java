package org.sic4change.nut4health.ui.main.contracts.fefa;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.github.pavlospt.CircleView;

import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;

import java.util.Date;
import java.util.Locale;


public class FEFAContractViewHolder extends RecyclerView.ViewHolder {

private TextView nTutorName;
private TextView nChildLocation;
private CircleView nPercentage;
private TextView nStatus;
private TextView nDate;
private TextView nConfirmationDate;
private TextView tvFefaStatus;
private CardView cvContract;
private Contract mContract;
private FEFAContractsAdapter.ItemAction itemAction;
private Context context;

    FEFAContractViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        nTutorName = itemView.findViewById(R.id.tvNameItem);
        nStatus = itemView.findViewById(R.id.tvStatus);
        tvFefaStatus = itemView.findViewById(R.id.tvSex);
        nChildLocation = itemView.findViewById(R.id.tvLocationItem);
        nPercentage = itemView.findViewById(R.id.tvPercentageItem);
        cvContract = itemView.findViewById(R.id.cvContractItem);
        nDate = itemView.findViewById(R.id.tvDateItem);
        nConfirmationDate = itemView.findViewById(R.id.tvDateConfirmationItem);
    }

    public Contract getContract() {
        return mContract;
    }

    void bindTo(int position, Contract contract, final FEFAContractsAdapter.ItemAction itemAction) {
        mContract = contract;
        nTutorName.setText(contract.getChildTutor());
        nChildLocation.setText(contract.getChildAddress());
        if (contract.getPercentage() < 50) {
            nPercentage.setTitleText(context.getResources().getString(R.string.normopeso_abrev));
            nPercentage.setFillColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nStatus.setText(context.getResources().getString(R.string.normopeso));
            nStatus.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        } else if (contract.getPercentage() == 50) {
            nPercentage.setTitleText(context.getResources().getString(R.string.moderate_acute_malnutrition_abrev));
            nPercentage.setFillColor(context.getResources().getColor(R.color.orange));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.orange));
            nStatus.setText(context.getResources().getString(R.string.moderate_acute_malnutrition));
            nStatus.setTextColor(context.getResources().getColor(R.color.orange));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        } else {
            nPercentage.setTitleText(context.getResources().getString(R.string.severe_acute_malnutrition_abrev));
            nPercentage.setFillColor(context.getResources().getColor(R.color.ms_errorColor));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.ms_errorColor));
            nStatus.setText(context.getResources().getString(R.string.severe_acute_malnutrition));
            nStatus.setTextColor(context.getResources().getColor(R.color.ms_errorColor));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        }
        if (contract.getStatus().equals(Contract.Status.ADMITTED.name())) {
            nPercentage.setFillColor(context.getResources().getColor(R.color.violet));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.violet));
            try {
                Date date = new Date(contract.getMedicalDate());
                Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
                TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                String text = TimeAgo.using(date.getTime(), messages);
                nConfirmationDate.setText(text);
            } catch (Exception e) {
                nConfirmationDate.setText("");
            }
            nStatus.setText(context.getResources().getString(R.string.admitted));
            nStatus.setTextColor(context.getResources().getColor(R.color.violet));
            nConfirmationDate.setVisibility(View.VISIBLE);
        }  else if (contract.getStatus().equals(Contract.Status.DUPLICATED.name())) {
            nPercentage.setTitleText(context.getResources().getString(R.string.duplicated_abrev));
            nPercentage.setFillColor(context.getResources().getColor(R.color.rose));
            nPercentage.setStrokeColor(context.getResources().getColor(R.color.rose));
            nStatus.setText(context.getResources().getString(R.string.duplicated));
            nStatus.setTextColor(context.getResources().getColor(R.color.rose));
            nConfirmationDate.setVisibility(View.INVISIBLE);
        }

        tvFefaStatus.setText(contract.getTutorStatus());
        Date date = new Date(contract.getCreationDate());
        Locale LocaleBylanguageTag = Locale.forLanguageTag("es");
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
        String text = TimeAgo.using(date.getTime(), messages);
        nDate.setText(text);
        setClickAction(this.itemAction);
        cvContract.setOnClickListener(v -> itemAction.onClick(position, getContract().getId()));
    }

    void clear() {
        itemView.invalidate();
        nTutorName.invalidate();
        nChildLocation.invalidate();
    }

    void setClickAction(FEFAContractsAdapter.ItemAction itemClickAction) {
        itemAction = itemClickAction;
    }
}

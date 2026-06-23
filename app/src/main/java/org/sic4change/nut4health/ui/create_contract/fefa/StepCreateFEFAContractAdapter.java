package org.sic4change.nut4health.ui.create_contract.fefa;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.sic4change.nut4health.R;


public class StepCreateFEFAContractAdapter extends AbstractFragmentStepAdapter {

    private Context mContext;

    public StepCreateFEFAContractAdapter(FragmentManager fm, Context context) {
        super(fm, context);
        this.mContext = context;
    }

    @Override
    public Step createStep(int position) {
        final StepCreateFEFAContractFragment step = new StepCreateFEFAContractFragment();
        step.setPosition(position);
        Bundle b = new Bundle();
        //b.putInt(CURRENT_STEP_POSITION_KEY, position);
        b.putInt("key", position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        String title = "";
        if (position == 0) {
            title = context.getString(R.string.first_step_title);
        } else if (position == 1) {
            title = "FEFA";
        } else {
            title = context.getString(R.string.third_step_title);
        }
        StepViewModel stepViewModel = new StepViewModel.Builder(context)
                .setTitle(title)
                .create();
        return stepViewModel;
    }
}


package org.sic4change.nut4health.ui.main.contracts.child;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sic4change.nut4health.R;
import org.sic4change.nut4health.data.entities.Contract;
import org.sic4change.nut4health.ui.create_contract.child.CreateContractActivity;
import org.sic4change.nut4health.ui.main.MainViewModel;
import org.sic4change.nut4health.utils.Nut4HealthKeyboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

import static maes.tech.intentanim.CustomIntent.customType;


public class ContractFragment extends Fragment {

    private FloatingActionButton btnCreateContract;
    private FloatingActionButton btnFilterContracts;
    private FloatingActionButton btnExportContract;
    private CardView lyFilter;
    private EditText etName;
    private EditText etSurname;
    private View ivStatus;
    private Spinner spStatus;
    private EditText tvDateRange;
    private Button btnFilter;
    private Button btnClear;

    private long timeRangeMin;
    private long timeRangeMax;

    private MainViewModel mMainViewModel;
    private boolean exportContract = false;

    public ContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        exportContract = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.list));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.map));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        final PagerFragmentAdapter adapter = new PagerFragmentAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), mMainViewModel.getCurrentUser().getValue().getRole());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        btnCreateContract = view.findViewById(R.id.btnCreateContract);
        btnCreateContract.setOnClickListener(v -> {
            goToCreateContractActivity();
        });
        btnFilterContracts = view.findViewById(R.id.btnFilterContracts);
        btnFilterContracts.setOnClickListener(v -> {
            showContractFilterMenu();
        });
        btnExportContract = view.findViewById(R.id.btnExportContracts);
        btnExportContract.setOnClickListener(v -> {
            exportContract = true;
            exportContractsToExcel();
        });
        btnClear = view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            Nut4HealthKeyboard.closeKeyboard(etName, getContext());
            mMainViewModel.setIsFiltered(false);
            clear();
        });
        lyFilter = view.findViewById(R.id.lyFilter);
        etName = view.findViewById(R.id.etName);
        etSurname = view.findViewById(R.id.etSurname);
        ivStatus = view.findViewById(R.id.ivStatus);
        spStatus = view.findViewById(R.id.spStatus);
        tvDateRange = view.findViewById(R.id.tvDateRange);
        tvDateRange.setOnClickListener(v -> {
            SlyCalendarDialog.Callback callback = new SlyCalendarDialog.Callback() {
                @Override
                public void onCancelled() {

                }

                @Override
                public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
                    try {
                        timeRangeMin = firstDate.getTimeInMillis();
                        timeRangeMax = secondDate.getTimeInMillis();
                        tvDateRange.setText(firstDate.get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1) + "/" + firstDate.get(Calendar.YEAR)
                                + " - " + secondDate.get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1) + "/" + secondDate.get(Calendar.YEAR));
                    } catch (Exception e) {
                        timeRangeMin = firstDate.getTimeInMillis();
                        timeRangeMax = Calendar.getInstance().getTimeInMillis();
                        tvDateRange.setText(firstDate.get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1)  + "/" + firstDate.get(Calendar.YEAR)
                                + " - " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (firstDate.get(Calendar.MONTH) + 1)  + "/" + Calendar.getInstance().get(Calendar.YEAR));
                    }
                }
            };
            new SlyCalendarDialog()
                    .setSingle(false)
                    .setCallback(callback)
                    .setHeaderColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .setBackgroundColor(getContext().getResources().getColor(R.color.white))
                    .setSelectedTextColor(getContext().getResources().getColor(R.color.white))
                    .setSelectedColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .show(getActivity().getSupportFragmentManager(), "TAG_CALENDAR_RANGE_SELECTION");
        });
        btnFilter = view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> {
            Nut4HealthKeyboard.closeKeyboard(etName, getContext());
            filterContracts();
            lyFilter.setVisibility(View.GONE);
        });
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.ms_black));
                        break;
                    case 1:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.violet));
                        break;
                    case 2:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 3:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case 4:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.orange));
                        break;
                    case 5:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.ms_errorColor));
                        break;
                    default:
                        ivStatus.setBackgroundColor(getResources().getColor(R.color.rose));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        return view;
    }

    private void initData() {
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getIsFiltered().observe(getActivity(), filtered -> {
            try {
                if (filtered) {
                    btnFilterContracts.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                } else {
                    btnFilterContracts.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                }
            } catch (Exception e) {
                System.out.println("button null now");
            }

        });
        mMainViewModel.getSortedContracts("DATE", "child", mMainViewModel.getName(),
                mMainViewModel.getSurname(), mMainViewModel.getTutorName(), mMainViewModel.getTutorStatus(),
                mMainViewModel.getStatus(), mMainViewModel.getDateStart(), mMainViewModel.getDateEnd(),
                mMainViewModel.getPercentageMin(), mMainViewModel.getPercentageMax());
    }

    private void goToCreateContractActivity() {
        Intent intent = new Intent(getActivity(), CreateContractActivity.class);
        startActivity(intent);
        customType(getActivity(),"left-to-right");
    }

    private void showContractFilterMenu() {
        if (lyFilter.getVisibility() == View.VISIBLE) {
            lyFilter.setVisibility(View.GONE);
            Nut4HealthKeyboard.closeKeyboard(etName, getContext());
        } else if (lyFilter.getVisibility() == View.GONE) {
            lyFilter.setVisibility(View.VISIBLE);
        }
    }

    private void exportContractsToExcel() {
            mMainViewModel.getContracts().observe(getActivity(), contracts -> {
                if (exportContract) {
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet(getString(R.string.contracts));
                    Row rowHeader = sheet.createRow(0);
                    rowHeader.createCell(0).setCellValue(getString(R.string.name_export));
                    rowHeader.createCell(1).setCellValue(getString(R.string.surnname_export));
                    rowHeader.createCell(2).setCellValue(getString(R.string.sex_export));
                    rowHeader.createCell(3).setCellValue(getString(R.string.address_export));
                    rowHeader.createCell(4).setCellValue(getString(R.string.phone_export));
                    rowHeader.createCell(5).setCellValue(getString(R.string.date_export));
                    rowHeader.createCell(6).setCellValue(getString(R.string.date_millis_export));
                    rowHeader.createCell(7).setCellValue(getString(R.string.screener_export));
                    rowHeader.createCell(8).setCellValue(getString(R.string.service_export));
                    rowHeader.createCell(9).setCellValue(getString(R.string.date_health_service));
                    rowHeader.createCell(10).setCellValue(getString(R.string.date_millis_health_service));
                    rowHeader.createCell(11).setCellValue(getString(R.string.locatoin_health_service));
                    rowHeader.createCell(12).setCellValue(getString(R.string.status));
                    for(int  i=0; i<contracts.size(); i++){
                        Row row = sheet.createRow(i+1);
                        String status = "";
                        if (contracts.get(i).getPercentage() < 50) {
                            status = getString(R.string.normopeso_abrev);
                        } else if (contracts.get(i).getPercentage() == 50) {
                            status = getString(R.string.moderate_acute_malnutrition_abrev);
                        } else {
                            status = getString(R.string.severe_acute_malnutrition_abrev);
                        }
                        row.createCell(0).setCellValue(contracts.get(i).getChildName());
                        row.createCell(1).setCellValue(contracts.get(i).getChildSurname());
                        row.createCell(2).setCellValue(contracts.get(i).getSex());
                        row.createCell(3).setCellValue(contracts.get(i).getChildAddress());
                        row.createCell(4).setCellValue(contracts.get(i).getChildPhoneContract());
                        row.createCell(5).setCellValue(contracts.get(i).getCreationDate());
                        row.createCell(6).setCellValue(contracts.get(i).getCreationDateMiliseconds());
                        row.createCell(7).setCellValue(contracts.get(i).getScreener());
                        row.createCell(8).setCellValue(contracts.get(i).getMedical());
                        row.createCell(9).setCellValue(contracts.get(i).getMedicalDate());
                        row.createCell(10).setCellValue(contracts.get(i).getMedicalDateMiliseconds());
                        row.createCell(11).setCellValue(contracts.get(i).getPointFullName());
                        row.createCell(12).setCellValue(status);
                    }
                    File file = new File(getActivity().getExternalFilesDir(null), "contracts.xlsx");
                    try {
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        FileOutputStream fileOut = new FileOutputStream(file);
                        workbook.write(fileOut);
                        fileOut.close();
                    } catch (FileNotFoundException e) {
                        showDialogErrorExportContractsResult();
                    } catch (IOException e) {
                        showDialogErrorExportContractsResult();
                    }
                    exportContract = false;
                    showDialogExportContractsResult();
                }

            });
    }

    public void showDialogExportContractsResult() {
        new AwesomeSuccessDialog(getActivity())
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.contracts_exported_ok))
                .setPositiveButtonText(getResources().getString(R.string.ok))
                .setPositiveButtonClick(() -> {
                    openFile();
                })
                .show();
    }

    public void showDialogErrorExportContractsResult() {
        new AwesomeErrorDialog(getActivity())
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.contracts_exported_error))
                .setButtonText(getResources().getString(R.string.ok))
                .setErrorButtonClick(() -> {

                }).show();
    }

    private void openFile() {
        File file = new File(getActivity().getExternalFilesDir(null), "contracts.xlsx");
        Uri uri = FileProvider.getUriForFile(getActivity(), "org.sic4change.nut4health.android.fileprovider", file);
        Intent in = new Intent(Intent.ACTION_VIEW);
        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        in.setDataAndType(uri, "text/html");
        getActivity().startActivity(in);
    }

    private void clear() {
        etName.setText("");
        etSurname.setText("");
        tvDateRange.setText("");
        spStatus.setSelection(0);
        mMainViewModel.setName("");
        mMainViewModel.setSurname("");
        mMainViewModel.setStatus(Contract.Status.EMPTY.name());
        timeRangeMin = 0;
        timeRangeMax = 0;
        mMainViewModel.setDateEnd(timeRangeMin);
        mMainViewModel.setDateStart(timeRangeMax);
        mMainViewModel.setPercentageMax(100);
        mMainViewModel.setPercentageMin(0);
        mMainViewModel.getSortedContracts("DATE", "child", mMainViewModel.getName(),
                mMainViewModel.getSurname(), mMainViewModel.getTutorName(), mMainViewModel.getTutorStatus(),
                mMainViewModel.getStatus(), mMainViewModel.getDateStart(), mMainViewModel.getDateEnd(),
                mMainViewModel.getPercentageMin(), mMainViewModel.getPercentageMax());
        lyFilter.setVisibility(View.GONE);
        mMainViewModel.setIsFiltered(false);
    }

    private void filterContracts() {
        mMainViewModel.setName(etName.getText().toString());
        mMainViewModel.setSurname(etSurname.getText().toString());
        switch (spStatus.getSelectedItemPosition()) {
            case 0:
                mMainViewModel.setStatus(Contract.Status.EMPTY.name());
                mMainViewModel.setPercentageMin(0);
                mMainViewModel.setPercentageMax(100);
                break;
            case 1:
                mMainViewModel.setStatus(Contract.Status.ADMITTED.name());
                mMainViewModel.setPercentageMin(0);
                mMainViewModel.setPercentageMax(100);
                break;
            case 2:
                mMainViewModel.setStatus(Contract.Status.EMPTY.name());
                mMainViewModel.setPercentageMax(0);
                mMainViewModel.setPercentageMin(0);
                break;
            case 3:
                mMainViewModel.setStatus(Contract.Status.EMPTY.name());
                mMainViewModel.setPercentageMax(50);
                mMainViewModel.setPercentageMin(50);
                break;
            case 4:
                mMainViewModel.setStatus(Contract.Status.EMPTY.name());
                mMainViewModel.setPercentageMax(100);
                mMainViewModel.setPercentageMin(100);
                break;
            case 5:
                mMainViewModel.setStatus(Contract.Status.DUPLICATED.name());
                break;
            default:
                mMainViewModel.setStatus(Contract.Status.EMPTY.name());
                mMainViewModel.setPercentageMax(0);
                mMainViewModel.setPercentageMin(100);
                break;
        }
        if (timeRangeMin != 0) {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(timeRangeMin);
            c1.add(Calendar.DATE, -1);
            mMainViewModel.setDateStart(c1.getTimeInMillis());
        } else {
            mMainViewModel.setDateStart(timeRangeMin);
        }
        if (timeRangeMax != 0) {
            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(timeRangeMax);
            c2.add(Calendar.DATE, 1);
            mMainViewModel.setDateEnd(c2.getTimeInMillis());
        } else {
            mMainViewModel.setDateStart(timeRangeMax);
        }
        mMainViewModel.getSortedContracts("DATE", "child", mMainViewModel.getName(),
                mMainViewModel.getSurname(), mMainViewModel.getTutorName(), mMainViewModel.getTutorStatus(),
                mMainViewModel.getStatus(), mMainViewModel.getDateStart(), mMainViewModel.getDateEnd(),
                mMainViewModel.getPercentageMin(), mMainViewModel.getPercentageMax());
        mMainViewModel.getContracts().observe(getActivity(), contracts -> mMainViewModel.setIsFiltered(true));
    }



}

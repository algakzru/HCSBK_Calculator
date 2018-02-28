package kz.algakzru.hcsbk_calculator;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DepositFragment extends Fragment {

    private EditText etDogovornayaSumma;
    private EditText etSummaVashihVznosov;
    private EditText etDataZakritiya;

    private Calendar calendar = Calendar.getInstance();
    private DialogFragment depositDialogFragment;

    public DepositFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        etDogovornayaSumma = (EditText) view.findViewById(R.id.et_dogovornaya_summa);
        etDogovornayaSumma.setText(sharedPref.getString(getString(R.string.dogovornaya_summa), "0.00"));
        etDogovornayaSumma.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.dogovornaya_summa), s.toString());
                editor.commit();

            }
        });

        etSummaVashihVznosov = (EditText) view.findViewById(R.id.et_summa_vashih_vznosov);
        etSummaVashihVznosov.setText(sharedPref.getString(getString(R.string.summa_vashih_vznosov), "0.00"));
        etSummaVashihVznosov.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.summa_vashih_vznosov), s.toString());
                editor.commit();

            }
        });
        etSummaVashihVznosov.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                depositDialogFragment.show(getFragmentManager(), "depositDialogFragment");
            }
        });

        etDataZakritiya = (EditText) view.findViewById(R.id.et_data_zakritiya);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        etDataZakritiya.setText(sharedPref.getString(getString(R.string.data_zakritiya), sdf.format(calendar.getTime())));
        etDataZakritiya.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.data_zakritiya), s.toString());
                editor.commit();

            }
        });
        etDataZakritiya.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        depositDialogFragment = new DepositDialogFragment();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            etDataZakritiya.setText(sdf.format(calendar.getTime()));
        }

    };

}

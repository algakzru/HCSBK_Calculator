package kz.algakzru.hcsbk_calculator;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.YearMonth;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreditFragment extends Fragment {

    private EditText etSummaCredita;
    private EditText etSrokCredita;
    private EditText etProcentnayaStavka;
    private EditText etDataVydachiCredita;
    private EditText etDataPervogoPlatezha;
    private EditText etEzhemesiachnyiPlatezh;
    private Button btnClean;
    private Button btnCalculate;
    private Button btnExport;

    private Calendar calendar = Calendar.getInstance();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public CreditFragment() {
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
        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        etSummaCredita = (EditText) view.findViewById(R.id.et_summa_credita);
        etSrokCredita = (EditText) view.findViewById(R.id.et_srok_credita);
        etProcentnayaStavka = (EditText) view.findViewById(R.id.et_procentnaya_stavka);
        etDataVydachiCredita = (EditText) view.findViewById(R.id.et_data_vydachi_credita);
        etDataPervogoPlatezha = (EditText) view.findViewById(R.id.et_data_pervogo_platezha);
        etEzhemesiachnyiPlatezh = (EditText) view.findViewById(R.id.et_ezhemesiachnyi_platezh);
        btnClean = (Button) view.findViewById(R.id.btn_clean);
        btnCalculate = (Button) view.findViewById(R.id.btn_calculate);
        btnExport = (Button) view.findViewById(R.id.btn_export);

//        setHint();
        setText();
        setListeners();

        return view;
    }

    private void setHint() {
        etDataVydachiCredita.setHint(sdf.format(calendar.getTime()));
        etDataPervogoPlatezha.setHint(sdf.format(calendar.getTime()));
    }

    private void setText() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        etSummaCredita.setText(sharedPref.getString(getString(R.string.summa_credita), ""));
        etSrokCredita.setText(sharedPref.getString(getString(R.string.srok_credita), ""));
        etProcentnayaStavka.setText(sharedPref.getString(getString(R.string.procentnaya_stavka), ""));
        etDataVydachiCredita.setText(sharedPref.getString(getString(R.string.data_vydachi_credita), ""));
        etDataPervogoPlatezha.setText(sharedPref.getString(getString(R.string.data_pervogo_platezha), ""));
    }

    private void resetText() {
        etSummaCredita.setText(null);
        etSrokCredita.setText(null);
        etProcentnayaStavka.setText(null);
        etDataVydachiCredita.setText(null);
        etDataPervogoPlatezha.setText(null);
        etEzhemesiachnyiPlatezh.setText(null);
    }

    private void setListeners() {

        etSummaCredita.addTextChangedListener(new TextWatcher() {

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
                editor.putString(getString(R.string.summa_credita), s.toString());
                editor.commit();
            }
        });

        etSrokCredita.addTextChangedListener(new TextWatcher() {

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
                editor.putString(getString(R.string.srok_credita), s.toString());
                editor.commit();
            }
        });

        etProcentnayaStavka.addTextChangedListener(new TextWatcher() {

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
                editor.putString(getString(R.string.procentnaya_stavka), s.toString());
                editor.commit();
            }
        });

        etDataVydachiCredita.addTextChangedListener(new TextWatcher() {

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
                editor.putString(getString(R.string.data_vydachi_credita), s.toString());
                editor.commit();
            }
        });

        etDataVydachiCredita.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    calendar = Calendar.getInstance();
                    if (!TextUtils.isEmpty(etDataVydachiCredita.getText().toString())) {
                        Date date = sdf.parse(etDataVydachiCredita.getText().toString());
                        calendar.setTime(date);
                    }
                    new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            etDataVydachiCredita.setText(sdf.format(calendar.getTime()));
                        }

                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
                }
            }
        });

        etDataPervogoPlatezha.addTextChangedListener(new TextWatcher() {

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
                editor.putString(getString(R.string.data_pervogo_platezha), s.toString());
                editor.commit();
            }
        });

        etDataPervogoPlatezha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    calendar = Calendar.getInstance();
                    if (!TextUtils.isEmpty(etDataPervogoPlatezha.getText().toString())) {
                        Date date = sdf.parse(etDataPervogoPlatezha.getText().toString());
                        calendar.setTime(date);
                    }
                    new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            etDataPervogoPlatezha.setText(sdf.format(calendar.getTime()));
                        }

                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
                }
            }
        });

        btnClean.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetText();
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                export();
            }
        });
    }

    private void export() {
        try {
            if (TextUtils.isEmpty(etEzhemesiachnyiPlatezh.getText().toString())){
                throw new Exception("Вы не посчитали ежемесячный платёж");
            }
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
    }

    private void calculate() {
        try {
            if (TextUtils.isEmpty(etSummaCredita.getText().toString())){
                throw new Exception("Вы не указали сумму кредита");
            }
            if (TextUtils.isEmpty(etSrokCredita.getText().toString())){
                throw new Exception("Вы не указали срок кредита");
            }
            if (TextUtils.isEmpty(etProcentnayaStavka.getText().toString())){
                throw new Exception("Вы не указали процентную ставку");
            }
            if (TextUtils.isEmpty(etDataVydachiCredita.getText().toString())){
                throw new Exception("Вы не указали дату выдачи кредита");
            }
            if (TextUtils.isEmpty(etDataPervogoPlatezha.getText().toString())){
                throw new Exception("Вы не указали дату первого платежа");
            }

            long raznicaDneiPervogoRaschetnogoPerioda = getRaznicaDneiPervogoRaschetnogoPerioda();
            BigDecimal procentnayaStavka = new BigDecimal(etProcentnayaStavka.getText().toString()).divide(new BigDecimal(100));
            BigDecimal procentPervogoRaschetnogoPerioda = new BigDecimal(raznicaDneiPervogoRaschetnogoPerioda).multiply( procentnayaStavka.divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP) );

            long raznicaDneiPoslednegoRaschetnogoPerioda = getRaznicaDneiPoslednegoRaschetnogoPerioda();
            BigDecimal procentPoslednegoRaschetnogoPerioda = BigDecimal.ZERO;
            BigDecimal procentEzhemesiachnyi = BigDecimal.ZERO;

            etEzhemesiachnyiPlatezh.setText(String.valueOf(procentPervogoRaschetnogoPerioda));
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
    }

    private long getRaznicaDneiPervogoRaschetnogoPerioda() throws Exception {
        Date dataVydachiCredita = sdf.parse(etDataVydachiCredita.getText().toString());
        Calendar calendarVydachiCredita = Calendar.getInstance();
        calendarVydachiCredita.setTime(dataVydachiCredita);

        Date dataPervogoPlatezha = sdf.parse(etDataPervogoPlatezha.getText().toString());
        Calendar calendarPervogoPlatezha = Calendar.getInstance();
        calendarPervogoPlatezha.setTime(dataPervogoPlatezha);

        // diff in millis
        long diff = calendarPervogoPlatezha.getTimeInMillis() - calendarVydachiCredita.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);

        return days;
    }

    private long getRaznicaDneiPoslednegoRaschetnogoPerioda() throws Exception {
        Date dataVydachiCredita = sdf.parse(etDataVydachiCredita.getText().toString());
        Calendar calendarVydachiCredita = Calendar.getInstance();
        calendarVydachiCredita.setTime(dataVydachiCredita);
        // data zakrytia credita
        calendarVydachiCredita.add(Calendar.MONTH, Integer.parseInt(etSrokCredita.getText().toString()));
        long dataZakrytiaCreditaInMillis = calendarVydachiCredita.getTimeInMillis();
        Log.d("test", sdf.format(calendarVydachiCredita.getTime()));

        Date dataPervogoPlatezha = sdf.parse(etDataPervogoPlatezha.getText().toString());
        Calendar calendarPervogoPlatezha = Calendar.getInstance();
        calendarPervogoPlatezha.setTime(dataPervogoPlatezha);
        // data predposlednego platezha
        calendarVydachiCredita.add(Calendar.MONTH, -1);
        calendarVydachiCredita.set(Calendar.DAY_OF_MONTH, calendarPervogoPlatezha.get(Calendar.DAY_OF_MONTH));
        long dataPredposlednegoPlatezhaInMillis = calendarVydachiCredita.getTimeInMillis();
        Log.d("test", sdf.format(calendarVydachiCredita.getTime()));

        // diff in millis
        long diff = dataZakrytiaCreditaInMillis - dataPredposlednegoPlatezhaInMillis;
        long days = diff / (24 * 60 * 60 * 1000);

        return days;
    }

    public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate) {
        int d1 = firstDate.getDayOfMonth();
        int d2 = secondDate.getDayOfMonth();
        if (d1 == 31) {
            d1 = 30;
        }
        if (d2 == 31) {
            d2 = 30;
        }
        return thirty360(
                firstDate.getYear(), firstDate.getMonthOfYear(), d1,
                secondDate.getYear(), secondDate.getMonthOfYear(), d2);
    }

    // calculate using the standard 30/360 function - 360(y2 - y1) + 30(m2 - m1) + (d2 - d1)) / 360
    private double thirty360(int y1, int m1, int d1, int y2, int m2, int d2) {
        return (360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1)) / 360d;
    }

    public int calculateDays(Date d1, Date d2) {

        int months = 0;
        int days = 0;

        months = Months.monthsBetween(new DateTime(d1), new DateTime(d2)).getMonths();

        int endOfMonth = d1.getDate() == 31 ? 31 : 30;

        if(d1.getDate() > d2.getDate()){
            days = endOfMonth - d1.getDate() + d2.getDate();
        }else{
            days = d2.getDate() - d1.getDate();
        }
        months = months * 30;
        days = months + days;

        return days;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

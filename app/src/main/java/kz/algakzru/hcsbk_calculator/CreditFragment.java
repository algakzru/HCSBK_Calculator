package kz.algakzru.hcsbk_calculator;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.joda.time.LocalDate;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import kz.algakzru.hcsbk_calculator.utils.CustomAdapter;
import kz.algakzru.hcsbk_calculator.utils.CustomTextWatcher;
import kz.algakzru.hcsbk_calculator.utils.ListItem;
import kz.algakzru.hcsbk_calculator.utils.NumberTextWatcherForThousand;

public class CreditFragment extends Fragment {

    private Spinner spPaymentType;
    private EditText etSummaCredita;
    private EditText etSrokCredita;
    private EditText etProcentnayaStavka;
    private EditText etDataVydachiCredita;
    private EditText etDataPervogoPlatezha;
    private Button btnCalculate;
    private Button btnExport;

    private Calendar calendar = Calendar.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final String ACCOUNTING_FLOAT = "#,##0.00тг";
    private final String BORDER_FORMAT = "borderFormat";
    private final String CENTRE_FORMAT = "centreFromat";
    private final String PERCENT_FORMAT = "percentFormat";
    private final String TENGE_FORMAT = "tengeFormat";
    private final String YELLOW_TENGE_FORMAT = "yellowTengeFormat";
    private final String GREEN_TENGE_FORMAT = "greenTengeFormat";
    private final String DATE_FORMAT = "dateFormat";
    private final String BLUE_TITLE_FORMAT = "blueTitleFormat";

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

        spPaymentType = (Spinner) view.findViewById(R.id.sp_payment_type);
        etSummaCredita = (EditText) view.findViewById(R.id.et_summa_credita);
        etSrokCredita = (EditText) view.findViewById(R.id.et_srok_credita);
        etProcentnayaStavka = (EditText) view.findViewById(R.id.et_procentnaya_stavka);
        etDataVydachiCredita = (EditText) view.findViewById(R.id.et_data_vydachi_credita);
        etDataPervogoPlatezha = (EditText) view.findViewById(R.id.et_data_pervogo_platezha);
        btnCalculate = (Button) view.findViewById(R.id.btn_calculate);
        btnExport = (Button) view.findViewById(R.id.btn_export);

        setListeners();
        setValues();

        return view;
    }

    private void setValues() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        etSummaCredita.setText(sharedPref.getString((String) etSummaCredita.getTag(), ""));
        etSrokCredita.setText(sharedPref.getString((String) etSrokCredita.getTag(), ""));
        etProcentnayaStavka.setText(sharedPref.getString((String) etProcentnayaStavka.getTag(), ""));
        etDataVydachiCredita.setText(sharedPref.getString((String) etDataVydachiCredita.getTag(), ""));
        etDataPervogoPlatezha.setText(sharedPref.getString((String) etDataPervogoPlatezha.getTag(), ""));
        spPaymentType.setSelection(sharedPref.getInt((String) spPaymentType.getTag(), 0));
    }

    private void setListeners() {

        etSummaCredita.addTextChangedListener(new NumberTextWatcherForThousand(etSummaCredita, this));

        etSrokCredita.addTextChangedListener(new CustomTextWatcher(etSrokCredita, this));

        etProcentnayaStavka.addTextChangedListener(new CustomTextWatcher(etProcentnayaStavka, this));

        etDataVydachiCredita.addTextChangedListener(new CustomTextWatcher(etDataVydachiCredita, this));

        etDataPervogoPlatezha.addTextChangedListener(new CustomTextWatcher(etDataPervogoPlatezha, this));

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

        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String tag = (String) spPaymentType.getTag();
                    if (!TextUtils.isEmpty(tag)) {
                        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt(tag, (int) spPaymentType.getSelectedItemId());
                        editor.commit();
                    }
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void validateEditText() throws Exception {

        // Валидация суммы кредита

        if (TextUtils.isEmpty(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString()))) {
            throw new Exception("Вы не указали сумму кредита");
        }
//        double summaCredita = Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString()));
//        if (100 * 1000000 < summaCredita) {
//            throw new Exception("Сумма кредита не может быть больше 100,000,000тг");
//        }
//        if (2405 * 500 > summaCredita) {
//            throw new Exception("Сумма кредита не может быть меньше 1,202,500тг (500 МРП)");
//        }

        // Валидация срока кредита

        if (TextUtils.isEmpty(etSrokCredita.getText().toString())) {
            throw new Exception("Вы не указали срок кредита");
        }
        int srokCredita = Integer.parseInt(etSrokCredita.getText().toString());
        if (6 * 12 > srokCredita) {
            throw new Exception("Срок кредита не может быть меньше 72 месяцев");
        }
        if (25 * 12 < srokCredita) {
            throw new Exception("Срок кредита не может быть больше 300 месяцев");
        }

        // Валидация процентной ставки

        if (TextUtils.isEmpty(etProcentnayaStavka.getText().toString())) {
            throw new Exception("Вы не указали процентную ставку");
        }
//        double procentnayaStavka = Double.parseDouble(etProcentnayaStavka.getText().toString());
//        if (5d < procentnayaStavka) {
//            throw new Exception("Процентная ставка не может быть больше 5%");
//        }
//        if (3.5d > procentnayaStavka) {
//            throw new Exception("Процентная ставка не может быть меньше 3.5%");
//        }

        // Валидация даты выдачи кредита и даты первого платежа

        if (TextUtils.isEmpty(etDataVydachiCredita.getText().toString())) {
            throw new Exception("Вы не указали дату выдачи кредита");
        }
        if (TextUtils.isEmpty(etDataPervogoPlatezha.getText().toString())) {
            throw new Exception("Вы не указали дату первого платежа");
        }
        LocalDate dataVydachiCredita = new LocalDate(etDataVydachiCredita.getText().toString());
        LocalDate dataPervogoPlatezha = new LocalDate(etDataPervogoPlatezha.getText().toString());
        if (dataVydachiCredita.isAfter(dataPervogoPlatezha)) {
            throw new Exception("Дата первого платежа не может быть раньше даты выдачи кредита");
        }
        if (dataPervogoPlatezha.isAfter(dataVydachiCredita.plusMonths(1))) {
            throw new Exception("Разница от даты выдачи кредита до даты первого платежа не может превышать один месяц");
        }
    }

    // http://www.docjar.com/html/api/jxl/demo/Write.java.html
    private void export() {
        try {
            validateEditText();

            if (!isExternalStorageWritable()) {
                throw new Exception("Внешняя память не доступна");
            }

            // Get the directory for the user's public documents directory
            File dir = new File(Environment.getExternalStorageDirectory(), "Documents");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new Exception("Не удалось создать папку" + System.getProperty("line.separator") + dir.getAbsolutePath());
            }

            final File file = new File(dir, getString(R.string.title_section1) + ".xls");

            // Create Workbook
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale("ru","RU"));
            WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings);

            // Excel sheets
            WritableSheet paramsSheet = workbook.createSheet("Параметры", 0);
            WritableSheet calculateSheet = workbook.createSheet("Вычисление", 1);

            // Set selected sheet
            paramsSheet.getSettings().setSelected(false);
            calculateSheet.getSettings().setSelected(true);

            Map<String, WritableCellFormat> writableCellFormatMap = createWritableCellFormatMap();

            // Add items to Параметры sheet
            addItemsToParamsSheet(paramsSheet, writableCellFormatMap);

            // Add items to Вычисление sheet
            {
                // Аннуитетный
                if (0 == spPaymentType.getSelectedItemId()) {
                    addItemsToAnnuitetCalculateSheet(calculateSheet, writableCellFormatMap);
                }
                // Дифференцированный
                else{
                    addItemsToDifferentiatedCalculateSheet(calculateSheet, writableCellFormatMap);
                }
            }

            // Close workbook
            workbook.write();
            workbook.close();

            new AlertDialog.Builder(getActivity())
                    .setTitle(file.getName())
                    .setMessage("Файл успешно сохранён в папке" + System.getProperty("line.separator") + dir.getAbsolutePath())
                    .setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Uri uri = FileProvider.getUriForFile(getActivity(), "kz.algakzru.hcsbk_calculator.fileprovider", file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri,"application/vnd.ms-excel");
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage("На вашем устройстве не установлен Excel редактор").setNegativeButton("OK", null).show();
                            } catch (Exception e) {
                                new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
                            }
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
    }

    private Map<String,WritableCellFormat> createWritableCellFormatMap() throws Exception {

        Map<String, WritableCellFormat> writableCellFormatMap = new HashMap<String, WritableCellFormat>();

        // Borderd cell format
        {
            WritableCellFormat writableCellFormat = new WritableCellFormat();
            writableCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            writableCellFormatMap.put(BORDER_FORMAT, writableCellFormat);
        }

        // Borderd cell format with center alignment
        {
            WritableCellFormat centreFromat = new WritableCellFormat();
            centreFromat.setAlignment(Alignment.CENTRE);
            centreFromat.setBorder(Border.ALL, BorderLineStyle.THIN);
            writableCellFormatMap.put(CENTRE_FORMAT, centreFromat);
        }

        // Percent format
        {
            WritableCellFormat percentFormat = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
            percentFormat.setAlignment(Alignment.CENTRE);
            percentFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            writableCellFormatMap.put(PERCENT_FORMAT, percentFormat);
        }

        // Accounting format
        {
            WritableCellFormat tengeFormat = new WritableCellFormat(new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT));
            tengeFormat.setAlignment(Alignment.CENTRE);
            tengeFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            writableCellFormatMap.put(TENGE_FORMAT, tengeFormat);
        }

        // Yellow accounting format
        {
            WritableCellFormat yellowTengeFormat = new WritableCellFormat(new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT));
            yellowTengeFormat.setAlignment(Alignment.CENTRE);
            yellowTengeFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            yellowTengeFormat.setBackground(Colour.VERY_LIGHT_YELLOW);
            writableCellFormatMap.put(YELLOW_TENGE_FORMAT, yellowTengeFormat);
        }

        // Green accounting format
        {
            WritableCellFormat greenTengeFormat = new WritableCellFormat(new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT));
            greenTengeFormat.setAlignment(Alignment.CENTRE);
            greenTengeFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            greenTengeFormat.setBackground(Colour.LIGHT_GREEN);
            writableCellFormatMap.put(GREEN_TENGE_FORMAT, greenTengeFormat);
        }

        // Date format
        {
            WritableCellFormat dateFormat = new WritableCellFormat(new DateFormat("yyyy-MM-dd"));
            dateFormat.setAlignment(Alignment.CENTRE);
            dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            writableCellFormatMap.put(DATE_FORMAT, dateFormat);
        }

        // Create a bold font
        {
            WritableCellFormat blueTitleFormat = new WritableCellFormat();
            blueTitleFormat.setAlignment(Alignment.CENTRE);
            blueTitleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            blueTitleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            blueTitleFormat.setBackground(Colour.PALE_BLUE);
            blueTitleFormat.setWrap(true);
            writableCellFormatMap.put(BLUE_TITLE_FORMAT, blueTitleFormat);
        }

        return writableCellFormatMap;
    }

    private void addItemsToParamsSheet(WritableSheet paramsSheet, Map<String, WritableCellFormat> writableCellFormatMap) throws Exception {
        paramsSheet.addCell(new Number(0, 0, Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString())), writableCellFormatMap.get(TENGE_FORMAT)));
        paramsSheet.addCell(new Label(1, 0, "Сумма кредита", writableCellFormatMap.get(BORDER_FORMAT)));
        paramsSheet.addCell(new Number(0, 1, Double.parseDouble(etSrokCredita.getText().toString()), writableCellFormatMap.get(CENTRE_FORMAT)));
        paramsSheet.addCell(new Label(1, 1, "Кол-во расчётных периодов (срок кредита)", writableCellFormatMap.get(BORDER_FORMAT)));
        paramsSheet.addCell(new Number(0, 2, Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d, writableCellFormatMap.get(PERCENT_FORMAT)));
        paramsSheet.addCell(new Label(1, 2, "Процентная ставка годовая", writableCellFormatMap.get(BORDER_FORMAT)));
        paramsSheet.addCell(new DateTime(0, 3, sdf.parse(etDataVydachiCredita.getText().toString()), writableCellFormatMap.get(DATE_FORMAT)));
        paramsSheet.addCell(new Label(1, 3, "Дата выдачи кредита", writableCellFormatMap.get(BORDER_FORMAT)));
        paramsSheet.addCell(new DateTime(0, 4, sdf.parse(etDataPervogoPlatezha.getText().toString()), writableCellFormatMap.get(DATE_FORMAT)));
        paramsSheet.addCell(new Label(1, 4, "Дата первого платежа", writableCellFormatMap.get(BORDER_FORMAT)));
        paramsSheet.addCell(new Label(0, 5, spPaymentType.getSelectedItem().toString(), writableCellFormatMap.get(CENTRE_FORMAT)));
        paramsSheet.addCell(new Label(1, 5, "Вид платежа", writableCellFormatMap.get(BORDER_FORMAT)));

        // CellView Auto-Size
        sheetAutoFitColumns(paramsSheet);
        CellView cellView = paramsSheet.getColumnView(1);
        cellView.setAutosize(true);
        paramsSheet.setColumnView(1, cellView);
    }

    private void addItemsToAnnuitetCalculateSheet(WritableSheet calculateSheet, Map<String, WritableCellFormat> writableCellFormatMap) throws Exception {
        int firstRow = 11;
        int srokCredita = Integer.parseInt(etSrokCredita.getText().toString());
        calculateSheet.mergeCells(1, 0, 6, 0);
        calculateSheet.mergeCells(1, 1, 6, 1);
        calculateSheet.mergeCells(1, 2, 6, 2);
        calculateSheet.mergeCells(1, 3, 6, 3);
        calculateSheet.mergeCells(1, 4, 6, 4);
        calculateSheet.addCell(new Formula(0, 0, "'Параметры'!A1*((1+A3)*(1+A4)*POWER(1+A5,'Параметры'!A2-2))/(1+SUM(G" + firstRow + ":G" + (firstRow+srokCredita-1) + "))", writableCellFormatMap.get(GREEN_TENGE_FORMAT)));
        calculateSheet.addCell(new Label(1, 0, "Ежемесячный платёж", writableCellFormatMap.get(BORDER_FORMAT)));
        calculateSheet.addCell(new Formula(0, 1, "SUM(C" + firstRow + ":C" + (firstRow+srokCredita-1) + ")", writableCellFormatMap.get(YELLOW_TENGE_FORMAT)));
        calculateSheet.addCell(new Label(1, 1, "Переплата", writableCellFormatMap.get(BORDER_FORMAT)));
        calculateSheet.addCell(new Formula(0, 2, "'Параметры'!A3*F" + firstRow + "/360", writableCellFormatMap.get(PERCENT_FORMAT)));
        calculateSheet.addCell(new Label(1, 2, "Процентная ставка в первом расчётном периоде", writableCellFormatMap.get(BORDER_FORMAT)));
        calculateSheet.addCell(new Formula(0, 3, "'Параметры'!A3*F" + (firstRow+srokCredita-1) + "/360", writableCellFormatMap.get(PERCENT_FORMAT)));
        calculateSheet.addCell(new Label(1, 3, "Процентная ставка в последнем расчётном периоде", writableCellFormatMap.get(BORDER_FORMAT)));
        calculateSheet.addCell(new Formula(0, 4, "'Параметры'!A3/12", writableCellFormatMap.get(PERCENT_FORMAT)));
        calculateSheet.addCell(new Label(1, 4, "Процентная ставка в остальных расчётных периодах", writableCellFormatMap.get(BORDER_FORMAT)));

        // Add blue titles
        {
            int row1 = firstRow - 3;
            int row2 = firstRow - 2;
            calculateSheet.mergeCells(0, row1, 0, row2);
            calculateSheet.mergeCells(1, row1, 1, row2);
            calculateSheet.mergeCells(2, row1, 2, row2);
            calculateSheet.mergeCells(3, row1, 3, row2);
            calculateSheet.mergeCells(4, row1, 4, row2);
            calculateSheet.mergeCells(5, row1, 5, row2);
            calculateSheet.mergeCells(6, row1, 6, row2);
            calculateSheet.addCell(new Label(0, row1, "Расчётный период", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(1, row1, "Дата погашения", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(2, row1, "Погашение вознаграждения", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(3, row1, "Погашение основного долга", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(4, row1, "Остаток " + System.getProperty("line.separator") +  " основного долга", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(5, row1, "Колличество дней", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(6, row1, "Коэффициент", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            autoSize(calculateSheet, 0, "Расчётный");
            autoSize(calculateSheet, 1, "погашения");
            autoSize(calculateSheet, 2, "вознаграждения");
            autoSize(calculateSheet, 3, "основного долга");
            autoSize(calculateSheet, 4, "основного долга");
            autoSize(calculateSheet, 5, "Колличество");
            autoSize(calculateSheet, 6, "Коэффициент");
        }

        for (int i=1; i <= srokCredita; i++) {
            int currentRow = firstRow - 2 + i;
            // first row
            if (i == 1) {
                calculateSheet.addCell(new Number(0, currentRow, i, writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(1, currentRow, "'Параметры'!A5", writableCellFormatMap.get(DATE_FORMAT)));
                calculateSheet.addCell(new Formula(4, currentRow, "'Параметры'!A1", writableCellFormatMap.get(TENGE_FORMAT)));
                calculateSheet.addCell(new Formula(5, currentRow, "DAYS('Параметры'!A4,'Параметры'!A5,TRUE)", writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(6, currentRow, "(1+A4)*POWER(1+A5, A" + (firstRow - 1 + i) + "-1)", writableCellFormatMap.get(CENTRE_FORMAT)));
            }
            // last row
            else if (i == srokCredita) {
                calculateSheet.addCell(new Number(0, currentRow, i, writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(1, currentRow, "DATE(YEAR('Параметры'!A4), MONTH('Параметры'!A4) + " + srokCredita + ", DAY('Параметры'!A4))", writableCellFormatMap.get(DATE_FORMAT)));
                calculateSheet.addCell(new Formula(4, currentRow, "E" + (firstRow - 2 + i) + "-D" + (firstRow - 2 + i), writableCellFormatMap.get(TENGE_FORMAT)));
                calculateSheet.addCell(new Formula(5, currentRow, "DAYS(B" + currentRow + ",B" + (currentRow + 1) + ",TRUE)", writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Label(6, currentRow, "", writableCellFormatMap.get(CENTRE_FORMAT)));
            }
            // other rows
            else {
                calculateSheet.addCell(new Number(0, currentRow, i, writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(1, currentRow, "DATE(YEAR(B" + firstRow  + "), MONTH(B" + firstRow  + ") + " + (i-1) + ", DAY(B" + firstRow  + "))", writableCellFormatMap.get(DATE_FORMAT)));
                calculateSheet.addCell(new Formula(4, currentRow, "E" + (firstRow - 2 + i) + "-D" + (firstRow - 2 + i), writableCellFormatMap.get(TENGE_FORMAT)));
                calculateSheet.addCell(new Formula(5, currentRow, "DAYS(B" + currentRow + ",B" + (currentRow + 1) + ",TRUE)", writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(6, currentRow, "(1+A4)*POWER(1+A5, A" + (firstRow - 1 + i) + "-1)", writableCellFormatMap.get(CENTRE_FORMAT)));
            }
            calculateSheet.addCell(new Formula(2, currentRow, "E" + (firstRow - 1 + i) + "*'Параметры'!A3*F" + (firstRow - 1 + i) + "/360", writableCellFormatMap.get(YELLOW_TENGE_FORMAT)));
            calculateSheet.addCell(new Formula(3, currentRow, "A1-C" + (firstRow - 1 + i), writableCellFormatMap.get(TENGE_FORMAT)));
        }
    }

    private void addItemsToDifferentiatedCalculateSheet(WritableSheet calculateSheet, Map<String, WritableCellFormat> writableCellFormatMap) throws Exception {
        int firstRow = 3;
        int srokCredita = Integer.parseInt(etSrokCredita.getText().toString());

        // Add blue titles
        {
            int row1 = firstRow - 3;
            int row2 = firstRow - 2;
            calculateSheet.mergeCells(0, row1, 0, row2);
            calculateSheet.mergeCells(1, row1, 1, row2);
            calculateSheet.mergeCells(2, row1, 2, row2);
            calculateSheet.mergeCells(3, row1, 4, row1);
            calculateSheet.mergeCells(5, row1, 5, row2);
            calculateSheet.mergeCells(6, row1, 6, row2);
            calculateSheet.addCell(new Label(0, row1, "Расчётный период", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(1, row1, "Дата погашения", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(2, row1, "Сумма платежа", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(3, row1, "Погашение", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(3, row2, "Процентов банка", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(4, row2, "Основного долга", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(5, row1, "Остаток " + System.getProperty("line.separator") +  " основного долга", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            calculateSheet.addCell(new Label(6, row1, "Колличество дней", writableCellFormatMap.get(BLUE_TITLE_FORMAT)));
            autoSize(calculateSheet, 0, "Расчётный");
            autoSize(calculateSheet, 1, "погашения");
            autoSize(calculateSheet, 2, "Сумма платежа");
            autoSize(calculateSheet, 3, "Процентов банка");
            autoSize(calculateSheet, 4, "Основного долга");
            autoSize(calculateSheet, 5, "основного долга");
            autoSize(calculateSheet, 6, "Колличество");
        }

        for (int i=1; i <= srokCredita; i++) {
            int currentRow = firstRow - 2 + i;
            // first row
            if (i == 1) {
                calculateSheet.addCell(new Number(0, currentRow, i, writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(1, currentRow, "'Параметры'!A5", writableCellFormatMap.get(DATE_FORMAT)));
                calculateSheet.addCell(new Formula(5, currentRow, "'Параметры'!A1", writableCellFormatMap.get(TENGE_FORMAT)));
                calculateSheet.addCell(new Formula(6, currentRow, "DAYS('Параметры'!A4,'Параметры'!A5,TRUE)", writableCellFormatMap.get(CENTRE_FORMAT)));
            }
            // last row
            else if (i == srokCredita) {
                calculateSheet.addCell(new Number(0, currentRow, i, writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(1, currentRow, "DATE(YEAR('Параметры'!A4), MONTH('Параметры'!A4) + " + srokCredita + ", DAY('Параметры'!A4))", writableCellFormatMap.get(DATE_FORMAT)));
                calculateSheet.addCell(new Formula(5, currentRow, "F" + (firstRow - 2 + i) + "-E" + (firstRow - 2 + i), writableCellFormatMap.get(TENGE_FORMAT)));
                calculateSheet.addCell(new Formula(6, currentRow, "DAYS(B" + currentRow + ",B" + (currentRow + 1) + ",TRUE)", writableCellFormatMap.get(CENTRE_FORMAT)));
            }
            // other rows
            else {
                calculateSheet.addCell(new Number(0, currentRow, i, writableCellFormatMap.get(CENTRE_FORMAT)));
                calculateSheet.addCell(new Formula(1, currentRow, "DATE(YEAR(B" + firstRow  + "), MONTH(B" + firstRow  + ") + " + (i-1) + ", DAY(B" + firstRow  + "))", writableCellFormatMap.get(DATE_FORMAT)));
                calculateSheet.addCell(new Formula(5, currentRow, "F" + (firstRow - 2 + i) + "-E" + (firstRow - 2 + i), writableCellFormatMap.get(TENGE_FORMAT)));
                calculateSheet.addCell(new Formula(6, currentRow, "DAYS(B" + currentRow + ",B" + (currentRow + 1) + ",TRUE)", writableCellFormatMap.get(CENTRE_FORMAT)));
            }
            calculateSheet.addCell(new Formula(2, currentRow, "D" + (firstRow - 1 + i) + "+E" + (firstRow - 1 + i), writableCellFormatMap.get(GREEN_TENGE_FORMAT)));
            calculateSheet.addCell(new Formula(3, currentRow, "F" + (firstRow - 1 + i) + "*'Параметры'!A3*G" + (firstRow - 1 + i) + "/360", writableCellFormatMap.get(YELLOW_TENGE_FORMAT)));
            calculateSheet.addCell(new Formula(4, currentRow, "'Параметры'!A1/'Параметры'!A2", writableCellFormatMap.get(TENGE_FORMAT)));
        }

        // Pereplata row
        {
            int pereplataRow = firstRow + srokCredita - 1;
            calculateSheet.mergeCells(0, pereplataRow, 2, pereplataRow);
            calculateSheet.addCell(new Label(0, pereplataRow, "Итого переплата:", writableCellFormatMap.get(CENTRE_FORMAT)));
            calculateSheet.addCell(new Formula(3, pereplataRow, "SUM(D" + firstRow + ":D" + (firstRow+srokCredita-1) + ")", writableCellFormatMap.get(YELLOW_TENGE_FORMAT)));
        }
    }

    private void sheetAutoFitColumns(WritableSheet writableSheet) throws Exception {

        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cellList.add(writableSheet.getCell(0, i));
        }

        /* Find the widest cell in the column. */
        int longestStrLen = -1;
        for (int i = 0; i < cellList.size(); i++) {
            Cell cell = cellList.get(i);
            String str = "";
            if (CellType.NUMBER == cell.getType()) {
                NumberCell numberCell = (NumberCell) cell;
                str = new DecimalFormat(ACCOUNTING_FLOAT).format(numberCell.getValue());
            } else if (CellType.DATE == cell.getType()) {
                DateCell dateCell = (DateCell) cell;
                str = sdf.format(dateCell.getDate());
            } else if (CellType.LABEL == cell.getType()) {
                LabelCell labelCell = (LabelCell) cell;
                str = labelCell.getString().toString();
            }

            if (str.length() > longestStrLen ) {
                if (TextUtils.isEmpty(str)) continue;
                longestStrLen = str.length();
            }
        }

        /* If not found, skip the column. */
        if (longestStrLen == -1) return;

        /* If wider than the max width, crop width */
        if (longestStrLen > 255) longestStrLen = 255;

        writableSheet.setColumnView(0, longestStrLen + 2);
    }

    private void autoSize(WritableSheet writableSheet, int columnNum, String str) throws Exception {

        int strLen = str.length() + 2;

        /* If wider than the max width, crop width */
        if (strLen > 255) strLen = 255;

        writableSheet.setColumnView(columnNum, strLen);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void calculate() {
        try {
            validateEditText();

            List<ListItem> listItems = new ArrayList<ListItem>();

            // Аннуитетный
            if (0 == spPaymentType.getSelectedItemId()) {
                double procentPervogoRaschetnogoPerioda = getProcentPervogoRaschetnogoPerioda();
                double procentPoslednegoRaschetnogoPerioda = getProcentPoslednegoRaschetnogoPerioda();
                double procentEzhemesiachnyi = getProcentEzhemesiachnyi();
                double up = getUp(procentPervogoRaschetnogoPerioda, procentPoslednegoRaschetnogoPerioda, procentEzhemesiachnyi);
                double down = getDown(procentPoslednegoRaschetnogoPerioda, procentEzhemesiachnyi);
                double annuitet = getAnnuitet(up, down);
                double pereplata = getPereplata(annuitet);

                listItems.add(new ListItem("Переплата:", pereplata));
                listItems.add(new ListItem("Ежемесячный платёж:", annuitet));
            }
            // Дифференцированный
            else {
                double pereplata = 0d;
                double pogashenieOsnovnogoDolga = getPogashenieOsnovnogoDolga();
                int srokCredita = Integer.parseInt(etSrokCredita.getText().toString());
                double ostatokOsnovnogoDolga = Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString()));
                for (int i=1; i <= srokCredita; i++) {
                    double procentRaschetnogoPerioda;
                    LocalDate dataPlatezha;
                    // first payment
                    if (1 == i) {
                        procentRaschetnogoPerioda = getProcentPervogoRaschetnogoPerioda();
                        dataPlatezha = new LocalDate(etDataPervogoPlatezha.getText().toString());
                    }
                    // last row
                    else if (i == srokCredita) {
                        procentRaschetnogoPerioda = getProcentPoslednegoRaschetnogoPerioda();
                        LocalDate dataVydachiCredita = new LocalDate(etDataVydachiCredita.getText().toString());
                        dataPlatezha = dataVydachiCredita.plusMonths(srokCredita);
                    }
                    // other payments
                    else {
                        procentRaschetnogoPerioda = getProcentObichnogoRaschetnogoPerioda(i);
                        LocalDate dataPervogoPlatezha = new LocalDate(etDataPervogoPlatezha.getText().toString());
                        dataPlatezha = dataPervogoPlatezha.plusMonths(i - 1);
                    }
                    double procentBanka = ostatokOsnovnogoDolga * procentRaschetnogoPerioda;
                    pereplata += procentBanka;
                    listItems.add(new ListItem("Сумма платежа за " + dataPlatezha.toString("yyyy-MM-dd") + ":", procentBanka + pogashenieOsnovnogoDolga));
                    ostatokOsnovnogoDolga = ostatokOsnovnogoDolga - pogashenieOsnovnogoDolga;
                }

                listItems.add(0, new ListItem("Переплата:", pereplata));
            }

            CustomAdapter customAdapter = new CustomAdapter(getActivity(), listItems);
            new AlertDialog.Builder(getActivity())
                    .setTitle("Итого")
                    .setAdapter(customAdapter, null)
                    .setPositiveButton("OK", null)
                    .show();
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
    }

    private double getPogashenieOsnovnogoDolga() throws Exception {
        double summaCreadita = Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString()));
        double srokCredita = Double.parseDouble(etSrokCredita.getText().toString());
        return summaCreadita / srokCredita;
    }

    private double getPereplata(double annuitet) throws Exception {
        double summaCreadita = Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString()));
        double srokCredita = Double.parseDouble(etSrokCredita.getText().toString());
        return (annuitet * srokCredita) - summaCreadita;
    }

    private double getAnnuitet(double up, double down) throws Exception {
        double summaCreadita = Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString()));
        return summaCreadita * up / down;
    }

    private double getUp(double procentPervogoRaschetnogoPerioda, double procentPoslednegoRaschetnogoPerioda, double procentEzhemesiachnyi) throws Exception {
        int power = Integer.parseInt(etSrokCredita.getText().toString()) - 2;
        return (1d + procentPervogoRaschetnogoPerioda) * Math.pow(1d + procentEzhemesiachnyi, power) * (1d + procentPoslednegoRaschetnogoPerioda);
    }

    private double getDown(double procentPoslednegoRaschetnogoPerioda, double procentEzhemesiachnyi) throws Exception {
        double down = 1d;
        int iMax = Integer.parseInt(etSrokCredita.getText().toString()) - 1;
        for (int i=1; i <= iMax; i++) {
            double test = (1d + procentPoslednegoRaschetnogoPerioda) * Math.pow(1d + procentEzhemesiachnyi, i-1);
            down += test;
        }
        return down;
    }

    private double getProcentPervogoRaschetnogoPerioda() throws Exception {
        LocalDate firstDate = new LocalDate(etDataVydachiCredita.getText().toString());
        LocalDate secondDate = new LocalDate(etDataPervogoPlatezha.getText().toString());
        double yearFraction = calculateYearFraction(firstDate, secondDate);
        double procentnayaStavka = Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d;
        return yearFraction * procentnayaStavka;
    }

    private double getProcentPoslednegoRaschetnogoPerioda() throws Exception {
        LocalDate dataVydachiCredita = new LocalDate(etDataVydachiCredita.getText().toString());
        LocalDate dataPervogoPlatezha = new LocalDate(etDataPervogoPlatezha.getText().toString());
        LocalDate firstDate = dataPervogoPlatezha.plusMonths(Integer.parseInt(etSrokCredita.getText().toString())-2);
        LocalDate secondDate = dataVydachiCredita.plusMonths(Integer.parseInt(etSrokCredita.getText().toString()));
        double yearFraction = calculateYearFraction(firstDate, secondDate);
        double procentnayaStavka = Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d;

        return yearFraction * procentnayaStavka;
    }

    private double getProcentEzhemesiachnyi() throws Exception {
        double procentnayaStavka = Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d;
        return procentnayaStavka / 12d;
    }

    private double getProcentObichnogoRaschetnogoPerioda(int raschiotnyPeriod) throws Exception {
        LocalDate dataPervogoPlatezha = new LocalDate(etDataPervogoPlatezha.getText().toString());
        LocalDate firstDate = dataPervogoPlatezha.plusMonths(raschiotnyPeriod - 2);
        LocalDate secondDate = dataPervogoPlatezha.plusMonths(raschiotnyPeriod - 1);
        double yearFraction = calculateYearFraction(firstDate, secondDate);
        double procentnayaStavka = Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d;

        return yearFraction * procentnayaStavka;
    }

    // E thirty day months / 360 ("30E/360")
    // https://stackoverflow.com/questions/28277833/how-to-create-a-bank-calendar-with-30-days-each-month
    // https://github.com/OpenGamma/OG-Commons/blob/master/modules/basics/src/main/java/com/opengamma/basics/date/StandardDayCounts.java#L266-L284
    public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate) throws Exception {
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
    private double thirty360(int y1, int m1, int d1, int y2, int m2, int d2) throws Exception {
        return (360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1)) / 360d;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

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
import android.widget.ArrayAdapter;
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
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.DateCell;
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
import kz.algakzru.hcsbk_calculator.utils.CustomTextWatcher;
import kz.algakzru.hcsbk_calculator.utils.NumberTextWatcherForThousand;

public class CreditFragment extends Fragment {

    private Spinner spCreditType;
    private EditText etSummaCredita;
    private EditText etSrokCredita;
    private EditText etProcentnayaStavka;
    private EditText etDataVydachiCredita;
    private EditText etDataPervogoPlatezha;
    public EditText etEzhemesiachnyiPlatezh;
    public EditText etPereplata;
    private Button btnCalculate;
    private Button btnExport;

    private Calendar calendar = Calendar.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final String ACCOUNTING_FLOAT = "#,##0.00тг";
    private final String DATE_FORMAT = "yyyy-MM-dd";

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

        spCreditType = (Spinner) view.findViewById(R.id.sp_credit_type);
        etSummaCredita = (EditText) view.findViewById(R.id.et_summa_credita);
        etSrokCredita = (EditText) view.findViewById(R.id.et_srok_credita);
        etProcentnayaStavka = (EditText) view.findViewById(R.id.et_procentnaya_stavka);
        etDataVydachiCredita = (EditText) view.findViewById(R.id.et_data_vydachi_credita);
        etDataPervogoPlatezha = (EditText) view.findViewById(R.id.et_data_pervogo_platezha);
        etEzhemesiachnyiPlatezh = (EditText) view.findViewById(R.id.et_ezhemesiachnyi_platezh);
        etPereplata = (EditText) view.findViewById(R.id.et_pereplata);
        btnCalculate = (Button) view.findViewById(R.id.btn_calculate);
        btnExport = (Button) view.findViewById(R.id.btn_export);

        setListeners();
        setText();

        return view;
    }

    private void setText() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        etSummaCredita.setText(sharedPref.getString((String) etSummaCredita.getTag(), ""));
        etSrokCredita.setText(sharedPref.getString((String) etSrokCredita.getTag(), ""));
        etProcentnayaStavka.setText(sharedPref.getString((String) etProcentnayaStavka.getTag(), ""));
        etDataVydachiCredita.setText(sharedPref.getString((String) etDataVydachiCredita.getTag(), ""));
        etDataPervogoPlatezha.setText(sharedPref.getString((String) etDataPervogoPlatezha.getTag(), ""));
    }

    private void setListeners() {

        spCreditType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    etEzhemesiachnyiPlatezh.setText(null);
                    etPereplata.setText(null);
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etSummaCredita.addTextChangedListener(new NumberTextWatcherForThousand(etSummaCredita, this));

        etSrokCredita.addTextChangedListener(new CustomTextWatcher(etSrokCredita, this));

        etProcentnayaStavka.addTextChangedListener(new CustomTextWatcher(etProcentnayaStavka, this));

        etDataVydachiCredita.addTextChangedListener(new CustomTextWatcher(etDataVydachiCredita, this));

        etDataPervogoPlatezha.addTextChangedListener(new CustomTextWatcher(etDataPervogoPlatezha, this));

        etEzhemesiachnyiPlatezh.addTextChangedListener(new NumberTextWatcherForThousand(etEzhemesiachnyiPlatezh, this));

        etPereplata.addTextChangedListener(new NumberTextWatcherForThousand(etPereplata, this));

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

            // Borderd cell format
            WritableCellFormat borderFormat = new WritableCellFormat();
            borderFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Borderd cell format with center alignment
            WritableCellFormat centreFromat = new WritableCellFormat();
            centreFromat.setAlignment(Alignment.CENTRE);
            centreFromat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Percent format
            WritableCellFormat percentFormat = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
            percentFormat.setAlignment(Alignment.CENTRE);
            percentFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Accounting format
            WritableCellFormat tengeFormat = new WritableCellFormat(new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT));
            tengeFormat.setAlignment(Alignment.CENTRE);
            tengeFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Yellow accounting format
            WritableCellFormat yellowTengeFormat = new WritableCellFormat(new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT));
            yellowTengeFormat.setAlignment(Alignment.CENTRE);
            yellowTengeFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            yellowTengeFormat.setBackground(Colour.VERY_LIGHT_YELLOW);

            // Green accounting format
            WritableCellFormat greenTengeFormat = new WritableCellFormat(new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT));
            greenTengeFormat.setAlignment(Alignment.CENTRE);
            greenTengeFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            greenTengeFormat.setBackground(Colour.LIGHT_GREEN);

            // Date format
            WritableCellFormat dateFormat = new WritableCellFormat(new DateFormat(DATE_FORMAT));
            dateFormat.setAlignment(Alignment.CENTRE);
            dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Column and row titles
            paramsSheet.addCell(new Number(0, 0, Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(etSummaCredita.getText().toString())), tengeFormat));
            paramsSheet.addCell(new Label(1, 0, "Сумма кредита", borderFormat));
            paramsSheet.addCell(new Number(0, 1, Double.parseDouble(etSrokCredita.getText().toString()), centreFromat));
            paramsSheet.addCell(new Label(1, 1, "Кол-во расчётных периодов (срок кредита)", borderFormat));
            paramsSheet.addCell(new Number(0, 2, Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d, percentFormat));
            paramsSheet.addCell(new Label(1, 2, "Процентная ставка годовая", borderFormat));
            paramsSheet.addCell(new DateTime(0, 3, sdf.parse(etDataVydachiCredita.getText().toString()), dateFormat));
            paramsSheet.addCell(new Label(1, 3, "Дата выдачи кредита", borderFormat));
            paramsSheet.addCell(new DateTime(0, 4, sdf.parse(etDataPervogoPlatezha.getText().toString()), dateFormat));
            paramsSheet.addCell(new Label(1, 4, "Дата первого платежа", borderFormat));

            int firstRow = 11;
            int srokCredita = Integer.parseInt(etSrokCredita.getText().toString());
            calculateSheet.mergeCells(1, 0, 6, 0);
            calculateSheet.mergeCells(1, 1, 6, 1);
            calculateSheet.mergeCells(1, 2, 6, 2);
            calculateSheet.mergeCells(1, 3, 6, 3);
            calculateSheet.mergeCells(1, 4, 6, 4);
            calculateSheet.addCell(new Formula(0, 0, "'Параметры'!A1*((1+A3)*(1+A4)*POWER(1+A5,'Параметры'!A2-2))/(1+SUM(G" + firstRow + ":G" + (firstRow+srokCredita-1) + "))", greenTengeFormat));
            calculateSheet.addCell(new Label(1, 0, "Ежемесячный платёж", borderFormat));
            calculateSheet.addCell(new Formula(0, 1, "SUM(C" + firstRow + ":C" + (firstRow+srokCredita-1) + ")", yellowTengeFormat));
            calculateSheet.addCell(new Label(1, 1, "Переплата", borderFormat));
            calculateSheet.addCell(new Formula(0, 2, "'Параметры'!A3*F" + firstRow + "/360", percentFormat));
            calculateSheet.addCell(new Label(1, 2, "Процентная ставка в первом расчётном периоде", borderFormat));
            calculateSheet.addCell(new Formula(0, 3, "'Параметры'!A3*F" + (firstRow+srokCredita-1) + "/360", percentFormat));
            calculateSheet.addCell(new Label(1, 3, "Процентная ставка в последнем расчётном периоде", borderFormat));
            calculateSheet.addCell(new Formula(0, 4, "'Параметры'!A3/12", percentFormat));
            calculateSheet.addCell(new Label(1, 4, "Процентная ставка в остальных расчётных периодах", borderFormat));

            addTitles(calculateSheet, firstRow);
            for (int i=1; i <= srokCredita; i++) {
                int currentRow = firstRow - 2 + i;
                // first row
                if (i == 1) {
                    calculateSheet.addCell(new Number(0, currentRow, i, centreFromat));
                    calculateSheet.addCell(new Formula(1, currentRow, "'Параметры'!A5", dateFormat));
                    calculateSheet.addCell(new Formula(4, currentRow, "'Параметры'!A1", tengeFormat));
                    calculateSheet.addCell(new Formula(5, currentRow, "DAYS('Параметры'!A4,'Параметры'!A5)", centreFromat));
                    calculateSheet.addCell(new Formula(6, currentRow, "(1+A4)*POWER(1+A5, A" + (firstRow - 1 + i) + "-1)", centreFromat));
                } else
                // last row
                if (i == srokCredita) {
                    calculateSheet.addCell(new Number(0, currentRow, i, centreFromat));
                    calculateSheet.addCell(new Formula(1, currentRow, "DATE(YEAR('Параметры'!A4), MONTH('Параметры'!A4) + " + srokCredita + ", DAY('Параметры'!A4))", dateFormat));
                    calculateSheet.addCell(new Formula(4, currentRow, "E" + (firstRow - 2 + i) + "-D" + (firstRow - 2 + i), tengeFormat));
                    calculateSheet.addCell(new Formula(5, currentRow, "DAYS(B" + currentRow + ",B" + (currentRow + 1) + ")", centreFromat));
                    calculateSheet.addCell(new Label(6, currentRow, "", centreFromat));
                }
                // other rows
                else {
                    calculateSheet.addCell(new Number(0, currentRow, i, centreFromat));
                    calculateSheet.addCell(new Formula(1, currentRow, "DATE(YEAR(B" + firstRow  + "), MONTH(B" + firstRow  + ") + " + (i-1) + ", DAY(B" + firstRow  + "))", dateFormat));
                    calculateSheet.addCell(new Formula(4, currentRow, "E" + (firstRow - 2 + i) + "-D" + (firstRow - 2 + i), tengeFormat));
                    calculateSheet.addCell(new Formula(5, currentRow, "DAYS(B" + currentRow + ",B" + (currentRow + 1) + ")", centreFromat));
                    calculateSheet.addCell(new Formula(6, currentRow, "(1+A4)*POWER(1+A5, A" + (firstRow - 1 + i) + "-1)", centreFromat));
                }
                calculateSheet.addCell(new Formula(2, currentRow, "E" + (firstRow - 1 + i) + "*'Параметры'!A3*F" + (firstRow - 1 + i) + "/360", yellowTengeFormat));
                calculateSheet.addCell(new Formula(3, currentRow, "A1-C" + (firstRow - 1 + i), tengeFormat));
            }

            // CellView Auto-Size
            sheetAutoFitColumns(paramsSheet);
            CellView cellView = paramsSheet.getColumnView(1);
            cellView.setAutosize(true);
            paramsSheet.setColumnView(1, cellView);

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

    private void addTitles(WritableSheet writableSheet, int firstRow) throws Exception {

        // Create a bold font
        WritableCellFormat titleFormat = new WritableCellFormat();
        titleFormat.setAlignment(Alignment.CENTRE);
        titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        titleFormat.setBackground(Colour.PALE_BLUE);
        titleFormat.setWrap(true);

        int row1 = firstRow - 3;
        int row2 = firstRow - 2;
        writableSheet.mergeCells(0, row1, 0, row2);
        writableSheet.mergeCells(1, row1, 1, row2);
        writableSheet.mergeCells(2, row1, 2, row2);
        writableSheet.mergeCells(3, row1, 3, row2);
        writableSheet.mergeCells(4, row1, 4, row2);
        writableSheet.mergeCells(5, row1, 5, row2);
        writableSheet.mergeCells(6, row1, 6, row2);
        writableSheet.addCell(new Label(0, row1, "Расчётный период", titleFormat));
        writableSheet.addCell(new Label(1, row1, "Дата погашения", titleFormat));
        writableSheet.addCell(new Label(2, row1, "Погашение вознаграждения", titleFormat));
        writableSheet.addCell(new Label(3, row1, "Погашение основного долга", titleFormat));
        writableSheet.addCell(new Label(4, row1, "Остаток " + System.getProperty("line.separator") +  " основного долга", titleFormat));
        writableSheet.addCell(new Label(5, row1, "Колличество дней", titleFormat));
        writableSheet.addCell(new Label(6, row1, "Коэффициент", titleFormat));
        autoSize(writableSheet, 0, "Расчётный");
        autoSize(writableSheet, 1, "погашения");
        autoSize(writableSheet, 2, "вознаграждения");
        autoSize(writableSheet, 3, "основного долга");
        autoSize(writableSheet, 4, "основного долга");
        autoSize(writableSheet, 5, "Колличество");
        autoSize(writableSheet, 6, "Коэффициент");
    }

    private void sheetAutoFitColumns(WritableSheet writableSheet) throws Exception {

        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
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

        CellView cv = writableSheet.getColumnView(0);
        cv.setSize(longestStrLen * 256 + 100); /* Every character is 256 units wide, so scale it. */
        writableSheet.setColumnView(0, cv);
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
            etEzhemesiachnyiPlatezh.setText(null);
            etPereplata.setText(null);
            validateEditText();

            double procentPervogoRaschetnogoPerioda = getProcentPervogoRaschetnogoPerioda();
            double procentPoslednegoRaschetnogoPerioda = getProcentPoslednegoRaschetnogoPerioda();
            double procentEzhemesiachnyi = getProcentEzhemesiachnyi();
            double up = getUp(procentPervogoRaschetnogoPerioda, procentPoslednegoRaschetnogoPerioda, procentEzhemesiachnyi);
            double down = getDown(procentPoslednegoRaschetnogoPerioda, procentEzhemesiachnyi);
            double annuitet = getAnnuitet(up, down);
            double pereplata = getPereplata(annuitet);

            etEzhemesiachnyiPlatezh.setText(String.format(Locale.US, "%.2f", annuitet));
            etPereplata.setText(String.format(Locale.US, "%.2f", pereplata));
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
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

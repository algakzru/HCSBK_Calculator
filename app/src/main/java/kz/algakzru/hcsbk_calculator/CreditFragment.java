package kz.algakzru.hcsbk_calculator;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.SheetSettings;
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
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.biff.FormulaRecord;

public class CreditFragment extends Fragment {

    private EditText etSummaCredita;
    private EditText etSrokCredita;
    private EditText etProcentnayaStavka;
    private EditText etDataVydachiCredita;
    private EditText etDataPervogoPlatezha;
    private EditText etEzhemesiachnyiPlatezh;
    private EditText etPereplata;
    private Button btnClean;
    private Button btnCalculate;
    private Button btnExport;

    private Calendar calendar = Calendar.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final String ACCOUNTING_FLOAT = "#,###.00тг";
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

        etSummaCredita = (EditText) view.findViewById(R.id.et_summa_credita);
        etSrokCredita = (EditText) view.findViewById(R.id.et_srok_credita);
        etProcentnayaStavka = (EditText) view.findViewById(R.id.et_procentnaya_stavka);
        etDataVydachiCredita = (EditText) view.findViewById(R.id.et_data_vydachi_credita);
        etDataPervogoPlatezha = (EditText) view.findViewById(R.id.et_data_pervogo_platezha);
        etEzhemesiachnyiPlatezh = (EditText) view.findViewById(R.id.et_ezhemesiachnyi_platezh);
        etPereplata = (EditText) view.findViewById(R.id.et_pereplata);
        btnClean = (Button) view.findViewById(R.id.btn_clean);
        btnCalculate = (Button) view.findViewById(R.id.btn_calculate);
        btnExport = (Button) view.findViewById(R.id.btn_export);

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
        etPereplata.setText(null);
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

    private void validateEditText() throws Exception {
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
        LocalDate dataVydachiCredita = new LocalDate(etDataVydachiCredita.getText().toString());
        LocalDate dataPervogoPlatezha = new LocalDate(etDataPervogoPlatezha.getText().toString());
        if (dataVydachiCredita.isAfter(dataPervogoPlatezha)){
            throw new Exception("Дата первого платежа не может быть меньше даты выдачи кредита");
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
//            File dir = new File(Environment.getExternalStorageDirectory(), "Documents");
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new Exception("Не удалось создать папку" + System.getProperty("line.separator") + dir.getAbsolutePath());
            }

            test(new File(dir, "Annuitet1.xls"));

            // Create Workbook
            final File file = new File(dir, "Annuitet.xls");
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale("ru","RU"));
            WritableWorkbook workbook = Workbook.createWorkbook(file, workbookSettings);

            // Excel sheetA first sheetA
            WritableSheet sheetAnnuitet = workbook.createSheet("Аннуитет", 0);
            WritableSheet sheetCalculate = workbook.createSheet("Вычесление", 1);

            // Create a center alignment
            WritableCellFormat alignmentCentre = new WritableCellFormat();
            alignmentCentre.setAlignment(Alignment.CENTRE);
            alignmentCentre.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat percentFloat = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
            percentFloat.setAlignment(Alignment.CENTRE);
            percentFloat.setBorder(Border.ALL, BorderLineStyle.THIN);

            NumberFormat euroSuffixCurrency = new NumberFormat(ACCOUNTING_FLOAT, NumberFormat.COMPLEX_FORMAT);
            WritableCellFormat euroSuffixFormat = new WritableCellFormat(euroSuffixCurrency);
            euroSuffixFormat.setAlignment(Alignment.CENTRE);
            euroSuffixFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Date format
            WritableCellFormat dateFormat = new WritableCellFormat(new DateFormat(DATE_FORMAT));
            dateFormat.setAlignment(Alignment.CENTRE);
            dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat borderFormat = new WritableCellFormat();
            borderFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Column and row titles
            sheetAnnuitet.addCell(new Number(0, 0, Double.parseDouble(etSummaCredita.getText().toString()), euroSuffixFormat));
            sheetAnnuitet.addCell(new Label(1, 0, "Сумма кредита", borderFormat));
            sheetAnnuitet.addCell(new Number(0, 1, Double.parseDouble(etSrokCredita.getText().toString()), alignmentCentre));
            sheetAnnuitet.addCell(new Label(1, 1, "Кол-во расчётных периодов (срок кредита)", borderFormat));
            sheetAnnuitet.addCell(new Number(0, 2, Double.parseDouble(etProcentnayaStavka.getText().toString()) / 100d, percentFloat));
            sheetAnnuitet.addCell(new Label(1, 2, "Процентная ставка годовая", borderFormat));
            sheetAnnuitet.addCell(new DateTime(0, 3, sdf.parse(etDataVydachiCredita.getText().toString()), dateFormat));
            sheetAnnuitet.addCell(new Label(1, 3, "Дата выдачи кредита", borderFormat));
            sheetAnnuitet.addCell(new DateTime(0, 4, sdf.parse(etDataPervogoPlatezha.getText().toString()), dateFormat));
            sheetAnnuitet.addCell(new Label(1, 4, "Дата первого платежа", borderFormat));
            sheetAnnuitet.addCell(new Number(0, 5, Double.parseDouble("94066.96"), euroSuffixFormat));
            sheetAnnuitet.addCell(new Label(1, 5, "Ежемесячный платёж", borderFormat));
            sheetAnnuitet.addCell(new Formula(0, 6, "a6*a2-a1", euroSuffixFormat));
            sheetAnnuitet.addCell(new Label(1, 6, "Переплата", borderFormat));

            sheetCalculate.addCell(new Number(0, 0, Double.parseDouble("0.00058"), percentFloat));
            sheetCalculate.addCell(new Label(1, 0, "Процентная ставка в первом расчётном периоде", borderFormat));
            sheetCalculate.addCell(new Number(0, 1, Double.parseDouble("0.00642"), percentFloat));
            sheetCalculate.addCell(new Label(1, 1, "Процентная ставка в последнем расчётном периоде", borderFormat));
            sheetCalculate.addCell(new Number(0, 2, Double.parseDouble("0.00350"), percentFloat));
            sheetCalculate.addCell(new Label(1, 2, "Процентная ставка в остальных расчётных периодах", borderFormat));

            addTitles(sheetCalculate);
            for (int i=0; i < Integer.parseInt(etSrokCredita.getText().toString()); i++) {
                if (i < Integer.parseInt(etSrokCredita.getText().toString()) - 1) {
                    sheetCalculate.addCell(new Number(0, 10+i, i+1, alignmentCentre));
                }
                if (i == 0) {
                    sheetCalculate.addCell(new Formula(1, 10+i, "'Аннуитет'!A5", dateFormat));
                } else {
                    sheetCalculate.addCell(new Formula(1, 10+i, "DATE(YEAR(B11), MONTH(B11) + " + i + ", DAY(B11))", dateFormat));
                    sheetCalculate.addCell(new FormulaRecord(2, 10+i, "DAYS360(B11,B12)"));
                }
            }

            // CellView Auto-Size
            sheetAutoFitColumns(sheetAnnuitet);
            CellView cellView = sheetAnnuitet.getColumnView(1);
            cellView.setAutosize(true);
            sheetAnnuitet.setColumnView(1, cellView);

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

    private void test(File file) throws Exception {
        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet(1);
        Cell cell = sheet.getCell(2, 11);
        if (CellType.NUMBER_FORMULA == cell.getType()) {
            NumberFormulaCell numberCell = (NumberFormulaCell) cell;
            String str = numberCell.getFormula().toString();
            Log.d("test", str);
        }
        Log.d("test", cell.getContents());
        Log.d("test", cell.getType().toString());
    }

    private void addTitles(WritableSheet writableSheet) throws Exception {

        // Create a bold font
        WritableCellFormat titleFormat = new WritableCellFormat();
        titleFormat.setAlignment(Alignment.CENTRE);
        titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        titleFormat.setBackground(Colour.PALE_BLUE);
        titleFormat.setWrap(true);

        writableSheet.mergeCells(0, 8, 0, 9);
        writableSheet.mergeCells(1, 8, 1, 9);
        writableSheet.mergeCells(2, 8, 2, 9);
        writableSheet.addCell(new Label(0, 8, "Расчётный период", titleFormat));
        writableSheet.addCell(new Label(1, 8, "Дата погашения", titleFormat));
        writableSheet.addCell(new Label(2, 8, "Колличество дней", titleFormat));
        autoSize(writableSheet, 0, "Расчётный");
        autoSize(writableSheet, 1, "погашения");
        autoSize(writableSheet, 2, "Колличество");
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

        int strLen = str.length() + 1;

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

            etEzhemesiachnyiPlatezh.setText(String.format("%.2f", annuitet));
            etPereplata.setText(String.format("%.2f", pereplata));
        } catch (Exception e) {
            new AlertDialog.Builder(getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
    }

    private double getPereplata(double annuitet) throws Exception {
        double summaCreadita = Double.parseDouble(etSummaCredita.getText().toString());
        double srokCredita = Double.parseDouble(etSrokCredita.getText().toString());
        return (annuitet * srokCredita) - summaCreadita;
    }

    private double getAnnuitet(double up, double down) throws Exception {
        double summaCreadita = Double.parseDouble(etSummaCredita.getText().toString());
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
        LocalDate firstDate = dataVydachiCredita.plusMonths(Integer.parseInt(etSrokCredita.getText().toString())-1).withDayOfMonth(dataPervogoPlatezha.getDayOfMonth());
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

package kz.algakzru.hcsbk_calculator.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.StringTokenizer;

/**
 * Created by Shreekrishna Ban on 12/14/2015.
 * This class is used For thousand seperator to the editText
 * This seperats the editText input with comma while user is entering the value

 * ----------------Extra features-----------------------------
 * Prevents wrinting that starts with "0" and adds "0." when "." pressed

 ----------------Method Lists-----------------------------
 >> beforeTextChanged 	=> 	@Override
 >> onTextChanged 		=> 	@Override
 >> afterTextChanged 	=> 	@Override

 >> getDecimalFormat 	=> 	gets decimal format of input string with comma as needed
 >> trimCommaOfString 	=> 	Trims comma of strings, Used by calling activity or fragment

 */
public class NumberTextWatcherForThousand implements TextWatcher {

    private EditText editText;
    private Fragment fragment;

    public NumberTextWatcherForThousand(EditText editText, Fragment fragment) {
        this.editText = editText;
        this.fragment = fragment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try
        {
            editText.removeTextChangedListener(this);
            String value = editText.getText().toString();

            if (value != null && !value.equals(""))
            {

                if(value.startsWith(".")){ //adds "0." when only "." is pressed on begining of writting
                    editText.setText("0.");
                }
                if(value.startsWith("0") && !value.startsWith("0.")){
                    editText.setText(""); //Prevents "0" while starting but not "0."

                }

                String str = editText.getText().toString().replaceAll(",", "");
                if (!value.equals(""))
//                    Double.valueOf(str).doubleValue();
                    editText.setText(getDecimalFormat(str));
                editText.setSelection(editText.getText().toString().length());
            }
            String tag = (String) editText.getTag();
            if (!TextUtils.isEmpty(tag)) {
                SharedPreferences sharedPreferences = fragment.getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(tag, trimCommaOfString(s.toString()));
                editor.commit();
            }
            editText.addTextChangedListener(this);
            return;
        }
        catch (Exception e)
        {
            new AlertDialog.Builder(fragment.getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
            editText.addTextChangedListener(this);
        }

    }

    public String getDecimalFormat(String value) throws Exception
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = ".";
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3)
            {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }

    //Trims all the comma of the string and returns
    public static String trimCommaOfString(String string) throws Exception {
//        String returnString;
        if(string.contains(",")){
            return string.replace(",","");}
        else {
            return string;
        }
    }
}

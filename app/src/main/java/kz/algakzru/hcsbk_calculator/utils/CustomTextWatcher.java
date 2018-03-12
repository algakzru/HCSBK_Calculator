package kz.algakzru.hcsbk_calculator.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import kz.algakzru.hcsbk_calculator.CreditFragment;

/**
 * Created by 816856 on 3/9/2018.
 */

public class CustomTextWatcher implements TextWatcher {

    private EditText editText;
    private CreditFragment fragment;

    public CustomTextWatcher(EditText editText, CreditFragment fragment) {
        this.editText = editText;
        this.fragment = fragment;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            fragment.etEzhemesiachnyiPlatezh.setText(null);
            fragment.etPereplata.setText(null);
            String tag = (String) editText.getTag();
            if (!TextUtils.isEmpty(tag)) {
                SharedPreferences sharedPreferences = fragment.getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(tag, editable.toString());
                editor.commit();
            }
        } catch (Exception e) {
            new AlertDialog.Builder(fragment.getActivity()).setTitle("Ошибка").setMessage(e.getMessage()).setNegativeButton("OK", null).show();
        }
    }
}

package id.halalvestor.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by Dell INSP 7348 on 10/4/2017.
 */

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;

    protected EditText et;

    public NumberTextWatcher(EditText et)
    {
        df =  new DecimalFormat("###,###,###");
        this.et = et;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    protected void change(Editable s){

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            if (!v.equalsIgnoreCase("")) {
                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                et.setText(df.format(n));
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
            Log.d(TAG, "exception", nfe);
        } catch (ParseException e) {
            Log.d(TAG, "exception", e);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);
        change(s);
        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


}
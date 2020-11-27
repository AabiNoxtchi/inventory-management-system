package com.example.inventoryui.Activities.Shared;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.Models.Shared.FilterType;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DateFilter {

    final String TAG = "MyActivity_DateFilter";

    Context context;
    Map<String, TextInputLayout> dialogFilterDateTexts ;
    Map<String,Object> dialogUrlParameters;
    private BaseMainActivity baseMainActivity;
    private SimpleDateFormat ft ;

    private enum DateError {Equal, ErrorBefore, ErrorAfter }
    final private String before = "Before";
    final private String after = "After";

    public DateFilter(Context context, Map<String, TextInputLayout> dialogFilterDateTexts ,
            Map<String,Object> dialogUrlParameters,
                      BaseMainActivity baseMainActivity){
        this.context = context;
        this.ft = ((AuthenticationManager)this.context.getApplicationContext()).ft;
        this.dialogFilterDateTexts = dialogFilterDateTexts;
        this.dialogUrlParameters = dialogUrlParameters;
        this.baseMainActivity = baseMainActivity;
    }

    public void giveMeDateTextInput(final String target, final String title) {

        final TextInputLayout txtLayout = new TextInputLayout(context);
        txtLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        txtLayout.setEndIconDrawable(R.drawable.ic_cancel_black_24dp);

        final TextInputEditText editTxt = new TextInputEditText(context);
        editTxt.setHint(title);
        editTxt.setFocusable(false);
        editTxt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        txtLayout.addView(editTxt);

        dialogFilterDateTexts.put(target,txtLayout);

        addOnClickListner(editTxt);
        addTextChangeListner(editTxt, txtLayout, target, title);

    }

    private void addOnClickListner(TextInputEditText editTxt) {

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date;

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editTxt.setText(ft.format(myCalendar.getTime()));
            }
        };

        editTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void addTextChangeListner(TextInputEditText editTxt, TextInputLayout txtLayout, String target, String title) {

        editTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                String target1 = setTarget1(target);
                String target2Url = setTarget2Url(target,target1);

                if(s.toString().length()>0) {

                    Date target1Date = getDate(s.toString());

                    DateError error = checkIfError(target1, target2Url, target1Date);
                    getErrorText(title, error, txtLayout);
                    if(error == null){

                        if(txtLayout.isErrorEnabled())txtLayout.setError(null);

                        if(dialogFilterDateTexts.containsKey(target2Url)) {
                            TextInputLayout tl = dialogFilterDateTexts.get(target2Url);
                            if(tl.isErrorEnabled())tl.setError(null);
                        }
                    }
                    if(!dialogUrlParameters.containsKey(target)){
                        baseMainActivity.addFiltersCount(FilterType.Dialog);
                    }
                    dialogUrlParameters.put(target, target1Date);

                }else{

                    if(dialogFilterDateTexts.containsKey(target2Url)) {
                        TextInputLayout tl = dialogFilterDateTexts.get(target2Url);
                        if(tl.isErrorEnabled())tl.setError(null);
                    }

                    dialogUrlParameters.remove(target);
                    baseMainActivity.substractFiltersCount(FilterType.Dialog);

                    if(txtLayout.isErrorEnabled()){
                        txtLayout.setError(null);
                    }
                }
            }
        });
    }

    private DateError checkIfError(String target1, String target2Url, Date target1Date) {

        if( !(target2Url!=null && dialogUrlParameters.keySet().contains(target2Url))) return null;
        DateError error = null;

        Date target2Date = (Date)dialogUrlParameters.get(target2Url);

        if(target1Date.equals(target2Date)) error = DateError.Equal;
        else if( target1.equals(before) && target1Date.before(target2Date)) error = DateError.ErrorBefore;
        else if( target1.equals(after) && target1Date.after(target2Date))  error = DateError.ErrorAfter;

       return error;
    }

    private String setTarget1(String target) {
        if(target.contains(before) || target.contains(after))
            return  target.contains(before) ? before : after;
        return null;
    }

    private String setTarget2Url(String target,String target1) {
        if (target1 == null) target1 = setTarget1(target);
        if(target1 !=null) {
            String target2 = target1.equals(before) ? after : before;
            return target.replace(target1, target2);
        }
        return null;
    }

    private void getErrorText(String title, DateError error, TextInputLayout txtLayout) {

        if(error == null ) return;

        String title1 = title.contains("before") ? "before" : "after";
        String title2 = title1.equals("before") ? "after" : "before";
        String title2Url = title.replace(title1,title2);

        String errorText = error.equals(DateError.Equal) ? (title+" can't be equal to "+title2Url) : (title+" can't be "+title2+" "+title2Url);

        txtLayout.setError(errorText);
        txtLayout.setErrorTextAppearance(R.style.ErrorTextAppearance);
    }

    private Date getDate(String s) {

        Date date = null;
        try {
            date = ft.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}

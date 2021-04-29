package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.DialogFilterClass;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Models.AuthenticationManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFilter extends InputLayoutFilter<Date>{

    static DateFilter instance;
    private DateFilter(){}
    public static synchronized DateFilter getInstance(){
        if(instance == null)
            instance = new DateFilter();
        return instance;

    }

    private SimpleDateFormat ft ;
    final private Calendar myCalendar = Calendar.getInstance();

    final private String before = "Before";
    final private String after = "After";
    final private String beforeHint = "before";
    final private String afterHint = "after";

    public void getDateFilter(DateAnnotation annotation,
                                  DialogFilterClass dialogFilterObj,
                                  Context context, BaseMainActivity activity){

            setVariables(context,activity);
            setLocalVariables(dialogFilterObj);
            getInputFilter(getTarget(annotation), getTitle(annotation));
    }


    protected void setLocalVariables(DialogFilterClass dialogFilterObj) {

        ft = ((AuthenticationManager) context.getApplicationContext()).ft;
        dialogUrlParameters = dialogFilterObj.getUrlParameters();
        dialogFilterInputs = dialogFilterObj.getFilterDateTexts();
        dialogComparableInputs = dialogFilterObj.getComparableInputs();

    }

    @Override
    protected String getBeforeString() {
        return before;
    }

    @Override
    protected String getAfterString() {
        return after;
    }

    @Override
    protected String getBeforeHint() {
        return beforeHint;
    }

    @Override
    protected String getAfterHint() {
        return afterHint;
    }

    @Override
    protected void editTextAddOtherListners(TextInputEditText editTxt) {
        addOnClickListner(editTxt);
    }

    @Override
    protected void editTxtSetFilters(TextInputEditText editTxt) {
    }

    @Override
    protected void editTextSetInputType(TextInputEditText editTxt) {
        editTxt.setFocusable(false);
        editTxt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    @Override
    protected void setLayoutParams(EditText editTxt) {
        // nothing
    }

    @Override
    protected void addPaddingToAfterInput(TextInputLayout txtLayout) {

    }

    @Override
    protected boolean checkIfBefore(Date target1Obj, Date target2Obj) {
        return target1Obj.before(target2Obj);
    }

    @Override
    protected boolean checkIfAfter(Date target1Obj, Date target2Obj) {
        return target1Obj.after(target2Obj);
    }

    @Override
    protected Date getObj(String toString) {
        Date date = null;
        try {
            date = ft.parse(toString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    protected String getTarget(DateAnnotation annotation) {
        return annotation.target();
    }

    protected String getTitle(DateAnnotation annotation) {
        return annotation.title();
    }

    private void addOnClickListner(TextInputEditText editTxt) {

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
                DatePickerDialog dialog =   new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                        dialog.getDatePicker().setMaxDate(new Date().getTime());
                        dialog.show();
            }
        });
    }

}

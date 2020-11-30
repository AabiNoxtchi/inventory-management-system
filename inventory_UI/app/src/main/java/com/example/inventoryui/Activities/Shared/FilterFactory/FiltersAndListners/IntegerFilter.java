package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.DialogFilterClass;
import com.example.inventoryui.Annotations.IntegerInputAnnotation;
import com.example.inventoryui.HelperFilters.MinMaxValueFilter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class IntegerFilter extends InputLayoutFilter<Integer> {

    final String TAG = "IntegerFilter";

    static IntegerFilter instance;
    private IntegerFilter(){}
    public static synchronized IntegerFilter getInstance(){
        if(instance == null)
            instance = new IntegerFilter();
        return instance;
    }

    final private String before = "Less";
    final private String after = "More";
    final private String beforeHint = "less than";
    final private String afterHint = "more than";

    public void getIntegerFilter(IntegerInputAnnotation annotation,
                                  DialogFilterClass dialogFilterObj,
                                  Context context, BaseMainActivity activity){

        setVariables(context,activity);
        setLocalVariables(dialogFilterObj);
        getInputFilter(getTarget(annotation), getTitle(annotation));

    }

    private void setLocalVariables(DialogFilterClass dialogFilterObj) {
        dialogUrlParameters = dialogFilterObj.getUrlParameters();
        dialogFilterInputs = dialogFilterObj.getFilterIntegerInputs();
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

    }

    @Override
    protected void editTxtSetFilters(TextInputEditText editTxt) {
        editTxt.setFilters( new InputFilter[]{ new MinMaxValueFilter( "1" , (Integer.MAX_VALUE)+"" )});
    }

    @Override
    protected void editTextSetInputType(TextInputEditText editTxt) {
        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    @Override
    protected void setLayoutParams(EditText editTxt) {
        int width = getEditTextWidth();
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams( // width,height
                        width, FrameLayout.LayoutParams.WRAP_CONTENT);

        editTxt.setLayoutParams(params);
    }

    @Override
    protected void addPaddingToAfterInput(TextInputLayout txtLayout) {
        txtLayout.setPadding(50,0,0,0);
    }


    protected int getEditTextWidth(){
        return 400;
    }

    @Override
    protected boolean checkIfBefore(Integer target1Obj, Integer target2Obj) {
        return target1Obj < target2Obj ;
    }

    @Override
    protected boolean checkIfAfter(Integer target1Obj, Integer target2Obj) {
        return target1Obj > target2Obj ;
    }

    @Override
    protected Integer getObj(String toString) {

        return Integer.parseInt(toString);
    }


    protected String getTarget(IntegerInputAnnotation annotation) {
        return annotation.target();
    }

    protected String getTitle(IntegerInputAnnotation annotation) {
        return annotation.title();
    }

}

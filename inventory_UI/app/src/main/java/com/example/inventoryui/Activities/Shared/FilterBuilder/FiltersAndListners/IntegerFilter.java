package com.example.inventoryui.Activities.Shared.FilterBuilder.FiltersAndListners;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FilterTypeClasses.FilterType;
import com.example.inventoryui.Annotations.IntegerInputAnnotation;
import com.example.inventoryui.HelperFilters.MinMaxValueFilter;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Map;

public class IntegerFilter {

    final String TAG = "MyActivity_textFilter";

    static IntegerFilter instance;
    private IntegerFilter(){}
    public static synchronized IntegerFilter getInstance(){
        if(instance == null)
            instance = new IntegerFilter();
        return instance;

    }

    Context context;
    private BaseMainActivity baseMainActivity;

    private enum DateError {Equal, ErrorBefore, ErrorAfter ,Length}
    final private String before = "Less";
    final private String after = "More";

    Map<String, TextInputLayout> dialogFilterIntegerInputs ;
    Map<String,Object> dialogUrlParameters;
    List<ComparableInputs> dialogComparableInputs;

    private void setVariables(Context context,
                              BaseMainActivity baseMainActivity){
        this.context = context;
        this.baseMainActivity = baseMainActivity;
    }

    public void giveMeDateTextInput(IntegerInputAnnotation dateAnnotation,
                                    Context context, BaseMainActivity activity,
                                    Map<String, TextInputLayout> dialogFilterIntegerInputs ,
                                    Map<String,Object> dialogUrlParameters,
                                    List<ComparableInputs> dialogComparableInputs) {

        setVariables(context,activity);
        this.dialogFilterIntegerInputs = dialogFilterIntegerInputs;
        this.dialogUrlParameters = dialogUrlParameters;
        this.dialogComparableInputs = dialogComparableInputs;

        String target = dateAnnotation.target();
        String title = dateAnnotation.title();
        boolean comparable = false;

        String hint ;
        if(target.contains(before)) {
            hint = "less than";
            comparable = true;
        }
        else if(target.contains(after)) {
            hint = "more than";
            comparable = true;
        }
        else hint = title;

        final TextInputLayout txtLayout = new TextInputLayout(context);
        txtLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        txtLayout.setEndIconDrawable(R.drawable.ic_cancel_black_24dp);
        //txtLayout.setHint(hint);

        final TextInputEditText editTxt = new TextInputEditText(context);
        editTxt.setHint(hint);

        editTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTxt.setTextAppearance(R.style.text_in_layout);
        editTxt.setFilters( new InputFilter[]{ new MinMaxValueFilter( "1" , (Integer.MAX_VALUE)+"" )});

        txtLayout.addView(editTxt);

        if(comparable) {
            dealWithComparable(txtLayout, editTxt, title, hint);

        }else {
            dialogFilterIntegerInputs.put(target, txtLayout);
        }
        addTextChangeListner(editTxt, txtLayout, target, title);
    }

    private void dealWithComparable(TextInputLayout txtLayout, EditText editTxt,String title, String hint) {

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams( // width,height
                        400, FrameLayout.LayoutParams.WRAP_CONTENT);

        //txtLayout.setLayoutParams(params);
        editTxt.setLayoutParams(params);
        String title1 = title.replace(hint," : ");

        ComparableInputs comparableInputs = null;
        Log.i(TAG,"*********************   title1 = "+title1);
        Log.i(TAG,"dialogComparableInputs.contains(title1) = "+dialogComparableInputs.contains(new ComparableInputs(title1)));

       /* List<ComparableInputs> inputs = dialogComparableInputs
                .stream()
                .filter(o -> o.getName().equals(title1))
                .collect(Collectors.toList())
               ;
        if(inputs != null){
            comparableInputs = inputs.get(0);
        }
*/
       /* if(comparableInputs == null){
            comparableInputs = new ComparableInputs(title1);
            dialogComparableInputs.add(comparableInputs);
        }*/

       ComparableInputs objToCompare = new ComparableInputs(title1);
        if(dialogComparableInputs.contains(objToCompare)) comparableInputs = dialogComparableInputs.get(dialogComparableInputs.indexOf(objToCompare));
        else{
            comparableInputs = new ComparableInputs(title1);
            dialogComparableInputs.add(comparableInputs);
        }

        for(ComparableInputs input : dialogComparableInputs) {
            Log.i(TAG, "ComparableInput  = " + input);
        }

        Log.i(TAG,"hint = "+hint);

        if(hint.contains("more")){
            comparableInputs.setMoreLayout(txtLayout);

        }else{
            comparableInputs.setLessLayout(txtLayout);
            txtLayout.setPadding(50,0,0,0);

            //editTxt.setPaddingRelative(100,0,0,0);
        }

       // this.dialogComparableTextInputs.

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

                    Integer target1Date = getDate(s.toString());

                    DateError error = checkIfError(target1, target2Url, target1Date);
                    getErrorText(title, error, txtLayout);
                    if(error == null){

                        if(txtLayout.isErrorEnabled())txtLayout.setError(null);

                        if(dialogFilterIntegerInputs.containsKey(target2Url)) {
                            TextInputLayout tl = dialogFilterIntegerInputs.get(target2Url);
                            if(tl.isErrorEnabled())tl.setError(null);
                        }
                    }
                    if(!dialogUrlParameters.containsKey(target)){
                        baseMainActivity.addFiltersCount(FilterType.Dialog);
                    }
                    dialogUrlParameters.put(target, target1Date);

                }else{

                    if(dialogFilterIntegerInputs.containsKey(target2Url)) {
                        TextInputLayout tl = dialogFilterIntegerInputs.get(target2Url);
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

    private DateError checkIfError(String target1, String target2Url, Integer target1Date) {

        if( !(target2Url!=null && dialogUrlParameters.keySet().contains(target2Url))) return null;
        DateError error = null;


        Integer target2Date = (Integer)dialogUrlParameters.get(target2Url);

        if(target1Date.equals(target2Date)) error = DateError.Equal;
        else if( target1.equals(before) && target1Date < (target2Date)) error = DateError.ErrorBefore;
        else if( target1.equals(after) && target1Date > (target2Date))  error = DateError.ErrorAfter;

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

        String title1 = title.contains("less") ? "less than" : "more than";
        String title2 = title1.contains("less") ? "more than" : "less than";
        //String title2Url = title.replace(title1,title2);

        String errorText = error.equals(DateError.Equal) ? (title1+" can't be equal to "+title2) : (title1+" can't be "+title2+" "+title2);

        txtLayout.setError(errorText);
       // txtLayout.setErrorTextAppearance(R.style.ErrorTextAppearance);
        //txtLayout.setHintTextAppearance(R.style.ErrorTextAppearance);
    }

    private Integer getDate(String s) {

        Integer date = null;

            date = Integer.parseInt(s);

        return date;
    }

}

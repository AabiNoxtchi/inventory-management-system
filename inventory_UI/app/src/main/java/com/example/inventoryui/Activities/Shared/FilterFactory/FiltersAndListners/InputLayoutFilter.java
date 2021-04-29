package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.FilterType;
import com.example.inventoryui.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Map;

public abstract class InputLayoutFilter <T extends Object>{

    Context context;
    private BaseMainActivity baseMainActivity;

    final private String before = getBeforeString();
    final private String after = getAfterString();
    final private String beforeHint = getBeforeHint();
    final private String afterHint = getAfterHint();

    protected Map<String, com.google.android.material.textfield.TextInputLayout> dialogFilterInputs ;
    protected Map<String,Object> dialogUrlParameters;
    protected List<ComparableInputs> dialogComparableInputs;

    protected enum Error {Equal, ErrorBefore, ErrorAfter }

    protected abstract String getBeforeString();
    protected abstract String getAfterString();
    protected abstract String getBeforeHint();
    protected abstract String getAfterHint();

    protected abstract void editTextAddOtherListners(TextInputEditText editTxt);
    protected abstract void editTxtSetFilters(TextInputEditText editTxt);
    protected abstract void editTextSetInputType(TextInputEditText editTxt);
    protected abstract void setLayoutParams(EditText editTxt);
    protected abstract void addPaddingToAfterInput(TextInputLayout txtLayout);

    protected abstract boolean checkIfBefore(T target1Obj, T target2Obj);
    protected abstract boolean checkIfAfter(T target1Obj, T target2Obj);

    protected abstract T getObj(String toString);

    protected void setVariables(Context context,
                              BaseMainActivity baseMainActivity){
        this.context = context;
        this.baseMainActivity = baseMainActivity;
    }

   protected void getInputFilter(String target, String title) {

        boolean comparable = false;

        String hint ;
        if(target.contains(before)) {
            hint = beforeHint;
            comparable = true;
        }
        else if(target.contains(after)) {
            hint = afterHint;
            comparable = true;
        }
        else hint = title;

        final TextInputLayout txtLayout = new TextInputLayout(context);
        txtLayout.setEndIconMode(com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT);
        txtLayout.setEndIconDrawable(R.drawable.ic_cancel_black_24dp);

        final TextInputEditText editTxt = new TextInputEditText(context);
        editTxt.setHint(hint);

        editTextSetInputType(editTxt);
        editTxt.setTextAppearance(R.style.text_in_layout);
        editTxtSetFilters(editTxt);

        txtLayout.addView(editTxt);
        if(comparable) {
            dealWithComparable(txtLayout, editTxt, title, hint);

        }else {
            dialogFilterInputs.put(target, txtLayout);
        }
        editTextAddOtherListners(editTxt);
        addTextChangeListner(editTxt, txtLayout, target, title, hint);
    }

    private void dealWithComparable(TextInputLayout txtLayout, EditText editTxt, String title, String hint) {

        setLayoutParams(editTxt);
        ComparableInputs comparableInputs = getComparableObject(title,hint);

       if(comparableInputs == null){
           String title1 = title.replace(hint," : ");
            comparableInputs = new ComparableInputs(title1);
            dialogComparableInputs.add(comparableInputs);
        }

        if(hint.contains(after.toLowerCase())){
            comparableInputs.setMoreLayout(txtLayout);

        }else{
            comparableInputs.setLessLayout(txtLayout);
            addPaddingToAfterInput(txtLayout);//
        }
    }

    private ComparableInputs getComparableObject(String title,String hint){

        String title1 = title.replace(hint," : ");
        ComparableInputs comparableInputs =
                dialogComparableInputs.stream().filter(c->c.getName().equals(title1)).findAny().orElse(null);

        return comparableInputs;
    }

    private void addTextChangeListner(TextInputEditText editTxt, TextInputLayout txtLayout, String target, String title, String hint) {

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

                   T target1Obj = getObj(s.toString());

                    Error error = checkIfError(target1, target2Url, target1Obj);
                    getErrorText(title, error, txtLayout);
                    if(error == null){

                        if(txtLayout.isErrorEnabled())txtLayout.setError(null);
                            clearComparableErrors(target2Url,title,hint);

                    }
                    if(!dialogUrlParameters.containsKey(target)){
                        baseMainActivity.addFiltersCount(FilterType.Dialog);
                    }
                    dialogUrlParameters.put(target, target1Obj);

                }else{
                    clearComparableErrors(target2Url,title,hint);

                    dialogUrlParameters.remove(target);
                    baseMainActivity.substractFiltersCount(FilterType.Dialog);

                    if(txtLayout.isErrorEnabled()){
                        txtLayout.setError(null);
                    }
                }
            }
        });
    }

    private void clearComparableErrors(String target2Url, String title, String hint) {
        if(dialogFilterInputs.containsKey(target2Url)) {

            TextInputLayout tl = dialogFilterInputs.get(target2Url);
            if(tl.isErrorEnabled())tl.setError(null);

        }else {

            ComparableInputs comparableInputs = getComparableObject(title,hint);

            if (hint.contains(after.toLowerCase())) {
                comparableInputs.getLessLayout().setError(null);
            }else if(hint.contains(before.toLowerCase())){
                comparableInputs.getMoreLayout().setError(null);
            }
        }
    }

    private Error checkIfError(String target1, String target2Url,T target1Obj) {

        if( !(target2Url!=null && dialogUrlParameters.keySet().contains(target2Url))) return null;
        Error error = null;

        T target2Obj = (T)dialogUrlParameters.get(target2Url);

        if(target1Obj.equals(target2Obj)) error = Error.Equal;
        else if( target1.equals(before) && checkIfBefore(target1Obj,target2Obj)) error = Error.ErrorBefore;
        else if(  target1.equals(after) && checkIfAfter(target1Obj,target2Obj))  error = Error.ErrorAfter;

        return error;
    }

    private String setTarget1(String target) {
        if(target.contains(before) || target.contains(after))
            return  target.contains(before) ? before : after;
        return null;
    }

    private String setTarget2Url(String target, String target1) {
        if (target1 == null) target1 = setTarget1(target);
        if(target1 !=null) {
            String target2 = target1.equals(before) ? after : before;
            return target.replace(target1, target2);
        }
        return null;
    }

    private void getErrorText(String title, Error error,
                              com.google.android.material.textfield.TextInputLayout txtLayout) {

        if(error == null ) return;

        String title1 = title.contains(before.toLowerCase()) ? beforeHint : afterHint;
        String title2 = title1.contains(before.toLowerCase()) ? afterHint : beforeHint;

        String errorText = error.equals(Error.Equal) ? (title1+" can't be equal to "+title2) :
                (title1+" can't be "+title1+" "+title2);

        txtLayout.setError(errorText);
    }

}

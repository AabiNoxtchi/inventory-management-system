package com.example.inventoryui.Activities.Shared.FilterBuilder;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FilterTypeClasses.BaseFilterClass;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FilterTypeClasses.DialogFilterClass;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FilterTypeClasses.FilterType;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FilterTypeClasses.FirstFilterClass;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FiltersAndListners.ComparableInputs;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FiltersAndListners.DateFilter;
import com.example.inventoryui.Activities.Shared.FilterBuilder.FiltersAndListners.IntegerFilter;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.IntegerInputAnnotation;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Map;

public class FilterBuilder {

    private static FilterBuilder instance;

   // FirstFilterClass firstFilterObject;
   // DialogFilterClass dialogFilterObject;

    BaseFilterClass baseFilterObject;
    Context context;
    BaseMainActivity baseActivity;

    private FilterBuilder(){


    }

    public static synchronized FilterBuilder getInstance(){
        if(instance == null){
            instance = new FilterBuilder();
        }

        return instance;
    }

   /* public void setVariables(FilterType filterType,
                             LinearLayout filterLayout,
                             BaseMainActivity baseMainActivity, Context context,Class filterClass)*/

    public void setVariables(FilterType filterType,
                           LinearLayout filterLayout,
                           BaseMainActivity baseMainActivity, Context context){//},Class filterClass){

        this.context = context;
        this.baseActivity = baseMainActivity;

        BaseFilterClass baseFilterObject = null;

        if (filterType.equals(FilterType.First)) {
            baseFilterObject = new FirstFilterClass(filterType);
           // firstFilterObject = (FirstFilterClass)baseFilterObject;
        } else if (filterType.equals(FilterType.Dialog)) {
            baseFilterObject = new DialogFilterClass(filterType);
           // dialogFilterObject = (DialogFilterClass)baseFilterObject;
        }

       // setFilters(baseFilterObject, filterLayout, filterClass);
    }

    public void dateFilter(DateAnnotation annotation, Map<String, TextInputLayout> dialogFilterDateTexts ,
                           Map<String,Object> dialogUrlParameters){
        DateFilter.getInstance().giveMeDateTextInput(annotation,this.context,this.baseActivity, dialogFilterDateTexts ,
               dialogUrlParameters);
    }

    public void integerFilter(IntegerInputAnnotation annotation, Map<String,TextInputLayout> sth, Map<String,Object> dialogUrlParameters,
                              List<ComparableInputs> dialogComparableInput){
        IntegerFilter.getInstance().giveMeDateTextInput(annotation, this.context, this.baseActivity,sth, dialogUrlParameters, dialogComparableInput);
    }

   /* private void setFilters( BaseFilterClass filterObj, LinearLayout filterLayout,Class filterClass) {
        for (Field f : filterClass.getDeclaredFields()) {

            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) {
                    continue;
                } else if (filterObj.getFilterType().equals(FilterType.Dialog) && annotation instanceof DateAnnotation) {


                    DateAnnotation dateAnnotation = (DateAnnotation) annotation;
                    String target = dateAnnotation.target();
                    String title = dateAnnotation.title();

                    *//*if (dialogFilterDateTexts == null)
                        dialogFilterDateTexts = new HashMap<>();*//*

                    //DateFilter dateFilter = new DateFilter(this,dialogFilterDateTexts,dialogUrlParameters,this);
                    // dateFilter.giveMeDateTextInput(target,title);

                }
            }
        }
    }*/

}

   /* private void setFilters( BaseFilterClass filterObj, LinearLayout filterLayout) {

        radioGroupTypeMap = new HashMap<>();
        spinnerTitles = null;

        for (Field f : filterClass.getDeclaredFields()) {

            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SkipAnnotation) {
                    continue;

                } else if (annotation instanceof ChechBoxAnnotation) {

                    ChechBoxAnnotation chckboxAnnotation = (ChechBoxAnnotation) annotation;
                    String title = chckboxAnnotation.title();
                    String target = chckboxAnnotation.target();

                    Object obj = getUrlObject(target);

                    if( title.equals("all") || filterType.equals(FilterType.First)) {
                        CheckBox chckbox = giveMeCheckBox(title,obj);
                        filterCheckBoxes.put(target, chckbox);

                    }else{
                        RadioGroup rg = giveMeRadioGroup(title,filterType,obj);
                        filterRadioGroups.put(target, rg);
                        radioGroupTypeMap.put(target,f.getType());

                    }
                }else if(annotation instanceof EnumAnnotation) {

                    EnumAnnotation enumAnnotation = (EnumAnnotation) annotation;
                    String target = enumAnnotation.target();

                    List<String> titles = new ArrayList<>();
                    List<Object> objList = (List) getValue(f, model.getFilter());
                    for(Object obj : objList) {
                        String name = ((SelectItem) obj).getName();
                        titles.add(name);
                    }

                    Object obj = getUrlObject(target);

                    RadioGroup rg = giveMeRadioGroup(titles,filterType,obj);
                    filterRadioGroups.put(enumAnnotation.target(), rg);
                    radioGroupTypeMap.put(target,f.getType());

                }else if ( filterType.equals(FilterType.Dialog) && annotation instanceof DropDownAnnotation) {

                    DropDownAnnotation dropDownAnnotation = (DropDownAnnotation) annotation;
                    String target = dropDownAnnotation.target();
                    String title = dropDownAnnotation.title();

                    List<Object> list = (List)getValue(f,model.getFilter());
                    if (list == null) continue;

                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
                    SearchableSpinner searchableSpinner = new SearchableSpinner(this);
                    searchableSpinner.setAdapter(adapter);

                    filterSpinners.put(searchableSpinner, new Pair(target, list));
                    if(spinnerTitles == null)spinnerTitles=new HashMap<>();
                    spinnerTitles.put(searchableSpinner,title);
                }else if ( filterType.equals(FilterType.Dialog) && annotation instanceof DateAnnotation) {

                    DateAnnotation dateAnnotation = (DateAnnotation) annotation;
                    String target = dateAnnotation.target();
                    String title = dateAnnotation.title();

                    if (dialogFilterDateTexts == null)
                        dialogFilterDateTexts = new HashMap<>();

                    DateFilter dateFilter = new DateFilter(this,dialogFilterDateTexts,dialogUrlParameters,this);
                    dateFilter.giveMeDateTextInput(target,title);

                }else if( filterType.equals(FilterType.Dialog) && annotation instanceof IntegerInputAnnotation){

                    IntegerInputAnnotation integerAnnotation = (IntegerInputAnnotation)annotation;
                    String target = integerAnnotation.target();
                    String title = integerAnnotation.title();

                    final TextInputLayout txtLayout = new TextInputLayout(BaseMainActivity.this);
                    txtLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                    txtLayout.setEndIconDrawable(R.drawable.ic_cancel_black_24dp);
                    // txtLayout.setHintTextAppearance(R.style.text_in_layout_hint_Style);

                    final TextInputEditText editTxt = new TextInputEditText(BaseMainActivity.this);
                    editTxt.setHint(title);
                    editTxt.setTextAppearance(R.style.text_in_layout);//setTextSize(16);
                    // editTxt.setHint
                    //editTxt.textsi
                    // editTxt.setFocusable(false);
                    editTxt.setInputType(InputType.TYPE_CLASS_NUMBER);

                    txtLayout.addView(editTxt);

                    if(dialogFilterIntegerInputs == null)
                        dialogFilterIntegerInputs = new HashMap<>();
                    dialogFilterIntegerInputs.put(target,txtLayout);


                    // addOnClickListner(editTxt);
                    //addTextChangeListner(editTxt, txtLayout, target, title);
                }
            }
        }
        addToLayout(filterLayout,filterCheckBoxes,filterRadioGroups,filterSpinners,spinnerTitles);
    }*/



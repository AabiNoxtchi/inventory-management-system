package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.BaseFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.FilterType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RadioGroupFilter {

    final String TAG = "Radio_Filter";

    private static RadioGroupFilter instance;
    private RadioGroupFilter() {}
    public synchronized static RadioGroupFilter getInstance() {
        if (instance == null)
            instance = new RadioGroupFilter();
        return instance;
    }

    Context context;
    private BaseMainActivity baseActivity;

    Map<String, RadioGroup> filterRadioGroups;
    List<RadioButton> checkedButtons;
    Map<String, Type> radioGroupTypeMap;

    Map<String, Object> urlParameters;
    FilterType filterType;

    int filtersChecked = 0;

    public void getRadioGroupFilter(Object titles, String target,
                                    Type fieldType,
                                    BaseFilterClass filterObj,
                                    Context context, BaseMainActivity activity) {

        setVariables(context, activity, filterObj);

        Object obj = baseActivity.getUrlObject(target);
        RadioGroup rg = giveMeRadioGroup(titles, filterType, obj);

        addListner(rg, target, filterType);
        filterRadioGroups.put(target, rg);
        radioGroupTypeMap.put(target, fieldType);

        checkRadioIfChecked(rg, target);

    }

    private void setVariables(Context context, BaseMainActivity baseActivity, BaseFilterClass filterObj) {
        this.baseActivity = baseActivity;
        this.context = context;

        this.filterRadioGroups = filterObj.getFilterRadioGroups();
        this.checkedButtons = filterObj.getCheckedButtons();
        this.radioGroupTypeMap = filterObj.getRadioGroupTypeMap();

        this.urlParameters = filterObj.getUrlParameters();
        this.filterType = filterObj.getFilterType();

        this.filtersChecked = baseActivity.getFilterCountInt(filterType);

    }

    private RadioGroup giveMeRadioGroup(Object obj, FilterType filterType, Object selected) {
        RadioGroup rg = new RadioGroup(this.context);

        List<String> names = null;
        if (obj instanceof String) {

            names = new ArrayList<>();
            names.add((String) obj);
            names.add("not " + obj);

        } else {
            names = (List) obj;
        }

        for (String name : names) {
            RadioButton rb1 = new RadioButton(this.context);
            rb1.setText(name);
            rg.addView(rb1);
        }

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams( // width,height
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (filterType.equals(FilterType.First)) {
            rg.setOrientation(RadioGroup.HORIZONTAL);
            params.leftMargin = 50;
            params.rightMargin = 80;
        } else {
            rg.setOrientation(RadioGroup.VERTICAL);
        }

        rg.setLayoutParams(params);
        return rg;
    }

    private void addListner(RadioGroup rg, String target, FilterType filterType) {

        final int rbCount = rg.getChildCount();

        for (int i = 0; i < rbCount; i++) {

            RadioButton b = (RadioButton) rg.getChildAt(i);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RadioButton radioButton = (RadioButton) v;
                    if (checkedButtons.contains(radioButton)) {
                        rg.clearCheck();
                        checkedButtons.remove(radioButton);
                    }

                    int count = baseActivity.getFilterCountInt(filterType);
                    if (!(count == 0 && !radioButton.isChecked())) {

                        Type type = radioGroupTypeMap.get(target);

                        String buttonName = radioButton.getText().toString();
                        buttonName = baseActivity.nameToBoolean(buttonName, type);

                        if (radioButton.isChecked()) {
                            if (urlParameters.containsKey(target)) {

                                for (int i = 0; i < rbCount; i++) {
                                    RadioButton b = (RadioButton) rg.getChildAt(i);
                                    String bName = b.getText().toString();

                                    bName = baseActivity.nameToBoolean(bName, type);

                                    if (!bName.equals(buttonName)) {
                                        checkedButtons.remove(b);
                                        b.setChecked(false);
                                    }
                                }

                            } else {
                                baseActivity.addFiltersCount(filterType);
                            }
                            checkedButtons.add(radioButton);
                            urlParameters.put(target, buttonName);

                        } else {

                            urlParameters.remove(target);
                            baseActivity.substractFiltersCount(filterType);
                        }
                        baseActivity.checkfilters(baseActivity.getFilterCountInt(filterType), urlParameters, filterType);
                        baseActivity.goGetItems(filterType);
                    }
                }
            });
        }
    }

    private void checkRadioIfChecked(RadioGroup rg, String target ) {
        if(filterType.equals(FilterType.First))return;

        Map<String,Object> firstFilterUrlParameters = baseActivity.getUrlParameters();
        if (firstFilterUrlParameters.containsKey(target)) {

            Object value = firstFilterUrlParameters.get(target);
            String btnValue = value.toString();

            Type type = radioGroupTypeMap.get(target);
            int count = rg.getChildCount();

            for (int i = 0; i < count; i++) {

                RadioButton b = (RadioButton) rg.getChildAt(i);
                String bName = b.getText().toString();
                bName = baseActivity.nameToBoolean(bName, type);

                boolean equals = bName.equals(btnValue);
                if (equals ) b.performClick();

            }
        }
    }

}


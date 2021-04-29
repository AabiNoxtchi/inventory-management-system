package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.BaseFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.FilterType;
import com.example.inventoryui.Annotations.CheckBoxAnnotation;

import java.util.Map;

public class CheckBoxFilter {

    private static CheckBoxFilter instance;
    private CheckBoxFilter() {}
    public synchronized static CheckBoxFilter getInstance() {
        if (instance == null) {
            instance = new CheckBoxFilter();
        }
        return instance;
    }

    Context context;
    BaseMainActivity baseActivity;

    Map<String, CheckBox> filterCheckBoxes;

    Map<String, Object> urlParameters;
    FilterType filterType;

    int filtersChecked = 0;

    public void getCheckBoxFilter(CheckBoxAnnotation annotation,
                                  BaseFilterClass filterObj,
                                  Context context, BaseMainActivity activity) {

        setVariables(context, activity, filterObj);

        String title = annotation.title();
        String target = annotation.target();

        Object obj = baseActivity.getUrlObject(target);
        CheckBox chckbox = getCheckBox(title, obj);
        filterCheckBoxes.put(target, chckbox);
        setListner(chckbox, target, filterType);
        checkIfChecked(chckbox, target);
    }

    private void setVariables(Context context, BaseMainActivity activity, BaseFilterClass filterObj) {
        this.baseActivity = activity;
        this.context = context;

        this.filterCheckBoxes = filterObj.getFilterCheckBoxes();
        this.urlParameters = filterObj.getUrlParameters();
        this.filterType = filterObj.getFilterType();

        this.filtersChecked = baseActivity.getFilterCountInt(filterType);
    }

    private CheckBox getCheckBox(String name, Object obj) {

        CheckBox chckbox = new CheckBox(context);
        chckbox.setText(name);
        chckbox.setPadding(0, 0, 50, 0);

        if (filtersChecked == 0 && name.equals("all")) chckbox.setChecked(true);
        else if (obj != null && obj.equals(true)) {
            chckbox.setChecked(true);
        }
        return chckbox;
    }

    private void setListner(CheckBox chckBox, String target,FilterType filterType) {

        chckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = baseActivity.getFilterCountInt(filterType);
                baseActivity.checkfilterBoxes(count, target, (CheckBox) v, filterType);

                count = baseActivity.getFilterCountInt(filterType);
                baseActivity.checkfilters(count, urlParameters, filterType);

                boolean isChecked = ((CheckBox) v).isChecked();
                if (count > 0) {
                    if (isChecked) {
                        urlParameters.put(target, true);
                    } else if (urlParameters.containsKey(target)) {
                        urlParameters.remove(target);
                    }
                }
                baseActivity.goGetItems(filterType);
            }
        });
    }

    private void checkIfChecked(CheckBox chckbox, String target) {

        if(filterType.equals(FilterType.First))return;

        Map<String,Object> firstFilterUrlParameters = baseActivity.getUrlParameters();

            if ( firstFilterUrlParameters.containsKey(target)) {

                if (chckbox.isChecked())
                    urlParameters.put(target, true);
                if (!target.equals("all")) chckbox.performClick();
            }
    }
}


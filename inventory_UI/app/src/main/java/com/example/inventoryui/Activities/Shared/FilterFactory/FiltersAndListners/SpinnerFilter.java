package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.DialogFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.FilterType;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinnerFilter {

    final String TAG = "Spinner_Filter";

    private static SpinnerFilter instance;
    private SpinnerFilter() {}
    public synchronized static SpinnerFilter getInstance() {
        if (instance == null)
            instance = new SpinnerFilter();
        return instance;
    }

    Context context;
    private BaseMainActivity baseActivity;

    Map<SearchableSpinner, Pair<String, List>> filterSpinners;
    HashMap<SearchableSpinner,String> spinnerTitles;

    Map<String, Object> urlParameters;
    FilterType filterType;

    int filtersChecked = 0;
    int spinnersChck = 0;

    public void getSpinnerFilter(DropDownAnnotation annotation,
                                 DialogFilterClass filterObj,
                                 List<Object> list,
                                 Context context, BaseMainActivity activity){

        setVariables(context, activity, filterObj);
        String target = annotation.target();
        String title = annotation.title();

        ArrayAdapter adapter = new ArrayAdapter(this.context, android.R.layout.simple_spinner_dropdown_item, list);
        SearchableSpinner searchableSpinner = new SearchableSpinner(this.context);
        searchableSpinner.setAdapter(adapter);

        searchableSpinner.setTitle(title); // added not tested
        searchableSpinner.setPrompt("prompt"+title);

        filterSpinners.put(searchableSpinner, new Pair(target, list));
        spinnerTitles.put(searchableSpinner, title);
        spinnersChck -= 1;

        addListner(searchableSpinner, target);
        checkSpinnerIfChecked(searchableSpinner, target, list);
    }

    private void setVariables(Context context, BaseMainActivity activity, DialogFilterClass filterObj) {
        this.baseActivity = activity;
        this.context = context;

        this.filterSpinners = filterObj.getFilterSpinners();
        this.spinnerTitles = filterObj.getSpinnerTitles();

        this.urlParameters = filterObj.getUrlParameters();
        this.filterType = filterObj.getFilterType();

        this.filtersChecked = baseActivity.getFilterCountInt(filterType);

    }

    public void addListner(SearchableSpinner spinner, String target){

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if( ++spinnersChck > 0) {
                        int count = baseActivity.getFilterCountInt(filterType);

                        if (!(count == 0 && position == 0))  {
                            Pair pair = filterSpinners.get(spinner);
                            String target = (String) pair.first;
                            if (position > 0) {

                                baseActivity.addFiltersCount(filterType);
                                SelectItem selectItem = (SelectItem) ((ArrayList) pair.second).get(position);
                                String value = selectItem.getValue();
                                urlParameters.put(target, value);

                            } else {
                                if (count > 0) baseActivity.substractFiltersCount(filterType);
                                urlParameters.remove(target);
                            }

                            baseActivity.checkfilters(baseActivity.getFilterCountInt(filterType), urlParameters, filterType);

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }

    private void checkSpinnerIfChecked( SearchableSpinner spinner, String target, List<Object> list){

        if(filterType.equals(FilterType.First))return;
        if( urlParameters.size()>0 && urlParameters.containsKey(target)) {
            spinner.setSelection(
                    list.indexOf(new SelectItem((String) urlParameters.get(target))));
            spinner.performClick();

        }
    }

}

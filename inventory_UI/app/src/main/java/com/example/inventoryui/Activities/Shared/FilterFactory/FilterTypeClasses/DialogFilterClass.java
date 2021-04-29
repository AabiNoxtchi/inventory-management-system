package com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses;

import android.util.Pair;

import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.ComparableInputs;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogFilterClass extends BaseFilterClass {

    Map<SearchableSpinner, Pair<String, List>> filterSpinners;
    HashMap<SearchableSpinner,String> spinnerTitles;
    Map<String, TextInputLayout>  filterDateTexts ;
    Map<String,TextInputLayout> filterIntegerInputs;
    List<ComparableInputs> comparableInputs;

    public DialogFilterClass(FilterType filterType) {
        super(filterType);
    }

    public Map<SearchableSpinner, Pair<String, List>> getFilterSpinners() {
        if(filterSpinners == null)
            filterSpinners = new HashMap<>();
        return filterSpinners;
    }
    public HashMap<SearchableSpinner, String> getSpinnerTitles() {
        if(spinnerTitles == null)
            spinnerTitles = new HashMap<>();
        return spinnerTitles;
    }
    public Map<String, TextInputLayout> getFilterDateTexts() {
        if(filterDateTexts == null)
            filterDateTexts = new HashMap<>();
        return filterDateTexts;
    }
    public Map<String, TextInputLayout> getFilterIntegerInputs() {
        if(filterIntegerInputs == null)
            filterIntegerInputs = new HashMap<>();
        return filterIntegerInputs;
    }
    public List<ComparableInputs> getComparableInputs() {
        if(comparableInputs == null)
            comparableInputs = new ArrayList<>();
        return comparableInputs;
    }


}

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

    int spinnersChck =0;

    public DialogFilterClass(FilterType filterType) {
        super(filterType);
    }

    public Map<SearchableSpinner, Pair<String, List>> getFilterSpinners() {
        if(filterSpinners == null)
            filterSpinners = new HashMap<>();
        return filterSpinners;
    }

    public void setFilterSpinners(Map<SearchableSpinner, Pair<String, List>> filterSpinners) {
        this.filterSpinners = filterSpinners;
    }

    public HashMap<SearchableSpinner, String> getSpinnerTitles() {
        if(spinnerTitles == null)
            spinnerTitles = new HashMap<>();
        return spinnerTitles;
    }

    public void setSpinnerTitles(HashMap<SearchableSpinner, String> spinnerTitles) {
        this.spinnerTitles = spinnerTitles;
    }

    public Map<String, TextInputLayout> getFilterDateTexts() {
        if(filterDateTexts == null)
            filterDateTexts = new HashMap<>();
        return filterDateTexts;
    }

    public void setFilterDateTexts(Map<String, TextInputLayout> filterDateTexts) {
        this.filterDateTexts = filterDateTexts;
    }

    public Map<String, TextInputLayout> getFilterIntegerInputs() {
        if(filterIntegerInputs == null)
            filterIntegerInputs = new HashMap<>();
        return filterIntegerInputs;
    }

    public void setFilterIntegerInputs(Map<String, TextInputLayout> filterIntegerInputs) {
        this.filterIntegerInputs = filterIntegerInputs;
    }

    public int getSpinnersChck() {
        return spinnersChck;
    }

    public void setSpinnersChck(int spinnersChck) {
        this.spinnersChck = spinnersChck;
    }

    public List<ComparableInputs> getComparableInputs() {
        if(comparableInputs == null)
            comparableInputs = new ArrayList<>();
        return comparableInputs;
    }

    public void setComparableIntegerInputs(List<ComparableInputs> comparableIntegerInputs) {
        this.comparableInputs = comparableIntegerInputs;
    }
}

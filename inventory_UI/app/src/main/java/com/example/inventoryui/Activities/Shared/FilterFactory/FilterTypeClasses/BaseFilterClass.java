package com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses;

import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseFilterClass {

    FilterType filterType;
    Map<String, CheckBox> filterCheckBoxes;
    Map<String, RadioGroup>  filterRadioGroups;
    List<RadioButton> checkedButtons;
    Map<String, Type> radioGroupTypeMap;
    Map<String,Object> urlParameters;

    public BaseFilterClass(FilterType filterType) {
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public Map<String, CheckBox> getFilterCheckBoxes() {
        if(filterCheckBoxes == null)
            filterCheckBoxes = new HashMap<>();
        return filterCheckBoxes;
    }

    public void setFilterCheckBoxes(Map<String, CheckBox> filterCheckBoxes) {

        this.filterCheckBoxes = filterCheckBoxes;
    }

    public Map<String, RadioGroup> getFilterRadioGroups() {
        if(filterRadioGroups == null)
            filterRadioGroups = new HashMap<>();
        return filterRadioGroups;
    }

    public void setFilterRadioGroups(Map<String, RadioGroup> filterRadioGroups) {
        this.filterRadioGroups = filterRadioGroups;
    }

    public List<RadioButton> getCheckedButtons() {
        if(checkedButtons == null)
            checkedButtons = new ArrayList<>();
        return checkedButtons;
    }

    public void setCheckedButtons(List<RadioButton> checkedButtons) {
        this.checkedButtons = checkedButtons;
    }

    public Map<String, Object> getUrlParameters() {
        if(urlParameters == null)
            urlParameters = new HashMap<>();
        return urlParameters;
    }

    public void setUrlParameters(Map<String, Object> urlParameters) {
        this.urlParameters = urlParameters;
    }

    public Map<String, Type> getRadioGroupTypeMap() {
        if(radioGroupTypeMap == null)
            radioGroupTypeMap = new HashMap<>();
        return radioGroupTypeMap;
    }

    public void setRadioGroupTypeMap(Map<String, Type> radioGroupTypeMap) {
        this.radioGroupTypeMap = radioGroupTypeMap;
    }
}

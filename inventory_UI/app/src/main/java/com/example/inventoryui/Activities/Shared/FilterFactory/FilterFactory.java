package com.example.inventoryui.Activities.Shared.FilterFactory;

import android.content.Context;

import com.example.inventoryui.Activities.Shared.BaseMainActivity;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.BaseFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FilterTypeClasses.DialogFilterClass;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.CheckBoxFilter;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.DateFilter;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.IntegerFilter;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.RadioGroupFilter;
import com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners.SpinnerFilter;
import com.example.inventoryui.Annotations.CheckBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.IntegerInputAnnotation;

import java.lang.reflect.Type;
import java.util.List;

public class FilterFactory {

    private static FilterFactory instance;
    private FilterFactory(){}
    public static synchronized FilterFactory getInstance(){
        if(instance == null){
            instance = new FilterFactory();
        }
        return instance;
    }

    Context context;
    BaseMainActivity baseActivity;

    public void setVariables(
                           BaseMainActivity baseMainActivity, Context context){

        this.context = context;
        this.baseActivity = baseMainActivity;
    }

    public void SpinnerFilter(DropDownAnnotation annotation, DialogFilterClass filterObj, List<Object> list){

        SpinnerFilter.getInstance().getSpinnerFilter(annotation, filterObj,list, this.context, this.baseActivity);
    }

    public void radioGroupFilter(Object titles, String target, Type fieldType, BaseFilterClass filterObj){

        RadioGroupFilter.getInstance().getRadioGroupFilter(titles,target,fieldType,filterObj,this.context,this.baseActivity);
    }

    public void checkBoxFilter(CheckBoxAnnotation annotation, BaseFilterClass filterObj){

        CheckBoxFilter.getInstance().getCheckBoxFilter(annotation, filterObj, this.context, this.baseActivity);
    }

    public void dateFilter(DateAnnotation annotation, DialogFilterClass filterObj){

        DateFilter.getInstance().getDateFilter(annotation, filterObj, this.context,this.baseActivity);
    }

    public void integerFilter(IntegerInputAnnotation annotation, DialogFilterClass filterObj){

        IntegerFilter.getInstance().getIntegerFilter(annotation, filterObj, this.context, this.baseActivity);
    }

}



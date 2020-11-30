package com.example.inventoryui.Activities.Shared.FilterFactory.FiltersAndListners;

import com.google.android.material.textfield.TextInputLayout;

public class ComparableInputs {

    private String name;
    private TextInputLayout moreLayout;
    private TextInputLayout lessLayout;

    public ComparableInputs(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextInputLayout getMoreLayout() {
        return moreLayout;
    }

    public void setMoreLayout(TextInputLayout moreLayout) {
        this.moreLayout = moreLayout;
    }

    public TextInputLayout getLessLayout() {
        return lessLayout;
    }

    public void setLessLayout(TextInputLayout lessLayout) {
        this.lessLayout = lessLayout;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof ComparableInputs)) {
            return false;
        } else {
            return ((ComparableInputs) o).getName().equals(this.getName()) ;
        }
    }

    @Override
    public String toString(){
        return this.getName();
    }

    @Override
    public int hashCode() {
        int result=5;
        result*= (this.getName().hashCode());
        return result;
    }

}

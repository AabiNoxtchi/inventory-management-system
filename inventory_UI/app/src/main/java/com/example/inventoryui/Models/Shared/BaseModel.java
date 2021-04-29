package com.example.inventoryui.Models.Shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseModel implements Serializable {

    private Long id;

    private boolean isSelected = false;

    public BaseModel() {}

    public BaseModel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isSelected() {
        return isSelected;
    }

    @JsonIgnore
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public void toggleSelected(){
        setSelected(!isSelected());
    }

}

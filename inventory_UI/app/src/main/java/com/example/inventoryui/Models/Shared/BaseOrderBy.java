package com.example.inventoryui.Models.Shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class BaseOrderBy implements Serializable {
    private String Prefix;

    public String getPrefix() {

        if(Prefix == null ) return "OrderBy" ; return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }
}

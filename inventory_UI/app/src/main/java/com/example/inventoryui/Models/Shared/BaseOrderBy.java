package com.example.inventoryui.Models.Shared;

import java.io.Serializable;

public abstract class BaseOrderBy implements Serializable {
    private String Prefix;

    public String getPrefix() {

        if(Prefix == null ) return "OrderBy" ; return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }
}

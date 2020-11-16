package com.example.inventoryui.Models.Shared;

import java.io.Serializable;

public class BaseModel implements Serializable {

    private Long id;

    public BaseModel() {
    }

    public BaseModel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

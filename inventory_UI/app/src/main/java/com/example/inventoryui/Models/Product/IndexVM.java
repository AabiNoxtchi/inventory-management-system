package com.example.inventoryui.Models.Product;


import com.example.inventoryui.Models.Shared.BaseIndexVM;
import com.example.inventoryui.Models.Shared.PagerVM;

import java.io.Serializable;
import java.util.List;

public class IndexVM extends BaseIndexVM<Product, FilterVM, OrderBy> implements Serializable {


    public IndexVM() {
    }

    public IndexVM(PagerVM pager, FilterVM filter,
                   com.example.inventoryui.Models.Product.OrderBy orderBy, String hello, List<Product> items) {
        super(pager, filter, orderBy, hello, items);
    }
}

package com.manager.helo.model.eventBus;

import com.manager.helo.model.NewProduct;

public class FixDelEvent {
    NewProduct newProduct;

    public FixDelEvent(NewProduct newProduct) {
        this.newProduct = newProduct;
    }

    public NewProduct getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(NewProduct newProduct) {
        this.newProduct = newProduct;
    }
}

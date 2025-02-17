package services;

import models.*;

// Decorator Pattern - allows adding additional responsibilities dynamically
public abstract class PropertyServiceDecorator extends Property {
    protected Property property;

    public PropertyServiceDecorator(Property property) {
        super(property.getSizeInSquareMeters(), property.getPrice(), property.getAddress());
        this.property = property;
    }

    @Override
    public void displayPropertyInfo() {
        property.displayPropertyInfo();
        addServiceDetails();
    }

    protected abstract void addServiceDetails();
}


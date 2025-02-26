package services;

import models.Property;

abstract class ServiceDecorator extends Property {
    protected Property decoratedProperty; // 👈 Stores the wrapped Property

    public ServiceDecorator(Property property) {
        super(property.getSizeInSquareMeters(), property.getPrice(), property.getAddress(), property.isSold());
        this.decoratedProperty = property;
    }

    @Override
    public double getPrice() {
        return decoratedProperty.getPrice(); // ✅ Calls the wrapped property
    }

    @Override
    public void getInfo() {
        decoratedProperty.getInfo(); // ✅ Calls the wrapped method
    }
}
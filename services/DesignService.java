package services;
import models.*;
// Decorator Pattern - allows adding additional responsibilities dynamically
public class DesignService extends ServiceDecorator {
    public DesignService(Property property) {
        super(property);
    }

    @Override
    public int getPrice() {
        return decoratedProperty.getPrice() + 100; // ✅ Stacks on top of the original price
    }

    @Override
    public void getInfo() {
        decoratedProperty.getInfo();
        System.out.println("with Cleaning Service");
    }
}
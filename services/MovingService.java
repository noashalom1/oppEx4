package services;
import models.*;
// Decorator Pattern - allows adding additional responsibilities dynamically
class MovingService extends ServiceDecorator {
    public MovingService(Property property) {
        super(property);
    }

    @Override
    public double getPrice() {
        return decoratedProperty.getPrice() + 200; // ✅ Stacks on top of the original price
    }

    @Override
    public void getInfo() {
        decoratedProperty.getInfo();
        System.out.println("with Cleaning Service");
    }
}
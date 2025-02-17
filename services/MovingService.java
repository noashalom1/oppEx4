package services;
import models.*;

public class MovingService extends PropertyServiceDecorator {
    public MovingService(Property property) {
        super(property);
    }

    @Override
    protected void addServiceDetails() {
        System.out.println("Includes Moving Service.");
    }

    @Override
    protected void displaySpecificDetails() {

    }
}
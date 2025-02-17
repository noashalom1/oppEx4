package services;
import models.*;

public class CleaningService extends PropertyServiceDecorator {
    public CleaningService(Property property) {
        super(property);
    }

    @Override
    protected void addServiceDetails() {
        System.out.println("Includes Cleaning Service.");
    }

    @Override
    protected void displaySpecificDetails() {

    }
}
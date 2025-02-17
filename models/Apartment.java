package models;

// Concrete class implementing Property
public class Apartment extends Property {
    public Apartment(double sizeInSquareMeters, double price, Address address) {
        super(sizeInSquareMeters, price, address);
    }

    @Override
    protected void displaySpecificDetails() {
        System.out.println("Property Type: Apartment");
        System.out.println("Size: " + getSizeInSquareMeters() + " sqm");
    }
}
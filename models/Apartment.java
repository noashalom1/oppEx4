package models;

public class Apartment extends Property {
    public Apartment(double sizeInSquareMeters, double price, Address address,boolean isSold) {
        super(sizeInSquareMeters, price, address,isSold);
    }
    @Override
    public void getInfo() {
        System.out.println("Apartment");
        super.displayInfo();
    }
}
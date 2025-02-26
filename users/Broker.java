package users;
import exceptions.DuplicateAddressException;
import exceptions.UnauthorizedEditException;
import models.*;
import management.PropertyManager;

public class Broker extends User {
    public Broker(String name) {
        super(name);
    }

    public void addProperty(Property property) throws DuplicateAddressException {
        PropertyManager.getInstance().addProperty(property);
        System.out.println(name + " added property: " + property.getAddress());
    }

    public void editPropertyDetails(Property property, double newPrice, double newSize) throws UnauthorizedEditException {
        property.setProperty(this, newPrice, newSize);
    }

    public void listAllProperties() {
        PropertyManager.getInstance().listAllProperties();
    }
}
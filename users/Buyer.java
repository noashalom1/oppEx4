package users;
import models.*;
import management.PropertyManager;
import java.util.List;

public class Buyer extends User {

    public Buyer(String name) {
        super(name);

    }

    public void searchPropertiesByRadius(Address center, double radius) {
        List<Property> results = PropertyManager.getInstance().getPropertiesWithinRadius(center, radius);
        System.out.println("Properties within radius " + radius + ":");
        results.forEach(Property::displayPropertyInfo);
    }

    public void getSoldPropertiesByRadius(Address center, double radius) {
        List<Property> results = PropertyManager.getInstance().getPropertiesWithinRadius(center, radius);
        System.out.println("Sold properties within radius " + radius + ":");
        results.stream().filter(Property::isSold).forEach(Property::displayP  ropertyInfo);
    }
}
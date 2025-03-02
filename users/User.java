package users;

import exceptions.PropertyNotFoundException;
import management.PropertyManager;
import models.Address;
import models.Property;

import java.util.ArrayList;
import java.util.List;

// Abstract User Class
public abstract class User {
    protected String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Property> getPropertiesWithinRadius(Property centerProperty, double radius) throws PropertyNotFoundException {

        // Check if the centerProperty is actually in the manager
        if (centerProperty == null
                || !PropertyManager.getInstance().getProperties().contains(centerProperty)) {
            System.out.println("Center property not found in PropertyManager's list");
            throw new PropertyNotFoundException("Center property not found in PropertyManager's list.");
        }

        Address centerAddr = centerProperty.getAddress();
        List<Property> result = new ArrayList<>();

        for (Property property : PropertyManager.getInstance().getProperties()) {
            double distance = centerAddr.calculateDistance(property.getAddress());
            if (distance <= radius) {
                result.add(property);
            }
        }
        return result;
    }


    public double getAveragePricePerSquareMeter(Property centerProperty, double radius) throws PropertyNotFoundException {

        List<Property> nearby = getPropertiesWithinRadius(centerProperty, radius);
        if (nearby.isEmpty()) return 0.0;

        double total = 0.0;
        for (Property p : nearby) {
            total += (double) p.getPrice() / p.getSizeInSquareMeters();
        }
        return total / nearby.size();
    }

    public List<Property> getSoldPropertiesWithinRadius(Property centerProperty, double radius) throws PropertyNotFoundException {

        List<Property> nearby = getPropertiesWithinRadius(centerProperty, radius);
        List<Property> sold = new ArrayList<>();
        for (Property p : nearby) {
            if (p.isSold()) {
                sold.add(p);
            }
        }
        return sold;
    }

    public List<Property> getAvailablePropertiesWithinRadius(Property centerProperty, double radius) throws PropertyNotFoundException {

        List<Property> nearby = getPropertiesWithinRadius(centerProperty, radius);
        List<Property> available = new ArrayList<>();
        for (Property p : nearby) {
            if (!p.isSold()) {
                available.add(p);
            }
        }
        return available;
    }

    public List<Property> getPropertiesByPriceComparison(Property centerProperty, double radius, String comparison) throws PropertyNotFoundException {
        // 1) Check that centerProperty is in the manager, else throw PropertyNotFoundException
        if (centerProperty == null
                || !PropertyManager.getInstance().getProperties().contains(centerProperty)) {
            throw new PropertyNotFoundException("Center property not found in PropertyManager's list.");
        }

        // 2) Calculate the center’s price per square meter
        double centerPricePerSqm = (double) centerProperty.getPrice() / centerProperty.getSizeInSquareMeters();

        // 3) Find all properties within the given radius
        List<Property> nearby = getPropertiesWithinRadius(centerProperty, radius);
        // (This calls your existing getPropertiesWithinRadius(...) method.)

        // 4) Compare each property’s price per sq. meter to the center
        List<Property> matched = new ArrayList<>();
        for (Property p : nearby) {
            double pPricePerSqm = (double) p.getPrice() / p.getSizeInSquareMeters();

            // “higher”, “lower”, or “equal” comparisons
            switch (comparison.toLowerCase()) {
                case "higher":
                    if (pPricePerSqm > centerPricePerSqm) {
                        matched.add(p);
                    }
                    break;
                case "lower":
                    if (pPricePerSqm < centerPricePerSqm) {
                        matched.add(p);
                    }
                    break;
                case "equal":
                    // Optional small epsilon check for floating precision
                    if (Math.abs(pPricePerSqm - centerPricePerSqm) < 1e-9) {
                        matched.add(p);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid comparison type. Use 'higher', 'lower', or 'equal'.");
            }
        }

        return matched;
    }

}
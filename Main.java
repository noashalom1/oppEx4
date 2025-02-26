import models.*;
import users.*;
import exceptions.*;
import management.PropertyManager;

public class Main {
    public static void main(String[] args) throws UnauthorizedEditException {
        // Create users
        Broker broker = new Broker("John Doe");
        Seller seller = new Seller("Alice Smith", broker);
        Buyer buyer = new Buyer("Bob Johnson");

        // Loading properties from file
        System.out.println("\n--- Loading Properties from File ---");
        PropertyManager.getInstance().loadPropertiesFromFile("properties.txt");
        broker.listAllProperties();

        // Attempt to add a property with duplicate address
        try { 
            Property duplicateProperty = new Apartment(150, 550000, new Address(4, 5));
            broker.addProperty(duplicateProperty);
        } catch (DuplicateAddressException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Broker edits property
        if (!PropertyManager.getInstance().getProperties().isEmpty()) {
            System.out.println("\n--- Broker Edits Property ---");
            Property propertyToEdit = PropertyManager.getInstance().getProperties().get(0);
            broker.editPropertyDetails(propertyToEdit, 480000, 110);
            broker.listAllProperties();

            // Unauthorized user attempts to edit property
            System.out.println("\n--- Unauthorized User Attempts to Edit Property ---");
            seller.deleteProperty(propertyToEdit);  // Allowed for seller

            try {
                propertyToEdit.setProperty(seller, 450000, 105);  // Unauthorized edit
            } catch (UnauthorizedEditException e) {
                System.out.println("Unauthorized Edit Attempt: " + e.getMessage());
            }

            // Demonstrating new functionalities
            Address searchCenter = new Address(4, 5);
            double radius = 2.0;

            // 1. Average price per square meter
            double avgPricePerSqM = PropertyManager.getInstance().getAveragePricePerSquareMeter(searchCenter, radius);
            System.out.println("\nAverage Price per Square Meter within radius " + radius + ": $" + avgPricePerSqM);

            // 2. Sold properties within radius
            System.out.println("\n--- Sold Properties Within Radius ---");
            PropertyManager.getInstance().getSoldPropertiesWithinRadius(searchCenter, radius)
                    .forEach(Property::displayPropertyInfo);

            // 3. Available properties within radius
            System.out.println("\n--- Available Properties Within Radius ---");
            PropertyManager.getInstance().getAvailablePropertiesWithinRadius(searchCenter, radius)
                    .forEach(Property::displayPropertyInfo);

            // 4. Properties by price comparison within radius
            double targetPricePerSqM = 4000;  // Example target price per square meter

            System.out.println("\n--- Properties with Higher Price per SqM ---");
            PropertyManager.getInstance().getPropertiesByPriceComparison(searchCenter, radius, targetPricePerSqM, "higher")
                    .forEach(Property::displayPropertyInfo);

            System.out.println("\n--- Properties with Lower Price per SqM ---");
            PropertyManager.getInstance().getPropertiesByPriceComparison(searchCenter, radius, targetPricePerSqM, "lower")
                    .forEach(Property::displayPropertyInfo);

            System.out.println("\n--- Properties with Equal Price per SqM ---");
            PropertyManager.getInstance().getPropertiesByPriceComparison(searchCenter, radius, targetPricePerSqM, "equal")
                    .forEach(Property::displayPropertyInfo);

        } else {
            System.out.println("No properties loaded to edit.");
        }

    }
}


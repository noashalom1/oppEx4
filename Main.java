import exceptions.*;
import management.PropertyManager;
import models.*;
import services.*;
import users.*;

import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Singleton instance of PropertyManager
        PropertyManager propertyManager = PropertyManager.getInstance();

        // 1. Load properties from file
        System.out.println("\n--- Loading Properties from File ---");
        propertyManager.loadProperties("properties.txt");

        // 2. Display all properties loaded
        System.out.println("\n--- Listing All Properties After Loading ---");
        propertyManager.listAllProperties();

        // 3. Create a few Sellers, Brokers, and Buyers
        Broker brokerJohn = new Broker("John");
        Broker brokerChen = new Broker("Chen");
        Broker brokerKobi = new Broker("Kobi");

        Seller sellerAlice = new Seller("Alice", brokerJohn);
        Seller sellerShahar = new Seller("Shahar", brokerChen);
        Seller sellerShani = new Seller("Shani", brokerKobi);//אם אני שמה בטעות מתווך לא נכון?


        Buyer buyerTom = new Buyer("Tom");
        buyerTom.setFunds(400_000); // Tom has a budget of 400k

        Buyer buyerDana = new Buyer("Dana");
        buyerDana.setFunds(700_000); // Dana has a budget of 700k


        // 4. Create properties from the text file
        List<Property> allProps = propertyManager.getProperties();

        Address targetAddr = new Address(4, 5, 1);
        Property prop451 = allProps.stream().filter(p -> p.getAddress().equals(targetAddr)).findFirst().orElse(null);

        Address targetAddr1 = new Address(4, 5);
        Property notexitedproperty = allProps.stream().filter(p -> p.getAddress().equals(targetAddr1)).findFirst().orElse(null);

        Address targetAddr2 = new Address(5, 6);
        Property prop56 = allProps.stream().filter(p -> p.getAddress().equals(targetAddr2)).findFirst().orElse(null);


        //SCENARIO 1: Authorized broker edits his property (success)
        System.out.println("\n--- Attempting To Edit Authorized Property ---");

//        if (prop451 == null) {
//            System.out.println("No property found at " + targetAddr);
//            return;
//        }
        try {
            prop451.getOwner().getBroker().editProperty(prop451, 290000, null, null);
            // newPrice=290000, newSize=null => keep old size, newStatus=null => keep old sold/available status
        } catch (UnauthorizedEditException | PropertyNotFoundException e) {
            System.err.println("Edit failed: " + e.getMessage());
        }
        // Print list again to show the updated state
        System.out.println("\n--- Properties After Edit ---");
        propertyManager.listAllProperties();

        //SCENARIO 2: Unauthorized broker tries to edit a property (failed)
        System.out.println("\n--- Attempting To Edit Unauthorized Property ---");
        try {
            brokerJohn.editProperty(prop451, 290000, null, null);
        } catch (UnauthorizedEditException | PropertyNotFoundException e) {
            System.err.println("Edit failed: " + e.getMessage());
        }

        //SCENARIO 3: A broker tries to edit a property that does not exist (failed)
        System.out.println("\n--- Attempting To Edit Non-Existent Property ---");

//        if (notexitedproperty == null) {
//            System.out.println("No property found at " + targetAddr1);
//        } else {
        try {
            brokerJohn.editProperty(notexitedproperty, 290000, null, null);
        } catch (UnauthorizedEditException | PropertyNotFoundException e) {
            System.err.println("Edit failed: " + e.getMessage());
        }


        // SCENARIO 1: Unauthorized seller tries to delete a property (failed)
        System.out.println("\n--- Attempting To Delete Unauthorized Property ---");
        try {
            sellerAlice.deleteProperty(prop451);
        } catch (UnauthorizedDeleteException | PropertyNotFoundException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }

        // SCENARIO 2: Authorized seller tries to delete her own property (success)
        System.out.println("\n--- Attempting To Delete Authorized Property ---");
        try {
            sellerShahar.deleteProperty(prop451);
        } catch (UnauthorizedDeleteException | PropertyNotFoundException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }

        // Print list again to show the updated state
        System.out.println("\n--- Properties After Delete ---");
        propertyManager.listAllProperties();


        //SCENARIO 3: A seller tries to delete a property that does not exist (failed)
        System.out.println("\n--- Attempting To Delete Non-Existent Property ---");
        try {
            sellerShahar.deleteProperty(prop451);
        } catch (UnauthorizedDeleteException | PropertyNotFoundException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }


        // 1) buyerTom -> getAveragePricePerSquareMeter
        System.out.println("\n--- Get Average Price Per Square Meter ---");
        try {
            double avgPriceSqM = buyerTom.getAveragePricePerSquareMeter(prop56, 2.0);
            System.out.println("Average price/sqm within radius=2 of property "
                    + prop56.getAddress() + " = " + avgPriceSqM);
        } catch (PropertyNotFoundException e) {
            System.err.println("Center property not found: " + e.getMessage());
        }

// 2) sellerAlice -> getSoldPropertiesWithinRadius
        System.out.println("\n--- get Sold Properties Within Radius ---");
        try {
            List<Property> soldProps = sellerAlice.getSoldPropertiesWithinRadius(prop56, 2.0);
            if (soldProps.isEmpty()) {
                System.out.println("No sold properties found within radius=2 of "
                        + prop56.getAddress());
            } else {
                System.out.println("Sold properties within radius=2 of "
                        + prop56.getAddress() + ":");
                for (Property p : soldProps) {
                    p.displayInfo();
                }
            }
        } catch (PropertyNotFoundException e) {
            System.err.println("Center property not found: " + e.getMessage());
        }

        // 3) buyerDana -> getAvailablePropertiesWithinRadius
        System.out.println("\n--- get Available Properties Within Radius ---");
        try {
            List<Property> availableProps = buyerDana.getAvailablePropertiesWithinRadius(prop56, 2.0);
            if (availableProps.isEmpty()) {
                System.out.println("No available properties found within radius=2 of "
                        + prop56.getAddress());
            } else {
                System.out.println("Available properties within radius=2 of "
                        + prop56.getAddress() + ":");
                for (Property p : availableProps) {
                    p.displayInfo();
                }
            }
        } catch (PropertyNotFoundException e) {
            System.err.println("Center property not found: " + e.getMessage());
        }


        // 4) brokerJohn -> getPropertiesByPriceComparison
        // e.g. we look for properties whose price/sqm is "higher" than centerProperty's
        System.out.println("\n--- get Properties By Price Comparison (higher) ---");
        try {
            List<Property> higherPricedProps = brokerJohn.getPropertiesByPriceComparison(prop56, 2.0, "higher");
            if (higherPricedProps.isEmpty()) {
                System.out.println("No properties found with price/sqm higher than property at "
                        + prop56.getAddress() + " within radius=2.");
            } else {
                System.out.println("Properties with price/sqm higher than property at "
                        + prop56.getAddress() + " (within radius=2):");
                for (Property p : higherPricedProps) {
                    p.displayInfo();
                }
            }
        } catch (PropertyNotFoundException e) {
            System.err.println("Center property not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid comparison: " + e.getMessage());
        }

        // ------------------------------------------------------
        // SCENARIO 1: brokerKobi sells prop56 to buyerTom (insufficient funds)
        // ------------------------------------------------------
        System.out.println("\n--- brokerKobi -> sell prop56 to buyerTom (no budget) ---");

        // Quick check if prop56 was found
//        if (prop56 == null) {
//            System.out.println("prop56 (5,6) not found in manager. Cannot sell.");
//        } else {
            try {
                // No extra services
                brokerKobi.sellProperty(
                        brokerKobi,
                        prop56,
                        buyerTom,
                        false, // isCleanService
                        false, // isMovingService
                        false, // isSuretyService
                        false  // isDesignService
                );
            } catch (UnauthorizedEditException | PropertyNotFoundException
                     | InsufficientFundsException | UnavailableProperty e) {
                System.err.println("Sell failed: " + e.getMessage());
            }


        // ------------------------------------------------------
        // SCENARIO 2: brokerKobi sells prop56 to buyerDana with cleaning service
        // ------------------------------------------------------
        System.out.println("\n--- brokerKobi -> sell prop56 to buyerDana (cleaning service) ---");

//        if (prop56 == null) {
//            System.out.println("prop56 (5,6) not found in manager. Cannot sell.");
//        } else {
            try {
                // Use cleaning service = true
                brokerKobi.sellProperty(
                        brokerKobi,
                        prop56,
                        buyerDana,
                        true,  // isCleanService
                        false, // isMovingService
                        false, // isSuretyService
                        false  // isDesignService
                );
            } catch (UnauthorizedEditException | PropertyNotFoundException
                     | InsufficientFundsException | UnavailableProperty e) {
                System.err.println("Sell failed: " + e.getMessage());
            }
        System.out.println("\n--- Properties After Sell ---");
        propertyManager.listAllProperties();

        // ------------------------------------------------------
        // SCENARIO 3: brokerKobi tries to sell prop451(not exited) to buyerTom
        // ------------------------------------------------------
        System.out.println("\n--- brokerKobi -> sell prop451 (not exited) to buyerTom ---");
            try {
                brokerKobi.sellProperty(
                        brokerKobi,
                        prop451,
                        buyerTom,
                        false, // no cleaning
                        false, // no moving
                        false, // no surety
                        false  // no design
                );
            } catch (UnauthorizedEditException | PropertyNotFoundException
                     | InsufficientFundsException | UnavailableProperty e) {
                System.err.println("Sell failed: " + e.getMessage());
            }

        // ------------------------------------------------------
        // SCENARIO 4: brokerKobi tries to sell prop56(unavailable) to buyerTom
        // ------------------------------------------------------
        System.out.println("\n--- brokerKobi -> sell prop56 (unavailable) to buyerTom ---");
        try {
            brokerKobi.sellProperty(
                    brokerKobi,
                    prop56,
                    buyerTom,
                    false, // no cleaning
                    false, // no moving
                    false, // no surety
                    false  // no design
            );
        } catch (UnauthorizedEditException | PropertyNotFoundException
                 | InsufficientFundsException | UnavailableProperty e) {
            System.err.println("Sell failed: " + e.getMessage());
        }

//
//        // 4. Create and add a new property (manually) for sellerAlice
//        Address newAddress = new Address(10, 12); // Street=10, Avenue=12
//        Property newApartment = new Apartment(
//                90,                 // sizeInSquareMeters
//                450_000,           // price
//                newAddress,        // address
//                false,             // isSold
//                sellerAlice        // owner
//        );
//
//        System.out.println("\n--- Adding a New Property Manually ---");
//        try {
//            propertyManager.addProperty(newApartment);
//            System.out.println("New property added successfully.");
//        } catch (DuplicateAddressException | SubUnitException e) {
//            System.err.println("Error adding new property: " + e.getMessage());
//        }
//
//        // List all properties again after adding
//        System.out.println("\n--- Listing All Properties After Manual Addition ---");
//        propertyManager.listAllProperties();
//
//        // 5. Broker tries to edit the newly added property
//        System.out.println("\n--- Broker Attempts to Edit a Property ---");
//        try {
//            // This will only succeed if the property's owner is associated with the same broker
//            brokerJohn.editProperty(newApartment, 470_000, 95, null);
//        } catch (UnauthorizedEditException | PropertyNotFoundException e) {
//            System.err.println("Edit failed: " + e.getMessage());
//        }
//
//        // 6. Broker tries to sell the property to buyerTom
//        System.out.println("\n--- Broker Attempts to Sell a Property ---");
//        try {
//            // We'll add a couple of services as an example
//            boolean useCleaningService = true;
//            boolean useMovingService   = false;
//            boolean useSuretyService   = true;
//            boolean useDesignService   = false;
//
//            brokerJohn.sellProperty(
//                    brokerJohn,
//                    newApartment,
//                    buyerTom,
//                    useCleaningService,
//                    useMovingService,
//                    useSuretyService,
//                    useDesignService
//            );
//        } catch (UnauthorizedEditException | PropertyNotFoundException | InsufficientFundsException | UnavailableProperty e) {
//            System.err.println("Sell failed: " + e.getMessage());
//        }
//
//        // 7. Show final status of all properties
//        System.out.println("\n--- Final List of All Properties ---");
//        propertyManager.listAllProperties();
//
//        // 8. Seller tries to delete a property
//        System.out.println("\n--- Seller Attempts to Delete a Property ---");
//        try {
//            sellerOri.deleteProperty(newApartment);
//        } catch (UnauthorizedDeleteException | PropertyNotFoundException e) {
//            System.err.println("Delete failed: " + e.getMessage());
//        }
//
//        // 9. Show final status of all properties after delete attempt
//        System.out.println("\n--- Final List of All Properties After Delete Attempt ---");
//        propertyManager.listAllProperties();
//    }
    }
}


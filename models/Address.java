package models;

import users.*;

// Address Class - encapsulates address details
public class Address {
    private int street;
    private int avenue;
    private int[] subdivisions;

    public Address(int street, int avenue, int... subdivisions) {
        this.street = street;
        this.avenue = avenue;
        this.subdivisions = subdivisions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        if (street != other.street || avenue != other.avenue || subdivisions.length != other.subdivisions.length) {
            return false;
        }
        for (int i = 0; i < subdivisions.length; i++) {
            if (subdivisions[i] != other.subdivisions[i]) {
                return false;
            }
        }
        return true;
    }

    // בודקת אם הנכס הוא תת-יחידה של נכס אחר
    public boolean isSubUnitOf(Address other) {
        if (this.street != other.street || this.avenue != other.avenue) {
            return false;
        }
        if (this.subdivisions.length <= other.subdivisions.length) {
            return false;
        }
        for (int i = 0; i < other.subdivisions.length; i++) {
            if (this.subdivisions[i] != other.subdivisions[i]) {
                return false;
            }
        }
        return true;
    }

    public double calculateDistance(Address other) {
        return Math.sqrt(Math.pow(this.street - other.street, 2) + Math.pow(this.avenue - other.avenue, 2));
    }

    @Override
    public String toString() {
        StringBuilder address = new StringBuilder(street + "," + avenue);
        for (int sub : subdivisions) {
            address.append(",").append(sub);
        }
        return address.toString();
    }

    public static Address fromString(String addressString) {
        // Check for null or empty input
        if (addressString == null || addressString.trim().isEmpty()) {
            System.out.println("Address string is empty");
            return null;
        }

        String[] parts = addressString.split(",");

        // Ensure there are at least 2 parts (street and avenue)
        if (parts.length < 2) {
            System.out.println("Invalid address format");
            return null;
        }

        try {
            int street = Integer.parseInt(parts[0].trim());
            int avenue = Integer.parseInt(parts[1].trim());

            // Parse subdivisions (if any)
            int[] subdivisions = new int[parts.length - 2];
            for (int i = 2; i < parts.length; i++) {
                subdivisions[i - 2] = Integer.parseInt(parts[i].trim());
            }

            return new Address(street, avenue, subdivisions);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in address string");
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error while parsing address");
            return null;
        }
    }

}
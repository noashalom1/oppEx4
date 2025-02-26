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
    // בודקת האם הנכס הוא יחידה ראשית (ללא תתי-יחידות)
    public boolean isMainUnit() {
        return subdivisions.length == 0;
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
        String[] parts = addressString.split(",");
        int street = Integer.parseInt(parts[0]);
        int avenue = Integer.parseInt(parts[1]);
        int[] subdivisions = new int[parts.length - 2];
        for (int i = 2; i < parts.length; i++) {
            subdivisions[i - 2] = Integer.parseInt(parts[i]);
        }
        return new Address(street, avenue, subdivisions);
    }
}
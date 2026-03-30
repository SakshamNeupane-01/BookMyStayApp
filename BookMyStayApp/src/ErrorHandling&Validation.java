import java.util.HashMap;
import java.util.Map;

/* ============================
   Custom Exception
   ============================ */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/* ============================
   Inventory Management
   ============================ */
class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 1);
        roomAvailability.put("Suite", 1);
    }

    public boolean roomTypeExists(String roomType) {
        return roomAvailability.containsKey(roomType);
    }

    public int getAvailableRooms(String roomType) {
        return roomAvailability.get(roomType);
    }

    public void reserveRoom(String roomType) throws InvalidBookingException {
        int available = roomAvailability.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException(
                "No available rooms for room type: " + roomType
            );
        }

        roomAvailability.put(roomType, available - 1);
    }
}

/* ============================
   Validator (Fail-Fast)
   ============================ */
class InvalidBookingValidator {

    public static void validate(String guestName, String roomType, Inventory inventory)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!inventory.roomTypeExists(roomType)) {
            throw new InvalidBookingException(
                "Invalid room type: " + roomType
            );
        }
    }
}

/* ============================
   Main Class
   ============================ */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        Inventory inventory = new Inventory();

        // Simulated guest inputs
        processBooking("Alice", "Deluxe", inventory);
        processBooking("Bob", "Deluxe", inventory);   // should fail (inventory)
        processBooking("", "Standard", inventory);    // invalid name
        processBooking("Charlie", "Luxury", inventory); // invalid room type
        processBooking("David", "Suite", inventory);  // valid

        System.out.println("\nSystem continues running safely.");
    }

    private static void processBooking(String guestName, String roomType, Inventory inventory) {

        try {
            // Fail-fast validation
            InvalidBookingValidator.validate(guestName, roomType, inventory);

            // Guard inventory state
            inventory.reserveRoom(roomType);

            System.out.println(
                "Booking confirmed for " + guestName + " (" + roomType + ")"
            );

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

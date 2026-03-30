import java.util.*;

/* ============================
   Reservation
   ============================ */
class Reservation {
    private int bookingId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(int bookingId, String guestName, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "BookingID=" + bookingId +
               ", Guest=" + guestName +
               ", RoomType=" + roomType +
               ", RoomID=" + roomId;
    }
}

/* ============================
   Inventory
   ============================ */
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public Inventory() {
        availability.put("Standard", 1);
        availability.put("Deluxe", 1);
    }

    public boolean hasRoom(String roomType) {
        return availability.get(roomType) > 0;
    }

    public void allocateRoom(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }

    public void restoreRoom(String roomType) {
        availability.put(roomType, availability.get(roomType) + 1);
    }

    public void printInventory() {
        System.out.println("Inventory Status: " + availability);
    }
}

/* ============================
   Booking History
   ============================ */
class BookingHistory {
    private Map<Integer, Reservation> confirmedBookings = new HashMap<>();

    public void addReservation(Reservation reservation) {
        confirmedBookings.put(reservation.getBookingId(), reservation);
    }

    public Reservation getReservation(int bookingId) {
        return confirmedBookings.get(bookingId);
    }

    public void removeReservation(int bookingId) {
        confirmedBookings.remove(bookingId);
    }
}

/* ============================
   Cancellation Service
   ============================ */
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(
            int bookingId,
            BookingHistory history,
            Inventory inventory) {

        Reservation reservation = history.getReservation(bookingId);

        // Validation
        if (reservation == null) {
            System.out.println("Cancellation failed: Booking does not exist.");
            return;
        }

        // Step 1: Record rollback info (LIFO)
        rollbackStack.push(reservation.getRoomId());

        // Step 2: Restore inventory
        inventory.restoreRoom(reservation.getRoomType());

        // Step 3: Remove booking from history
        history.removeReservation(bookingId);

        // Step 4: Complete rollback
        String releasedRoom = rollbackStack.pop();

        System.out.println(
            "Booking cancelled successfully. Room released: " + releasedRoom
        );
    }
}

/* ============================
   Main Class
   ============================ */
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        // Simulate confirmed booking
        if (inventory.hasRoom("Deluxe")) {
            inventory.allocateRoom("Deluxe");

            Reservation r1 = new Reservation(
                201, "Alice", "Deluxe", "D-101"
            );
            history.addReservation(r1);

            System.out.println("Booking confirmed: " + r1);
        }

        inventory.printInventory();

        // Valid cancellation
        cancellationService.cancelBooking(201, history, inventory);
        inventory.printInventory();

        // Invalid cancellation (already cancelled)
        cancellationService.cancelBooking(201, history, inventory);

        System.out.println("\nSystem remains consistent and stable.");
    }
}

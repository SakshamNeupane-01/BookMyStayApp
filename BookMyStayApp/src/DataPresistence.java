import java.io.*;
import java.util.*;

/* ============================
   Reservation (Serializable)
   ============================ */
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookingId;
    private String guestName;
    private String roomType;

    public Reservation(int bookingId, String guestName, String roomType) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "BookingID=" + bookingId +
               ", Guest=" + guestName +
               ", RoomType=" + roomType;
    }
}

/* ============================
   Inventory (Serializable)
   ============================ */
class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public Inventory() {
        availability.put("Standard", 2);
        availability.put("Deluxe", 1);
    }

    public void allocate(String roomType) {
        int count = availability.get(roomType);
        if (count > 0) {
            availability.put(roomType, count - 1);
        }
    }

    public void printInventory() {
        System.out.println("Inventory: " + availability);
    }
}

/* ============================
   System State Wrapper
   ============================ */
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Inventory inventory;
    List<Reservation> bookingHistory;

    public SystemState(Inventory inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

/* ============================
   Persistence Service
   ============================ */
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    public static void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Failed to save system state.");
        }
    }

    public static SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state restored from file.");
            return (SystemState) in.readObject();

        } catch (Exception e) {
            System.out.println(
                "No valid saved state found. Starting with fresh system."
            );
            return null;
        }
    }
}

/* ============================
   Main Class
   ============================ */
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Restore state on startup
        SystemState recoveredState = PersistenceService.load();

        Inventory inventory;
        List<Reservation> bookingHistory;

        if (recoveredState != null) {
            inventory = recoveredState.inventory;
            bookingHistory = recoveredState.bookingHistory;
        } else {
            inventory = new Inventory();
            bookingHistory = new ArrayList<>();
        }

        // Current system state
        System.out.println("\n--- Current System State ---");
        inventory.printInventory();
        System.out.println("Bookings: " + bookingHistory);

        // Simulate new booking
        Reservation r1 = new Reservation(401, "Alice", "Standard");
        bookingHistory.add(r1);
        inventory.allocate("Standard");

        System.out.println("\n--- After New Booking ---");
        inventory.printInventory();
        System.out.println("Bookings: " + bookingHistory);

        // Persist state before shutdown
        SystemState currentState =
            new SystemState(inventory, bookingHistory);

        PersistenceService.save(currentState);

        System.out.println("\nSystem shutdown completed safely.");
    }
}

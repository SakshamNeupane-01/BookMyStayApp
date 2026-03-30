import java.util.LinkedList;
import java.util.Queue;

/* ============================
   Booking Request
   ============================ */
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

/* ============================
   Inventory (Shared Resource)
   ============================ */
class Inventory {
    private int availableRooms;

    public Inventory(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    // Critical section
    public synchronized boolean allocateRoom(String guestName) {
        if (availableRooms > 0) {
            System.out.println(guestName + " successfully booked a room.");
            availableRooms--;
            return true;
        } else {
            System.out.println(guestName + " failed to book (No rooms left).");
            return false;
        }
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}

/* ============================
   Concurrent Booking Processor
   ============================ */
class BookingProcessor implements Runnable {

    private Queue<BookingRequest> bookingQueue;
    private Inventory inventory;

    public BookingProcessor(Queue<BookingRequest> bookingQueue, Inventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Synchronized access to shared queue
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    return;
                }
                request = bookingQueue.poll();
            }

            // Allocate room (thread-safe)
            inventory.allocateRoom(request.guestName);
        }
    }
}

/* ============================
   Main Class
   ============================ */
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {

        // Shared booking queue
        Queue<BookingRequest> bookingQueue = new LinkedList<>();

        bookingQueue.add(new BookingRequest("Alice", "Deluxe"));
        bookingQueue.add(new BookingRequest("Bob", "Deluxe"));
        bookingQueue.add(new BookingRequest("Charlie", "Deluxe"));
        bookingQueue.add(new BookingRequest("David", "Deluxe"));

        // Only 2 rooms available
        Inventory inventory = new Inventory(2);

        // Multiple threads (guests)
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory));

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        t1.join();
        t2.join();
        t3.join();

        System.out.println(
            "\nFinal available rooms: " + inventory.getAvailableRooms()
        );
        System.out.println("System state is consistent.");
    }
}

import java.util.ArrayList;
import java.util.List;

/* =======================
   Reservation (Entity)
   ======================= */
class Reservation {
    private int bookingId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(int bookingId, String customerName, String roomType, int nights) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
               ", Customer: " + customerName +
               ", Room Type: " + roomType +
               ", Nights: " + nights;
    }
}

/* =======================
   Booking History
   ======================= */
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Store confirmed reservation
    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    // Read-only access
    public List<Reservation> getAllReservations() {
        return confirmedBookings;
    }
}

/* =======================
   Booking Report Service
   ======================= */
class BookingReportService {

    public void displayAllBookings(BookingHistory history) {
        System.out.println("\n--- Booking History Report ---");

        List<Reservation> reservations = history.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void generateSummaryReport(BookingHistory history) {
        List<Reservation> reservations = history.getAllReservations();

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Confirmed Bookings: " + reservations.size());
    }
}

/* =======================
   Main Class
   ======================= */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Booking history (acts like persistence)
        BookingHistory bookingHistory = new BookingHistory();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation(101, "Alice", "Deluxe", 3);
        Reservation r2 = new Reservation(102, "Bob", "Standard", 2);
        Reservation r3 = new Reservation(103, "Charlie", "Suite", 5);

        // Add confirmed bookings to history
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        // Admin requests reports
        BookingReportService reportService = new BookingReportService();

        reportService.displayAllBookings(bookingHistory);
        reportService.generateSummaryReport(bookingHistory);
    }
}

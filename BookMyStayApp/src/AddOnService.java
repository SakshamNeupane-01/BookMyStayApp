import java.util.*;

/* ---------------- Add-On Service ---------------- */
class AddOnService {
    private String serviceName;
    private double serviceCost;

    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }
}

/* -------- Add-On Service Manager ---------------- */
class AddOnServiceManager {

    // One-to-Many: Reservation ID → List of Services
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Attach a service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get all services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate additional cost
    public double calculateAdditionalCost(String reservationId) {
        double total = 0.0;
        for (AddOnService service : getServices(reservationId)) {
            total += service.getServiceCost();
        }
        return total;
    }
}

/* ---------------- Main Driver ------------------- */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Existing confirmed booking (UNCHANGED)
        Map<String, Double> bookingBaseCost = new HashMap<>();
        bookingBaseCost.put("RES101", 5000.0);

        // Add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 800);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1500);
        AddOnService spa = new AddOnService("Spa Access", 2000);

        // Service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects services
        serviceManager.addService("RES101", breakfast);
        serviceManager.addService("RES101", airportPickup);
        serviceManager.addService("RES101", spa);

        // Cost calculation
        double baseCost = bookingBaseCost.get("RES101");
        double addOnCost = serviceManager.calculateAdditionalCost("RES101");
        double finalCost = baseCost + addOnCost;

        // Output
        System.out.println("Reservation ID: RES101");
        System.out.println("Base Booking Cost: ₹" + baseCost);
        System.out.println("Add-On Services:");

        for (AddOnService service : serviceManager.getServices("RES101")) {
            System.out.println(" - " + service.getServiceName() +
                    " : ₹" + service.getServiceCost());
        }

        System.out.println("Total Add-On Cost: ₹" + addOnCost);
        System.out.println("Final Payable Amount: ₹" + finalCost);
    }
}

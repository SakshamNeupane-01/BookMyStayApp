public class BookingApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay (v4.0) =====\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Create room objects (domain data)
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory, rooms);

        // Perform search (READ-ONLY)
        searchService.searchAvailableRooms();

        System.out.println("\nSearch completed. No data was modified.");
    }
}
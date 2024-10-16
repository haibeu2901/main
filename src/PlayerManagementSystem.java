import java.io.*;
import java.util.*;

public class PlayerManagementSystem {
    private static final String FILE_NAME = "data.txt";
    private static List<Player> playerList = new ArrayList<>();

    public static void main(String[] args) {
        loadPlayers();

        // Simulated interaction loop (you'd implement an actual web server interaction)
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Show Player List");
            System.out.println("2. Add New Player");
            System.out.println("3. Update Player");
            System.out.println("4. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    printPlayers();
                    break;
                case 2:
                    addPlayer(scanner);
                    break;
                case 3:
                    updatePlayer(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadPlayers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Player player = new Player(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]);
                playerList.add(player);
            }
        } catch (IOException e) {
            System.out.println("Could not load players: " + e.getMessage());
        }
    }

    private static void printPlayers() {
        if (playerList.isEmpty()) {
            System.out.println("No players found.");
        } else {
            System.out.printf("%-10s %-15s %-15s %-15s %-10s %-10s%n", "ID", "Real Name", "Shirt Number", "Shirt Name", "Size", "Note");
            System.out.println("-----------------------------------------------------------------");
            playerList.forEach(player -> System.out.printf("%-10s %-15s %-15d %-15s %-10s %-10s%n",
                    player.getId(), player.getRealName(), player.getShirtNumber(), player.getShirtName(), player.getSize(), player.getNote()));
        }
    }

    private static void addPlayer(Scanner scanner) {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        if (playerList.stream().anyMatch(p -> p.getId().equals(id))) {
            System.out.println("Player with this ID already exists.");
            return;
        }

        System.out.print("Enter Real Name: ");
        String realName = scanner.nextLine();
        System.out.print("Enter Shirt Number: ");
        int shirtNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (playerList.stream().anyMatch(p -> p.getShirtNumber() == shirtNumber)) {
            System.out.println("Player with this shirt number already exists.");
            return;
        }

        System.out.print("Enter Shirt Name: ");
        String shirtName = scanner.nextLine();
        System.out.print("Enter Size: ");
        String size = scanner.nextLine();
        System.out.print("Enter Note: ");
        String note = scanner.nextLine();

        Player newPlayer = new Player(id, realName, shirtNumber, shirtName, size, note);
        playerList.add(newPlayer);
        savePlayers();
        System.out.println("Player added successfully.");
    }

    private static void updatePlayer(Scanner scanner) {
        System.out.print("Enter ID or Shirt Number of the player to update: ");
        String input = scanner.nextLine();
        
        Player player = playerList.stream()
            .filter(p -> p.getId().equals(input) || String.valueOf(p.getShirtNumber()).equals(input))
            .findFirst().orElse(null);
        
        if (player == null) {
            System.out.println("Player not found.");
            return;
        }
        
        System.out.print("Enter new Real Name: ");
        String realName = scanner.nextLine();
        System.out.print("Enter new Shirt Number: ");
        int shirtNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (playerList.stream().anyMatch(p -> p.getShirtNumber() == shirtNumber && !p.getId().equals(player.getId()))) {
            System.out.println("Player with this shirt number already exists.");
            return;
        }

        System.out.print("Enter new Shirt Name: ");
        String shirtName = scanner.nextLine();
        System.out.print("Enter new Size: ");
        String size = scanner.nextLine();
        System.out.print("Enter new Note: ");
        String note = scanner.nextLine();

        player.setRealName(realName);
        player.setShirtNumber(shirtNumber);
        player.setShirtName(shirtName);
        player.setSize(size);
        player.setNote(note);
        savePlayers();
        System.out.println("Player updated successfully.");
    }

    private static void savePlayers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Player player : playerList) {
                bw.write(player.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save players: " + e.getMessage());
        }
    }
}

class Player {
    private String id;
    private String realName;
    private int shirtNumber;
    private String shirtName;
    private String size;
    private String note;

    public Player(String id, String realName, int shirtNumber, String shirtName, String size, String note) {
        this.id = id;
        this.realName = realName;
        this.shirtNumber = shirtNumber;
        this.shirtName = shirtName;
        this.size = size;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public void setShirtName(String shirtName) {
        this.shirtName = shirtName;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return id + "," + realName + "," + shirtNumber + "," + shirtName + "," + size + "," + note;
    }
}

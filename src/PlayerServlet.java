import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/PlayerServlet")
public class PlayerServlet extends HttpServlet {
    private static final String FILE_NAME = "data.txt";
    private List<Player> playerList = new ArrayList<>();
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        loadPlayers();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("show".equals(action)) {
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(playerList));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addPlayer(request, response);
        } else if ("update".equals(action)) {
            updatePlayer(request, response);
        }
    }

    private void loadPlayers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Player player = new Player(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4], data[5]);
                playerList.add(player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPlayer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        Player newPlayer = gson.fromJson(reader, Player.class);
        
        if (playerList.stream().anyMatch(p -> p.getId().equals(newPlayer.getId()))) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Player with this ID already exists.");
            return;
        }
        
        if (playerList.stream().anyMatch(p -> p.getShirtNumber() == newPlayer.getShirtNumber())) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Player with this shirt number already exists.");
            return;
        }

        playerList.add(newPlayer);
        savePlayers();
        response.getWriter().write("{\"message\":\"Player added successfully.\"}");
    }

    private void updatePlayer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        Map<String, String> updateData = gson.fromJson(reader, Map.class);

        String idOrShirtNumber = updateData.get("idOrShirtNumber");
        Player player = playerList.stream()
                .filter(p -> p.getId().equals(idOrShirtNumber) || String.valueOf(p.getShirtNumber()).equals(idOrShirtNumber))
                .findFirst().orElse(null);
        
        if (player == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Player not found.");
            return;
        }

        if (playerList.stream().anyMatch(p -> p.getShirtNumber() == Integer.parseInt(updateData.get("shirtNumber")) && !p.getId().equals(player.getId()))) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Player with this shirt number already exists.");
            return;
        }

        player.setRealName(updateData.get("realName"));
        player.setShirtNumber(Integer.parseInt(updateData.get("shirtNumber")));
        player.setShirtName(updateData.get("shirtName"));
        player.setSize(updateData.get("size"));
        player.setNote(updateData.get("note"));
        savePlayers();

        response.getWriter().write("{\"message\":\"Player updated successfully.\"}");
    }

    private void savePlayers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Player player : playerList) {
                bw.write(player.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


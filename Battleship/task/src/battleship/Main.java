package battleship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Util {
    static int[] parseCoordinates(String co) throws IllegalArgumentException {
        int[] coordinates = new int[2];
        if (co != null && co.length() <= 3) {
            char letter = co.charAt(0);
            if (letter >= 'A' && letter <= 'J') {
                coordinates[0] = letter;
            } else {
                throw new IllegalArgumentException("First digit must be a letter [A...J] - " + letter);
            }
            int number = Integer.parseInt(co.substring(1));
            if (number >= 1 && number <= 10) {
                coordinates[1] = number;
            } else {
                throw new IllegalArgumentException("Second digit must be a number [1...10] - " + number);
            }
        } else {
            throw new IllegalArgumentException("Wrong format - " + co);
        }
        return coordinates;
    }

    public static int[] targetCell() {
        int[] cell = null;
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.next();
                cell = Util.parseCoordinates(input);
                System.out.println();
            } catch (IllegalArgumentException ex) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                System.out.println();
            }
        } while (cell == null);
        return cell;
    }

    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            //  Handle any exceptions.
        }
    }

    public static void promptEnterKey() {
        System.out.println("\nPress Enter and pass the move to another player");
        try {
            System.in.read();
            clearConsole();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}

class Player {

    private static final List<Player> players = new ArrayList<>();
    private final int id;
    private final int[][] battleField = new int[10][10];
    private final List<Ship> fleet = new ArrayList<>();

    public Player(int id) {
        this.id = id;
        setUpFleet();
        initialiseBattleField();
        players.add(this);
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static Player getEnemy(Player player) {
        Player enemy = player;
        for (Player p : Player.getPlayers()) {
            if (p.getId() != player.getId()) {
                enemy = p;
            }
        }
        return enemy;
    }

    public List<Ship> getFleet() {
        return fleet;
    }

    public static Player turnPlayers(Player player) {
        Util.promptEnterKey();
        Player active = getEnemy(player);
        player.printFogOfWar(player.getBattleField());
        System.out.println("---------------------");
        active.printBattleField();
        System.out.printf("\nPlayer %d, it's your turn:\n\n", active.getId());
        return active;
    }


    public void placeShips() {
        System.out.printf("\nPlayer %d, place your ships on the dame filed\n\n", id);
        printBattleField();
        for (Ship ship : fleet) {
            ship.enterCoordinates();
            boolean proximityAlert;
            do {
                ship.checkCoordinates();
                proximityAlert = checkProximity(ship);
            } while (proximityAlert);
            place(ship);
            printBattleField();
        }
    }

    public Ship checkShot(int[] cell) {
        Player enemy = Player.getEnemy(this);
        int[][] field = enemy.getBattleField();
        int x = cell[0] - 'A';
        int y = cell[1] - 1;
        int cellValue = field[x][y];
        if (cellValue == '~') {
            field[x][y] = 'M';
            return null;
        } else {
            field[x][y] = 'X';
            return hitShip(cell);
        }
    }

    public int[][] getBattleField() {
        return battleField;
    }

    public void printBattleField() {
        printField(battleField);
    }

    public void printFogOfWar(int[][] field) {
        int[][] fogOfWar = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                fogOfWar[i][j] = field[i][j] == 'O' ? '~' : field[i][j];
            }
        }
        printField(fogOfWar);
    }

    public int getId() {
        return id;
    }

    private Ship hitShip(int[] cell) {
        Player enemy = getEnemy(this);
        int x = cell[0] - 'A';
        int y = cell[1] - 1;
        for (Ship s : enemy.getFleet()) {
            if (s.getX1() == s.getX2()) {
                int y1 = Math.min(s.getY1(), s.getY2());
                int y2 = Math.max(s.getY1(), s.getY2());
                if (s.getX1() == x && y1 <= y && y <= y2) {
                    s.countHit();
                    return s;
                }
            }
            if (s.getY1() == s.getY2()) {
                int x1 = Math.min(s.getX1(), s.getX2());
                int x2 = Math.max(s.getX1(), s.getX2());
                if (s.getY1() == y && x1 <= x && x <= x2) {
                    s.countHit();
                    return s;
                }
            }
        }
        return null;
    }

    private void printField(int[][] field) {
        System.out.print("  ");
        for (int h = 0; h < 10; h++) {
            System.out.print(h + 1 + " ");
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print((char) (i + 'A') + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print((char) field[i][j] + " ");
            }
            System.out.print('\n');
        }
    }

    private void initialiseBattleField() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                battleField[i][j] = '~';
            }
        }
    }

    private void place(Ship ship) {
        int x1 = ship.getX1();
        int y1 = ship.getY1();
        int x2 = ship.getX2();
        int y2 = ship.getY2();

        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                battleField[x1][i] = 'O';
            }
        } else if (y1 == y2) {
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                battleField[i][y1] = 'O';
            }
        }
    }

    private boolean checkProximity(Ship ship) {
        boolean proximityAlert = false;
        int xl = Math.min(ship.getX1(), ship.getX2());
        int xh = Math.max(ship.getX1(), ship.getX2());
        int yl = Math.min(ship.getY1(), ship.getY2());
        int yh = Math.max(ship.getY1(), ship.getY2());

        xl = xl > 0 ? --xl : 0;
        xh = xh < 9 ? ++xh : 9;
        yl = yl > 0 ? --yl : 0;
        yh = yh < 9 ? ++yh : 9;

        for (int i = xl; i <= xh; i++) {
            for (int j = yl; j <= yh; j++) {
                if (battleField[i][j] == 'O') {
                    System.out.println("\nError! You placed it too close to another one. Try again:");
                    System.out.println();
                    proximityAlert = true;
                    break;
                }
            }
        }
        return proximityAlert;
    }

    private void setUpFleet() {
        fleet.add(new AircraftCarrier());
        fleet.add(new Battleship());
        fleet.add(new Submarine());
        fleet.add(new Cruiser());
        fleet.add(new Destroyer());
    }

    public boolean allSunk() {
        Player enemy = Player.getEnemy(this);
        boolean allSunk = true;
        for (Ship s : enemy.getFleet()) {
            if (!s.isSunk()) {
                allSunk = false;
                break;
            }
        }
        return allSunk;
    }
}

abstract class Ship {
    private final String type;
    private final int cells;
    int[] coordinates1 = new int[2];
    int[] coordinates2 = new int[2];
    private int hitCells;
    private boolean isSunk = false;

    public Ship(String type, int cells) {
        this.type = type;
        this.cells = cells;
    }

    public void enterCoordinates() {
        System.out.printf("\nEnter the coordinates of the %s (%d cells):\n\n", getType(), getCells());
    }

    public void checkCoordinates() {
        do {
            try {
                Scanner scanner = new Scanner(System.in);
                String co1 = scanner.next();
                String co2 = scanner.next();
                coordinates1 = Util.parseCoordinates(co1);
                coordinates2 = Util.parseCoordinates(co2);
                System.out.println();
            } catch (IllegalArgumentException ex) {
                System.out.println("Error! Wrong ship location! Try again:");
                System.out.println();
            }
        }
        while (!checkLength());
    }

    public String getType() {
        return type;
    }

    public int getCells() {
        return cells;
    }

    public int getX1() {
        return coordinates1[0] - 'A';
    }

    public int getY1() {
        return coordinates1[1] - 1;
    }

    public int getX2() {
        return coordinates2[0] - 'A';
    }

    public int getY2() {
        return coordinates2[1] - 1;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void countHit() {
        ++hitCells;
        if (hitCells == cells) {
            isSunk = true;
        }
    }

    private boolean checkLength() {
        int length;
        if (coordinates1[0] == coordinates2[0]) {
            length = Math.abs(coordinates1[1] - coordinates2[1]) + 1;
        } else if (coordinates1[1] == coordinates2[1]) {
            length = Math.abs(coordinates1[0] - coordinates2[0]) + 1;
        } else {
            System.out.println("Error! Wrong ship location! Try again:");
            System.out.println();
            return false;
        }
        boolean checkLength = getCells() == length;
        if (!checkLength) {
            System.out.println("Error! Wrong length of the " + getType() + "! Try again:");
            System.out.println();
        }
        return checkLength;
    }
}

class AircraftCarrier extends Ship {
    static final String type = "Aircraft Carrier";
    static final int cells = 5;

    AircraftCarrier() {
        super(type, cells);
    }
}

class Battleship extends Ship {
    static final String type = "Battleship";
    static final int cells = 4;

    Battleship() {
        super(type, cells);
    }
}

class Submarine extends Ship {
    static final String type = "Submarine";
    static final int cells = 3;

    Submarine() {
        super(type, cells);
    }
}

class Cruiser extends Ship {
    static final String type = "Cruiser";
    static final int cells = 3;

    Cruiser() {
        super(type, cells);
    }
}

class Destroyer extends Ship {
    static final String type = "Destroyer";
    static final int cells = 2;

    Destroyer() {
        super(type, cells);
    }
}

public class Main {

    public static void main(String[] args) {

        Player player1 = new Player(1);
        player1.placeShips();
        Util.promptEnterKey();
        Player player2 = new Player(2);
        player2.placeShips();
        Player active = Player.turnPlayers(player2);

        do {
            Ship victim = active.checkShot(Util.targetCell());
            if (victim != null) {
                if (active.allSunk())
                    break;
                if (victim.isSunk()) {
                    System.out.println("You sank a ship!");
                    System.out.println();
                } else {
                    System.out.println("You hit a ship!");
                    System.out.println();
                }
            } else {
                System.out.println("You missed!");
                System.out.println();
            }
            active = Player.turnPlayers(active);
        } while (true);
        System.out.println("You sank the last ship. You won. Congratulations!");
    }
}

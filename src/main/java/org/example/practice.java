package org.example;

/*
Feladat háttér story: Az egyik haverod imádja a számítogépes játokokat. Megkér, hogy készíts neki egy java programot amivel számon tudja tartani, hogy milyen játékok vannak meg neki és melyik játékkal mennyit játszott.

        Task 1: Hozz létre psql-be egy adatbázis felhasználót `gamer` néven, egy álatalad választot jelszóval.
        psql -U postgres -h 127.0.0.1
        CREATE USER gamer WITH PASSWORD 'gamer123';

        Task 2: Készíts egy adatbázist `gamedb`. A `gamer` felhasználónak legyen meg minden jogosultsága a `gamedb`-n.
        Task 3: A `gamer` felhasználót használva hozz létre egy új táblát a `gamedb`-ben. A tábla a következő tulajdonságokkal rendelkezzen:
        - a neve legyen `game`.
        - legyen egy `id` nevű oszlopa. Ez az oszlop legyen a tábla elsödleges kulcsa.
        Automatikusan kapjon értéket, számokat tartalmazzon.
        - legyen egy `name`nevű oszlopa, ennek tipusa legyen `VARCHAR(50)`.
        - legyen egy `hours_in_game`nevű oszlopa, ennek tipusa legyen `INTEGER`.
        - legyen egy `category`nevű oszlopa, ennek tipusa legyen `VARCHAR(50)`.
        - legyen egy `release_date` nevű oszlopa aminek tipusa `DATE`.
        - legyen egy `description`nevű oszlopa, ennek tipusa legyen `VARCHAR(250)`.
        - legyen egy `developer`nevű oszlopa, ennek tipusa legyen `VARCHAR(50)`.
        - legyen egy `currently_installed` nevű oszlopa, ennek tipusa legyen `BOOLEAN`.

        Task 5: készítsünk egy java alkalmazást ami képes csatlakozni az adatbázishoz. két menüpontja legyen:
- `1: List all games` ez listázza ki az összes játék összes adatát.
- `2: Exit`

Task 6: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program listázza ki az összes játékot név szerint sorba rendezve. get all. név szerinti sorban
Task 7: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program adjon hozzá az adatbázishoz egy új játékot a user általl megadott adatokkal, majd írja ki a generált id-t. insert
Task 8: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program törli a user által megadott id-val rendelkező játékot.

házi:
Task 9: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program kérjen el a usertől egy id-t és hogy az adott id-ju játék telepített e. A kapott adatok alapján updatelje a db-t.
Task 10: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program írja ki annak a játéknak a nevét amivel a user a legtöbbet játszott. (legtöbb hours_in_game)
Task 11: ugyanaz mint a task 9 de csak a telepített játékok közül.
Task 12: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program írja ki a user által megadott kategoriába tartozó játékokat.
Task 13: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program írja ki azokat a játékokat amik leírása tartalmazza a user által megadott szövegrészletet.
Task 14: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program írja ki a user által megadott két dátum között kiadott játékoka.
Task 15: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program írja ki a hogy milyen fejlesztőktől van telepített játék a felhasználó gépén. Minden eredmény csak egyszer jelenjen meg.
Task 16: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program listázza, hogy a user milyen categoriájú játékkal mennyit játszott.
Task 17: adjunk a programunkhoz egy új menüpontot. Ha a user ezt választja akkor a program írja ki sorban a fejlesztőket játékidő szerint.
Task 18: Az alkalmazás a környezeti változokból vegye a felhasználó nevet és jelszót.*/

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class practice {
    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose option!");
        System.out.println("1. List all games!");
        System.out.println("2. get all");
        System.out.println("3. add game");
        System.out.println("4. delete game");
        System.out.println("5. installed game");
        System.out.println("6. most played game");
        System.out.println("7. same as 5. but installed game");
        System.out.println("8. category game");
        System.out.println("9. list game by description");
        System.out.println("10. list game by between date");
        System.out.println("11. installed game by developers game");
        System.out.println("12. Most played category");
        System.out.println("13. Developers by played times");
        System.out.println("Q. Exit");

        String input = scanner.nextLine();

        switch (input) {
            case "1":
                listAllgames();
                mainMenu();
                break;
            case "2":
                getAll();
                mainMenu();
                break;
            case "3":
                addGame();
                mainMenu();
                break;
            case "4":
                deleteGame();
                mainMenu();
                break;
            case "5":
                installedGame();
                mainMenu();
                break;
            case "6":
                mostPlayedGame();
                mainMenu();
                break;
            case "7":
                installedGamever2();
                mainMenu();
                break;
            case "8":
                categoryGame();
                mainMenu();
                break;
            case "9":
                listGameDescription();
                mainMenu();
                break;
            case "10":
                listGameByDates();
                mainMenu();
                break;
            case "11":
                installedGameByDevelopers();
                mainMenu();
                break;
            case "12":
                mostPlayedCategory();
                mainMenu();
                break;
            case "13":
                developersByGameTime();
                mainMenu();
                break;
            default:
                ;
        }
    }
    private static void installedGame() {
    }
    private static void installedGamever2() {
    }

    private static void mostPlayedCategory() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "SELECT category, sum(hours_in_game) as Total_played_time FROM game group by category order by Total_played_time desc;";
            PreparedStatement st = con.prepareStatement(SQL);

            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1) + " " + rs.getString(2));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void installedGameByDevelopers() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "SELECT developer FROM game where currently_insalled = true group by developer order by developer;";
            PreparedStatement st = con.prepareStatement(SQL);

            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void listGameByDates() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");
            Scanner scanner = new Scanner(System.in);
            System.out.println("start date");
            String desc = scanner.nextLine();
            System.out.println("end date");
            String descend = scanner.nextLine();

            final String SQL = "SELECT name FROM game where release_date between ? and ?;";

            PreparedStatement st = con.prepareStatement(SQL);
            st.setDate(1, java.sql.Date.valueOf(desc));
            st.setDate(2, java.sql.Date.valueOf(descend));
            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void listGameDescription() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Write description:");
            String desc = scanner.nextLine();

            final String SQL = "SELECT \"name\", description FROM game where description like concat('%',?,'%') order by game;";

            PreparedStatement st = con.prepareStatement(SQL);
            st.setString(1, desc);
            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1) + " " + rs.getString(2));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void categoryGame() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");
            System.out.println("Connection working");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Write category:");
            String desc = scanner.nextLine();

            final String SQL = "SELECT name FROM game where category = ?;";
            PreparedStatement st = con.prepareStatement(SQL);
            st.setString(1, desc);
            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mostPlayedGame() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "SELECT name FROM game order by hours_in_game desc limit 1 ;";
            PreparedStatement st = con.prepareStatement(SQL);

            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private static void developersByGameTime() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "SELECT developer, sum(hours_in_game) as Total_played_time FROM game group by developer order by Total_played_time desc;";
            PreparedStatement st = con.prepareStatement(SQL);

            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1) + " " + rs.getString(2));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteGame() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");
            Scanner scanner = new Scanner(System.in);
            int id = scanner.nextInt();


            final String SQL = "delete from game where id = ?;";
            PreparedStatement st = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, id);
            st.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addGame() {
        Long result = null;
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "insert into game (\"name\",hours_in_game,category,release_date,description,developer,currently_insalled) values('Subverse',0,'Adventure','2021-03-26','Mass effect porn game.','FOW Interactive',TRUE);";
            PreparedStatement st = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            result = rs.getLong(1);
            System.out.println(result);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getAll() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "SELECT name FROM game order by name;";
            PreparedStatement st = con.prepareStatement(SQL);

            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(rs.getString(1));

                System.out.println(row);

            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void listAllgames() {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gamedb", "gamer", "gamer123")) {
            System.out.println("Connection working");

            final String SQL = "SELECT * FROM game;";
            PreparedStatement st = con.prepareStatement(SQL);

            ResultSet rs = st.executeQuery();

            List<List<String>> allRows = new ArrayList<>();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf((rs.getInt(1))));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                row.add(rs.getString(5));
                row.add(rs.getString(6));
                row.add(rs.getString(7));

                System.out.println(row);

            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

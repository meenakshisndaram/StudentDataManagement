package StudentDataManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String[]> studentData = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 6) {
                    String name = parts[0];
                    String[] marks = new String[4];
                    marks[0] = parts[1];
                    marks[1] = parts[2];
                    marks[2] = parts[3];
                    marks[3] = parts[4];
                    String age = parts[5];
                    studentData.put(name, new String[]{marks[0], marks[1], marks[2], marks[3], age});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Data read from file and added to map:");
        for (Map.Entry<String, String[]> entry : studentData.entrySet()) {
            System.out.println("Name: " + entry.getKey());
            String[] marks = entry.getValue();
            System.out.println("Marks:");
            for (int i = 0; i < marks.length - 1; i++) {
                System.out.println(marks[i]);
            }
            System.out.println("Age: " + marks[4]);
            System.out.println();
        }

        String url = "jdbc:mysql://localhost:3306/meenakshi";
        String user = "root";
        String password = "password21";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO students (name, math, english, science, history, age) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            for (Map.Entry<String, String[]> entry : studentData.entrySet()) {
                String name = entry.getKey();
                String[] marks = entry.getValue();
                String age = marks[4];

                statement.setString(1, name);
                for (int i = 0; i < 4; i++) {
                    statement.setString(i + 2, marks[i]);
                }
                statement.setString(6, age);

                statement.executeUpdate();
            }
            System.out.println("Data inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

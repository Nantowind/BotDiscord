package org.walycorp.botdiscord.tools;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;

public class WordFilter {

    private static Dotenv config;

    public WordFilter(Dotenv config) {
        this.config = config;
    }
    public static boolean containsWord(String text) {



        config = Dotenv.configure().load();
        String USER = config.get("USERDB");
        String PASS = config.get("PassDB");

        ArrayList<String> wordList = new ArrayList<String>();
        // Conexión a la base de datos
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/badWord", USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT word FROM wordlist";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                wordList.add(rs.getString("word"));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Verificar si la cadena contiene alguna de las palabras de la lista
        for (String word : wordList) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }



}

package org.walycorp.botdiscord.tools;


import org.walycorp.botdiscord.credentials.EnvReader;

import java.sql.*;
import java.util.ArrayList;

public class WordFilter {


    public static boolean containsWord(String text) {

        String USER = EnvReader.OTHER_INFO.get("USERDB");
        String PASS = EnvReader.OTHER_INFO.get("PASSDB");

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

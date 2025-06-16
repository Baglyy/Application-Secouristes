package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportCSV {

    // Configuration de la connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost/AppSecouristes";
    private static final String DB_USER = "client";
    private static final String DB_PASSWORD = "root";

    private static final String CSV_SEPARATOR = ";"; 
    private static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String EXPORT_DIRECTORY = "csv/";


    private static String escapeData(String data) {
        if (data == null) {
            return "";
        }
        return data;
    }


    public static void exporterTouteLaBase() throws SQLException { 
        String[] tablesAExporter = {"Secouriste", "Competence", "Journee", "Site", "Sport", "DPS", "Besoin", "Possede", "Disponibilite", "Affectation"};

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace ();
        }

        for (String tableName : tablesAExporter) {
            exporterUneTable(tableName, EXPORT_DIRECTORY);
        }
        System.out.println("Exportation CSV de toutes les tables terminée.");
    }

    private static void exporterUneTable(String tableName, String directoryPath) throws SQLException {
        String csvFilePath = directoryPath + tableName + ".csv";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
             Writer writer = new BufferedWriter(new FileWriter(csvFilePath, StandardCharsets.UTF_8))) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Écrire la ligne d'en-tête
            for (int i = 1; i <= columnCount; i++) {
                writer.append(escapeData(metaData.getColumnLabel(i))); 
                if (i < columnCount) {
                    writer.append(CSV_SEPARATOR);
                }
            }
            writer.append(LINE_SEPARATOR);

            // Écrire les lignes de données
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.append(escapeData(rs.getString(i))); 
                    if (i < columnCount) {
                        writer.append(CSV_SEPARATOR);
                    }
                }
                writer.append(LINE_SEPARATOR);
            }
            writer.flush();
            System.out.println("  Table '" + tableName + "' exportée avec succès vers : " + csvFilePath);

        } catch (IOException e) {
            System.err.println("Erreur : Echec de l'exportation de la table '" + tableName + "': " + e.getMessage());
        }
    }
}

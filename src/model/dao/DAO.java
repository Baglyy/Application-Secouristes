package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe abstraite générique représentant un DAO (Data Access Object).
 * Fournit la structure de base pour les opérations CRUD et la gestion de la connexion JDBC.
 *
 * @param <T> le type d'entité manipulée par ce DAO
 */
public abstract class DAO<T> {

    /** Nom de la classe du driver JDBC */
    private static String driverClassName = "com.mysql.cj.jdbc.Driver";
    
    /** URL de connexion à la base de données */
    private static String url = "jdbc:mysql://localhost/AppSecouristes";
    
    /** Nom d'utilisateur pour la connexion à la base */
    private static String username = "client";
    
    /** Mot de passe pour la connexion à la base */
    private static String password = "root";

    /**
     * Obtient une connexion JDBC à la base de données.
     *
     * @return la connexion JDBC
     * @throws SQLException si une erreur survient pendant la connexion
     */
    protected Connection getConnection() throws SQLException {
        // Charger la classe du pilote
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        // Obtenir la connexion
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Récupère tous les éléments de la table.
     *
     * @return liste des objets de type T
     */
    public abstract List<T> findAll();

    /**
     * Recherche un élément par sa ou ses clé(s) primaire(s).
     *
     * @param keys une ou plusieurs clés pour identifier l’élément
     * @return l’objet trouvé ou null
     */
    public abstract T findByID(Object... keys);

    /**
     * Met à jour un élément existant dans la base.
     *
     * @param element l’élément à mettre à jour
     * @return nombre de lignes affectées ou -1 en cas d’échec
     */
    public abstract int update(T element);

    /**
     * Supprime un élément de la base.
     *
     * @param element l’élément à supprimer
     * @return nombre de lignes affectées ou -1 en cas d’échec
     */
    public abstract int delete(T element);

    /**
     * Crée un nouvel élément dans la base.
     *
     * @param element l’élément à insérer
     * @return nombre de lignes insérées ou -1 en cas d’échec
     */
    public abstract int create(T element);
}

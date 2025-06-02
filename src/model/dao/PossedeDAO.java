package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Possede;

public class PossedeDAO extends DAO<Possede> {

    @Override
    public List<Possede> findAll() {
        List<Possede> possedes = new LinkedList<>();
        String query = "SELECT * FROM possede";

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String intitule = rs.getString("INTITULE");
                possedes.add(new possede(intitule));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return possedes;
    }

    public possede findByID(String intitule) {
        String query = "SELECT * FROM possede WHERE INTITULE = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, intitule);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new possede(rs.getString("INTITULE"));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}

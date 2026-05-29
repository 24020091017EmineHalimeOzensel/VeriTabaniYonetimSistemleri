package org.example.digitallibraryproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeriTabaniBaglantisi {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=DigitalLibraryDB;encrypt=true;trustServerCertificate=true;";
    private static final String KULLANICI = "sa";
    private static final String SIFRE = "halime05-";

    public static Connection baglantiAl() {
        Connection baglanti = null;
        try {
            baglanti = DriverManager.getConnection(URL, KULLANICI, SIFRE);
            System.out.println("Tebrikler! Veritabanına bağlantı başarıyla sağlandı.");
        } catch (SQLException e) {
            System.out.println("Bağlantı hatası: " + e.getMessage());
        }

        return baglanti;
    }
    public static void main(String[] args) {
        baglantiAl();
    }
}

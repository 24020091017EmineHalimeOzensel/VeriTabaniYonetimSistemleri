package org.example.digitallibraryproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnionHavuzPaneli {

    public static void ekranıAc() {
        Stage stage = new Stage();
        stage.setTitle("W3Schools UNION Sorgu Havuzu");

        Label baslik = new Label("SİSTEMDEKİ TÜM İSİMLER (UNION)");
        baslik.setStyle("-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #8e44ad;");

        Label aciklama = new Label("Kitap adları ile Öğrenci adlarını tek bir havuzda birleştirir:");
        aciklama.setStyle("-font-size: 12px; -fx-text-fill: #7f8c8d;");

        ListView<String> listeGörünümü = new ListView<>();

        Button listeleButonu = new Button("Verileri UNION ile Getir");
        listeleButonu.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        listeleButonu.setOnAction(e -> {
            listeGörünümü.getItems().clear();

            String unionSorgusu = "SELECT KitapAdi AS Isim, 'Kitap' AS Tur FROM Kitaplar " +
                    "UNION " +
                    "SELECT Ad + ' ' + Soyad AS Isim, 'Öğrenci' AS Tur FROM Ogrenciler";

            try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
                 PreparedStatement hazirSorgu = baglanti.prepareStatement(unionSorgusu);
                 ResultSet sonucKumesi = hazirSorgu.executeQuery()) {

                while (sonucKumesi.next()) {
                    String isim = sonucKumesi.getString("Isim");
                    String tur = sonucKumesi.getString("Tur");
                    listeGörünümü.getItems().add("[" + tur + "] " + isim);
                }

                if (listeGörünümü.getItems().isEmpty()) {
                    listeGörünümü.getItems().add("Veritabanında henüz gösterilecek veri yok!");
                }

            } catch (SQLException ex) {
                System.out.println("UNION sorgusu çalışırken hata oluştu: " + ex.getMessage());
            }
        });

        VBox duzen = new VBox(15);
        duzen.setPadding(new Insets(20));
        duzen.setAlignment(Pos.CENTER);
        duzen.getChildren().addAll(baslik, aciklama, listeGörünümü, listeleButonu);

        Scene scene = new Scene(duzen, 400, 450);
        stage.setScene(scene);
        stage.show();
    }
}
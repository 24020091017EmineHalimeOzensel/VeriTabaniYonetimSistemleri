package org.example.digitallibraryproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OgrenciPaneli {

    public static class OgrenciModel {
        private final int id;
        private final String numara;
        private final String ad;
        private final String soyad;

        public OgrenciModel(int id, String numara, String ad, String soyad) {
            this.id = id;
            this.numara = numara;
            this.ad = ad;
            this.soyad = soyad;
        }
        public int getId() { return id; }
        public String getNumara() { return numara; }
        public String getAd() { return ad; }
        public String getSoyad() { return soyad; }
    }

    private static TableView<OgrenciModel> tablo = new TableView<>();
    private static ObservableList<OgrenciModel> ogrenciListesi = FXCollections.observableArrayList();

    public static void ekranıAc() {
        Stage stage = new Stage();
        stage.setTitle("Öğrenci Kayıt ve Takip Paneli");

        Label baslik = new Label("ÖĞRENCİ KAYIT PANELİ");
        baslik.setStyle("-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label noEtiket = new Label("Öğrenci Numarası:");
        TextField noGiris = new TextField();

        Label adEtiket = new Label("Öğrenci Adı:");
        TextField adGiris = new TextField();

        Label soyadEtiket = new Label("Öğrenci Soyadı:");
        TextField soyadGiris = new TextField();


        Button ekleButonu = new Button("Öğrenciyi Sisteme Kaydet");
        ekleButonu.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Button silButonu = new Button("Seçili Öğrenciyi Sil");
        silButonu.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");


        TableColumn<OgrenciModel, Integer> idSutun = new TableColumn<>("ID");
        idSutun.setCellValueFactory(new PropertyValueFactory<>("id"));
        idSutun.setPrefWidth(40);

        TableColumn<OgrenciModel, String> noSutun = new TableColumn<>("Öğrenci No");
        noSutun.setCellValueFactory(new PropertyValueFactory<>("numara"));
        noSutun.setPrefWidth(110);

        TableColumn<OgrenciModel, String> adSutun = new TableColumn<>("Adı");
        adSutun.setCellValueFactory(new PropertyValueFactory<>("ad"));
        adSutun.setPrefWidth(110);

        TableColumn<OgrenciModel, String> soyadSutun = new TableColumn<>("Soyadı");
        soyadSutun.setCellValueFactory(new PropertyValueFactory<>("soyad"));
        soyadSutun.setPrefWidth(110);

        tablo.getColumns().clear();
        tablo.getColumns().addAll(idSutun, noSutun, adSutun, soyadSutun);
        tablo.setItems(ogrenciListesi);
        tablo.setPrefHeight(200);

        tabloyuYenile();
        ekleButonu.setOnAction(e -> {
            String ogrenciNo = noGiris.getText();
            String ad = adGiris.getText();
            String soyad = soyadGiris.getText();

            if (ogrenciNo.isEmpty() || ad.isEmpty() || soyad.isEmpty()) {
                System.out.println("Lütfen tüm alanları doldurun!");
                return;
            }


            String sqlSorgusu = "INSERT INTO Ogrenciler (OgrenciNo, Ad, Soyad) VALUES (?, ?, ?)";

            try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
                 PreparedStatement hazirSorgu = baglanti.prepareStatement(sqlSorgusu)) {

                hazirSorgu.setString(1, ogrenciNo);
                hazirSorgu.setString(2, ad);
                hazirSorgu.setString(3, soyad);

                hazirSorgu.executeUpdate();
                System.out.println("Öğrenci başarıyla MS SQL veritabanına kaydedildi!");

                noGiris.clear();
                adGiris.clear();
                soyadGiris.clear();

                tabloyuYenile();

            } catch (SQLException ex) {
                System.out.println("Öğrenci ekleme hatası: " + ex.getMessage());
            }
        });

        silButonu.setOnAction(e -> {
            OgrenciModel seciliOgrenci = tablo.getSelectionModel().getSelectedItem();
            if (seciliOgrenci == null) {
                System.out.println("Lütfen silmek için tablodan bir öğrenci seçin!");
                return;
            }

            String sqlSil = "DELETE FROM Ogrenciler WHERE OgrenciID = ?";

            try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
                 PreparedStatement hazirSorgu = baglanti.prepareStatement(sqlSil)) {

                hazirSorgu.setInt(1, seciliOgrenci.getId());
                hazirSorgu.executeUpdate();
                System.out.println("Öğrenci başarıyla veritabanından silindi!");

                tabloyuYenile();

            } catch (SQLException ex) {
                System.out.println("Öğrenci silme hatası: " + ex.getMessage());
            }
        });

        VBox formDizayn = new VBox(8);
        formDizayn.setPadding(new Insets(15));
        formDizayn.setAlignment(Pos.CENTER_LEFT);
        formDizayn.getChildren().addAll(
                baslik, noEtiket, noGiris, adEtiket, adGiris, soyadEtiket, soyadGiris,
                ekleButonu, tablo, silButonu
        );

        Scene scene = new Scene(formDizayn, 410, 560);
        stage.setScene(scene);
        stage.show();
    }

    private static void tabloyuYenile() {
        ogrenciListesi.clear();
        String sqlSec = "SELECT OgrenciID, OgrenciNo, Ad, Soyad FROM Ogrenciler";

        try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
             PreparedStatement hazirSorgu = baglanti.prepareStatement(sqlSec);
             ResultSet sonucKumesi = hazirSorgu.executeQuery()) {

            while (sonucKumesi.next()) {
                ogrenciListesi.add(new OgrenciModel(
                        sonucKumesi.getInt("OgrenciID"),
                        sonucKumesi.getString("OgrenciNo"),
                        sonucKumesi.getString("Ad"),
                        sonucKumesi.getString("Soyad")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Öğrenci tablosu yenilenirken hata: " + ex.getMessage());
        }
    }
}
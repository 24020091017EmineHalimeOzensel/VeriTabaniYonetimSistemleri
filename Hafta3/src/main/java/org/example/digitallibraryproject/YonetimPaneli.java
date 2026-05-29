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

public class YonetimPaneli {

    public static class KitapModel {
        private final int id;
        private final String ad;
        private final String isbn;
        private final int sayfa;

        public KitapModel(int id, String ad, String isbn, int sayfa) {
            this.id = id;
            this.ad = ad;
            this.isbn = isbn;
            this.sayfa = sayfa;
        }
        public int getId() { return id; }
        public String getAd() { return ad; }
        public String getIsbn() { return isbn; }
        public int getSayfa() { return sayfa; }
    }

    private static TableView<KitapModel> tablo = new TableView<>();
    private static ObservableList<KitapModel> kitapListesi = FXCollections.observableArrayList();

    public static void ekranıAc() {
        Stage stage = new Stage();
        stage.setTitle("Kütüphane Yönetim (Admin) Paneli");

        Label baslik = new Label("KÜTÜPHANE YÖNETİM PANELİ");
        baslik.setStyle("-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label isimEtiket = new Label("Kitap Adı:");
        TextField isimGiris = new TextField();

        Label isbnEtiket = new Label("ISBN No:");
        TextField isbnGiris = new TextField();

        Label sayfaEtiket = new Label("Sayfa Sayısı:");
        TextField sayfaGiris = new TextField();

        Button ekleButonu = new Button("Kitabı Veritabanına Ekle");
        ekleButonu.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Button silButonu = new Button("Seçili Kitabı Sil");
        silButonu.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        TableColumn<KitapModel, Integer> idSutun = new TableColumn<>("ID");
        idSutun.setCellValueFactory(new PropertyValueFactory<>("id"));
        idSutun.setPrefWidth(50);

        TableColumn<KitapModel, String> adSutun = new TableColumn<>("Kitap Adı");
        adSutun.setCellValueFactory(new PropertyValueFactory<>("ad"));
        adSutun.setPrefWidth(150);

        TableColumn<KitapModel, String> isbnSutun = new TableColumn<>("ISBN");
        isbnSutun.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnSutun.setPrefWidth(100);

        TableColumn<KitapModel, Integer> sayfaSutun = new TableColumn<>("Sayfa");
        sayfaSutun.setCellValueFactory(new PropertyValueFactory<>("sayfa"));
        sayfaSutun.setPrefWidth(70);

        tablo.getColumns().clear();
        tablo.getColumns().addAll(idSutun, adSutun, isbnSutun, sayfaSutun);
        tablo.setItems(kitapListesi);
        tablo.setPrefHeight(200);

        tabloyuYenile();

        ekleButonu.setOnAction(e -> {
            String kitapAdi = isimGiris.getText();
            String isbn = isbnGiris.getText();
            String sayfaStr = sayfaGiris.getText();

            if (kitapAdi.isEmpty() || isbn.isEmpty() || sayfaStr.isEmpty()) {
                System.out.println("Lütfen tüm alanları doldurun!");
                return;
            }

            String sqlSorgusu = "INSERT INTO Kitaplar (KitapAdi, ISBN, SayfaSayisi, YazarID) VALUES (?, ?, ?, 1)";

            try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
                 PreparedStatement hazirSorgu = baglanti.prepareStatement(sqlSorgusu)) {

                hazirSorgu.setString(1, kitapAdi);
                hazirSorgu.setString(2, isbn);
                hazirSorgu.setInt(3, Integer.parseInt(sayfaStr));

                hazirSorgu.executeUpdate();
                System.out.println("Kitap başarıyla MS SQL veritabanına kaydedildi!");

                isimGiris.clear();
                isbnGiris.clear();
                sayfaGiris.clear();

                tabloyuYenile();

            } catch (SQLException ex) {
                System.out.println("Ekleme hatası: " + ex.getMessage());
            }
        });

        silButonu.setOnAction(e -> {
            KitapModel seciliKitap = tablo.getSelectionModel().getSelectedItem();
            if (seciliKitap == null) {
                System.out.println("Lütfen silmek için tablodan bir kitap seçin!");
                return;
            }
            // burda kitabı sildiğim zaman ID 'ler otomotik olarak (mesela 3 numaralı ID sildim, yeni eklediğim 4ten) kaldığı yerden devam ediyor.
            // eğer tekrardan sildiğim ID 'nin ID'sini yazsaydı karışırdı.

            String sqlSil = "DELETE FROM Kitaplar WHERE KitapID = ?";

            try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
                 PreparedStatement hazirSorgu = baglanti.prepareStatement(sqlSil)) {

                hazirSorgu.setInt(1, seciliKitap.getId());
                hazirSorgu.executeUpdate();
                System.out.println("Kitap başarıyla veritabanından silindi!");

                tabloyuYenile();

            } catch (SQLException ex) {
                System.out.println("Silme hatası: " + ex.getMessage());
            }
        });

        VBox formDizayn = new VBox(8);
        formDizayn.setPadding(new Insets(15));
        formDizayn.setAlignment(Pos.CENTER_LEFT);
        formDizayn.getChildren().addAll(
                baslik, isimEtiket, isimGiris, isbnEtiket, isbnGiris, sayfaEtiket, sayfaGiris,
                ekleButonu, tablo, silButonu
        );

        Scene scene = new Scene(formDizayn, 420, 580);
        stage.setScene(scene);
        stage.show();
    }

    private static void tabloyuYenile() {
        kitapListesi.clear();
        String sqlSec = "SELECT KitapID, KitapAdi, ISBN, SayfaSayisi FROM Kitaplar";

        try (Connection baglanti = VeriTabaniBaglantisi.baglantiAl();
             PreparedStatement hazirSorgu = baglanti.prepareStatement(sqlSec);
             ResultSet sonucKumesi = hazirSorgu.executeQuery()) {

            while (sonucKumesi.next()) {
                kitapListesi.add(new KitapModel(
                        sonucKumesi.getInt("KitapID"),
                        sonucKumesi.getString("KitapAdi"),
                        sonucKumesi.getString("ISBN"),
                        sonucKumesi.getInt("SayfaSayisi")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Tablo yenilenirken hata oluştu: " + ex.getMessage());
        }
    }
}
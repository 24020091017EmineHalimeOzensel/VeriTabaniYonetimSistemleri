package org.example.digitallibraryproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage anaSahne) {
        anaSahne.setTitle("Dijital Kütüphane Otomasyon Sistemi");

        Label baslikLabel = new Label("KÜTÜPHANE OTOMASYON SİSTEMİ");
        baslikLabel.setStyle("-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label altBaslikLabel = new Label("Lütfen işlem yapmak istediğiniz paneli seçiniz:");
        altBaslikLabel.setStyle("-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Button yonetimButonu = new Button("Yönetim (Admin) Girişi");
        Button ogrenciButonu = new Button("Öğrenci Yönetim Ekranı");
        Button unionButonu = new Button("Genel Sistem Havuzu (UNION Sorgusu)");

        String butonStili = "-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-padding: 12px 30px; -fx-background-radius: 5px; -fx-cursor: hand; -fx-width: 300px;";
        yonetimButonu.setStyle(butonStili);
        ogrenciButonu.setStyle(butonStili);

        unionButonu.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-padding: 12px 30px; -fx-background-radius: 5px; -fx-cursor: hand; -fx-width: 300px;");

        yonetimButonu.setOnAction(e -> YonetimPaneli.ekranıAc());
        ogrenciButonu.setOnAction(e -> OgrenciPaneli.ekranıAc());
        unionButonu.setOnAction(e -> UnionHavuzPaneli.ekranıAc());

        VBox duzenleyici = new VBox(20);
        duzenleyici.setAlignment(Pos.CENTER);
        duzenleyici.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 40px;");
        duzenleyici.getChildren().addAll(baslikLabel, altBaslikLabel, yonetimButonu, ogrenciButonu, unionButonu);

        Scene sahne = new Scene(duzenleyici, 480, 420);
        anaSahne.setScene(sahne);
        anaSahne.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
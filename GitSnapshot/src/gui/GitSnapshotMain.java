/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Apa
 */
public class GitSnapshotMain extends Application {
    
/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(GitSnapshotMain.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(GitSnapshotMain.class.getResource("GitSnapshotMainView.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Git snapshot");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(GitSnapshotMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}

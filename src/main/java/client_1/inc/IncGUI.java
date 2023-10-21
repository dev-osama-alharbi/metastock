package client_1.inc;

import client_1.controller.*;
import client_1.view.IndexView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class IncGUI {
    public Stage stage;
    public Scene scene;

    public AnchorPane gui_Home,gui_Stock,gui_Src,gui_Wallet;
    public BorderPane gui_Root;

    public IncGUI() {
        stage = new Stage();
        scene = new Scene(new AnchorPane());
        stage.setScene(scene);
    }

    public void start() {
        generateGUI();
    }

    public FXMLLoader getLoader(String path) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(IndexView.class.getResource(path + ".fxml"));
        return loader;
    }

    public Pane getPain(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void generateGUI() {
//        FXMLLoader loaderHome = getLoader("Home");
//        gui_Home = (AnchorPane) getPain(loaderHome);
//        Inc.controller.home = loaderHome.<Home>getController();

        FXMLLoader loaderRoot = getLoader("Root");
        gui_Root = (BorderPane) getPain(loaderRoot);
        Inc.controller.root = loaderRoot.<Root>getController();

        FXMLLoader loaderStock = getLoader("Stock");
        gui_Stock = (AnchorPane) getPain(loaderStock);
        Inc.controller.stock = loaderStock.<Stock>getController();

        FXMLLoader loaderSrc = getLoader("Src");
        gui_Src = (AnchorPane) getPain(loaderSrc);
        Inc.controller.src = loaderSrc.<Src>getController();

        FXMLLoader loaderWallet = getLoader("Wallet");
        gui_Wallet = (AnchorPane) getPain(loaderWallet);
        Inc.controller.wallet = loaderWallet.<Wallet>getController();


        scene.getStylesheets().add(IndexView.class.getResource("css.css").toExternalForm());
        stage.getIcons().add(new Image(IndexView.class.getResourceAsStream("icon.png")));

        show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        Platform.setImplicitExit(true);
    }

    private void show(){
        gui_Root.setCenter(gui_Stock);
        scene.setRoot(gui_Root);
        stage.show();
    }
}

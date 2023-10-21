package client_1.main;

import client_1.inc.Inc;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    //Main-Class: client_1.main.Main
    @Override
    public void start(Stage primaryStage) throws Exception {
        Inc.start();
//        new Thread(() -> {
//            try {
//                File file = new File("C:\\Users\\Osama AL-Harbi\\Desktop\\freelancer.sa\\Clients\\1_metastock\\metastock_file\\2\\Interday\\MASTER");
//                int length = (int) file.length();
//                byte[] data = new byte[length];
//                FileInputStream f = new FileInputStream(file);
//                f.read(data);
//                Thread.sleep(90000);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }
}

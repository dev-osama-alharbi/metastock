package client_1.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class xxxxx {
    public void ss(String[] args) {
        try {
            File file = new File(System.currentTimeMillis()+"_errors.txt");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setErr(ps);
        }catch (FileNotFoundException e){
        }

        new Thread(() -> {

            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        }).start();
    }
}

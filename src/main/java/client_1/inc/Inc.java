package client_1.inc;

import client_1.db.DB;
import client_1.model.Model;
import client_1.model.SettingModel;

public class Inc {
    public static GUIController controller;
    public static IncGUI gui;
    public static Model model;
    public static DB db;

    public static void start(){
        MathTools.init();
        model = new Model();
        db = new DB();
        db.start();
        startDb();
        controller = new GUIController();
        gui = new IncGUI();
        gui.start();
    }



    public static void startDb(){
//        db.tables.setting.update.update(new SettingModel("C:\\Users\\Osama AL-Harbi\\Desktop\\x\\testF\\Daily\\","C:\\Users\\Osama AL-Harbi\\Desktop\\x\\testF\\Intraday\\"));
        model.setting.setSettingModel(db.tables.setting.select.select());
        model.walletModels.addAll(db.tables.wallet.select.selectAll());
    }
}

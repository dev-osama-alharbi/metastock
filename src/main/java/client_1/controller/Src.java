package client_1.controller;

import client_1.inc.Inc;
import client_1.model.SettingModel;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Src implements Initializable {
    private DirectoryChooser directoryChooser = null;

    @FXML public TextField txtDirDayley,txtDirInst;
    @FXML public JFXCheckBox chkStartShowEx;

    private PrintStream console;
    private File file = null;
    private FileOutputStream fos = null;
    private PrintStream ps = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        console = System.err;
        directoryChooser = new DirectoryChooser();


        chkStartShowEx.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                if(file == null || fos == null || ps == null){
                    try {
                        file = new File(System.currentTimeMillis()+"_errors.txt");
                        fos = new FileOutputStream(file);
                        ps = new PrintStream(fos);
                    }catch (FileNotFoundException e){
                    }
                }
                System.setErr(ps);
            }else {
                System.setErr(console);
            }
        });
    }

    @FXML public void onClickDirDayley(){
        if(directoryChooser.getInitialDirectory() == null){
            if(!txtDirDayley.getText().isEmpty()){
                directoryChooser.setInitialDirectory(new File(txtDirDayley.getText()));
            }
        }else{
            directoryChooser.setInitialDirectory(directoryChooser.getInitialDirectory());
        }
        File f = directoryChooser.showDialog(Inc.controller.stock.tempStageSrc);
        if(f != null){
            txtDirDayley.setText(f.getAbsolutePath());
        }
    }

    @FXML public void onClickDirInst(){
        if(directoryChooser.getInitialDirectory() == null){
            if(!txtDirInst.getText().isEmpty()){
                directoryChooser.setInitialDirectory(new File(txtDirInst.getText()));
            }
        }else{
            directoryChooser.setInitialDirectory(directoryChooser.getInitialDirectory());
        }
        File f = directoryChooser.showDialog(Inc.controller.stock.tempStageSrc);
        if(f != null){
            txtDirInst.setText(f.getAbsolutePath());
        }
    }

    @FXML public void onClickSave(){
        if(!txtDirDayley.getText().isEmpty() && !txtDirDayley.getText().endsWith("\\")){
            txtDirDayley.setText(txtDirDayley.getText()+"\\");
        }
        if(!txtDirInst.getText().isEmpty() && !txtDirInst.getText().endsWith("\\")){
            txtDirInst.setText(txtDirInst.getText()+"\\");
        }
        SettingModel settingModel = new SettingModel(txtDirDayley.getText(),txtDirInst.getText(),Inc.model.settingModel.getDate(),Inc.model.settingModel.getNumOfLastDays());
        if(Inc.db.tables.setting.update.update(settingModel)){
            Inc.model.setting.setSettingModel(settingModel);
        }

        Inc.controller.stock.tempStageSrc.hide();
    }

    @FXML public void onClickReset(){
        SettingModel settingModel = new SettingModel("","",Inc.model.settingModel.getDate(), Inc.model.settingModel.getNumOfLastDays());
        if(Inc.db.tables.setting.update.update(settingModel)){
            Inc.model.setting.setSettingModel(settingModel);
        }
        if(Inc.db.tables.wallet.delete.deleteAll()){
            Inc.model.walletModels.clear();
        }
        Inc.controller.stock.obsTbl.clear();
        Inc.controller.stock.tbl.refresh();

        Inc.controller.stock.dypDate.setValue(LocalDate.now());
        Inc.controller.stock.spnLastDays.getValueFactory().setValue(1);
        Inc.controller.stock.updateLastPrice.set(false);
        Inc.controller.stock.lblDateFromTo.setText("");
        Inc.controller.stock.lblLoad.setText("");

        Inc.controller.wallet.tbl.refresh();

        Inc.controller.stock.tempStageSrc.hide();

        // OR init();
    }

    public void init() {
        txtDirDayley.setText(Inc.model.settingModel.getDaily());
        txtDirInst.setText(Inc.model.settingModel.getIntraday());
    }
}

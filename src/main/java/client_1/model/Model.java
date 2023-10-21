package client_1.model;

import client_1.inc.Inc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    public SettingModel settingModel = null;
    public ObservableList<WalletModel> walletModels = null;


    public Setting setting = null;

    public Model(){
        this.setting = new Setting();
        this.walletModels = FXCollections.observableArrayList();
    }

    public class Setting{
        public void setSettingModel(SettingModel settingModel){
            if(Inc.model.settingModel != null){
                Inc.model.settingModel.setDaily(settingModel.getDaily());
                Inc.model.settingModel.setIntraday(settingModel.getIntraday());
            }else{
                Inc.model.settingModel = settingModel;
            }
        }
    }


}

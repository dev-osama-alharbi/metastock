package client_1.model;

import client_1.inc.MathTools;
import javafx.beans.property.*;

import java.time.LocalDate;

public class WalletModel {
    private IntegerProperty wallet_id;
    private StringProperty wallet_symple;
    private ObjectProperty<LocalDate> wallet_date;
    private FloatProperty wallet_pay;
    private FloatProperty wallet_lastPrice;
    private BooleanProperty isSelectedToDelete;

    public WalletModel(int wallet_id, String wallet_symple, LocalDate wallet_date, float wallet_pay) {
        this.wallet_id = new SimpleIntegerProperty(wallet_id);
        this.wallet_symple = new SimpleStringProperty(wallet_symple);
        this.wallet_date = new SimpleObjectProperty<>(wallet_date);
        this.wallet_pay = new SimpleFloatProperty(MathTools.toPrice(wallet_pay));
        this.wallet_lastPrice = new SimpleFloatProperty(MathTools.toPrice(0));
        this.isSelectedToDelete = new SimpleBooleanProperty(false);
    }

    public int getWallet_id() {
        return wallet_id.get();
    }

    public IntegerProperty wallet_idProperty() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id.set(wallet_id);
    }

    public String getWallet_symple() {
        return wallet_symple.get();
    }

    public StringProperty wallet_sympleProperty() {
        return wallet_symple;
    }

    public void setWallet_symple(String wallet_symple) {
        this.wallet_symple.set(wallet_symple);
    }

    public LocalDate getWallet_date() {
        return wallet_date.get();
    }

    public ObjectProperty<LocalDate> wallet_dateProperty() {
        return wallet_date;
    }

    public void setWallet_date(LocalDate wallet_date) {
        this.wallet_date.set(wallet_date);
    }

    public float getWallet_pay() {
        return wallet_pay.get();
    }

    public FloatProperty wallet_payProperty() {
        return wallet_pay;
    }

    public void setWallet_pay(float wallet_pay) {
        this.wallet_pay.set(MathTools.toPrice(wallet_pay));
    }

    public float getWallet_lastPrice() {
        return wallet_lastPrice.get();
    }

    public FloatProperty wallet_lastPriceProperty() {
        return wallet_lastPrice;
    }

    public void setWallet_lastPrice(float wallet_lastPrice) {
        this.wallet_lastPrice.set(MathTools.toPrice(wallet_lastPrice));
    }

    public boolean isIsSelectedToDelete() {
        return isSelectedToDelete.get();
    }

    public BooleanProperty isSelectedToDeleteProperty() {
        return isSelectedToDelete;
    }

    public void setIsSelectedToDelete(boolean isSelectedToDelete) {
        this.isSelectedToDelete.set(isSelectedToDelete);
    }
}

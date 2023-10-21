package client_1.controller;

import client_1.inc.Inc;
import client_1.inc.MathTools;
import client_1.model.RowModel;
import client_1.model.WalletModel;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Wallet implements Initializable {

    @FXML public TableView<WalletModel> tbl;

    @FXML public TableColumn<WalletModel, Number> tbcSelect;
    @FXML public TableColumn<WalletModel, String> tbcSymple,tbcDate,tbcPay,tbcLastPrice,tbcFinalResult;

    @FXML public TextField txtSymple,txtPay;

    @FXML public JFXDatePicker dpDate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tbl.setItems(Inc.model.walletModels);

        tbcSelect.setCellValueFactory(e -> e.getValue().wallet_idProperty());
        tbcSymple.setCellValueFactory(e -> e.getValue().wallet_sympleProperty());
        tbcDate.setCellValueFactory(e -> e.getValue().wallet_dateProperty().asString());
        tbcPay.setCellValueFactory(e -> MathTools.toStringExpression(e.getValue().wallet_payProperty()));
        tbcLastPrice.setCellValueFactory(e -> MathTools.toStringExpression(e.getValue().wallet_lastPriceProperty()));
        tbcFinalResult.setCellValueFactory(e -> MathTools.toStringExpression(e.getValue().wallet_lastPriceProperty().subtract(e.getValue().wallet_payProperty())));

        tbcSelect.setCellFactory(new Callback<TableColumn<WalletModel, Number>, TableCell<WalletModel, Number>>() {
            @Override
            public TableCell<WalletModel, Number> call(TableColumn<WalletModel, Number> param) {
                return new TableCell<WalletModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null && !empty){
                            WalletModel walletModel = getTableView().getItems().get(getIndex());
                            JFXCheckBox checkBox = new JFXCheckBox();
                            checkBox.setSelected(walletModel.isIsSelectedToDelete());
                            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                walletModel.setIsSelectedToDelete(newValue);
                            });
                            checkBox.setText("");
                            checkBox.setAlignment(Pos.CENTER);
                            setGraphic(checkBox);
                            this.setStyle("-fx-background-color:#FFFFFF ;");
                        }else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        tbcSymple.setCellFactory(param -> {
            TableCell cell = new TableCell<WalletModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty){
                        WalletModel walletModel = getTableView().getItems().get(getIndex());
                        setText(walletModel.getWallet_symple());
                        setTextFill(Paint.valueOf("#000000"));
                        this.setStyle("-fx-background-color:#FFFFFF ;");
                    }else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        TableCell c = (TableCell) event.getSource();
                        WalletModel w = (WalletModel)c.getTableRow().getItem();

                        TextInputDialog dialog = new TextInputDialog(w.getWallet_symple());
                        dialog.setTitle("تعديل خانة");
                        dialog.setHeaderText("تعديل خانة الرمز");
                        dialog.setContentText("الرمز");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()){
                            if(!result.get().isEmpty()){
                                WalletModel wupdate = new WalletModel(w.getWallet_id(), result.get(), w.getWallet_date(), w.getWallet_pay());
                                if(Inc.db.tables.wallet.update.updateVat(wupdate)){
                                    w.setWallet_symple(result.get());
                                }
                            }
                        }

                    }
                }
            });
            return cell;
        });

        tbcDate.setCellFactory(param -> {
            TableCell<WalletModel, String> cell = new TableCell<WalletModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty){
                        WalletModel walletModel = getTableView().getItems().get(getIndex());
                        setText(walletModel.getWallet_date().toString());
                        setTextFill(Paint.valueOf("#000000"));
                        this.setStyle("-fx-background-color:#FFFFFF ;");
                    }else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        TableCell c = (TableCell) event.getSource();
                        WalletModel w = (WalletModel)c.getTableRow().getItem();

                        Dialog<Pair<LocalDate, String>> dialog = new Dialog<>();
                        dialog.setTitle("Login Dialog");
                        dialog.setHeaderText("Look, a Custom Login Dialog");

                        ButtonType save = new ButtonType("حفظ", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancel = new ButtonType("إلغاء", ButtonBar.ButtonData.CANCEL_CLOSE);
                        dialog.getDialogPane().getButtonTypes().addAll(save, cancel);

                        GridPane grid = new GridPane();
                        grid.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        JFXDatePicker datePicker = new JFXDatePicker();
                        datePicker.setEditable(false);
                        datePicker.setValue(w.getWallet_date());
                        datePicker.getEditor().setFont(Font.font(18));
                        datePicker.setConverter(new StringConverter<LocalDate>() {
                            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            @Override
                            public String toString(LocalDate localDate) {
                                if(localDate==null)
                                    return "";
                                return dateTimeFormatter.format(localDate);
                            }
                            @Override
                            public LocalDate fromString(String dateString) {
                                if(dateString==null || dateString.trim().isEmpty()) {
                                    return null;
                                }
                                return LocalDate.parse(dateString,dateTimeFormatter);
                            }
                        });


                        Label lbl = new Label("تاريخ الشراء:");
                        lbl.setFont(Font.font(18));

                        grid.add(lbl, 0, 0);
                        grid.add(datePicker, 1, 0);

                        dialog.getDialogPane().setContent(grid);

                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == save) {
                                return new Pair<>(datePicker.getValue(), "");
                            }
                            return null;
                        });

                        Optional<Pair<LocalDate, String>> result = dialog.showAndWait();

                        result.ifPresent(x ->{
                            if(x.getKey() != null){
                                WalletModel wupdate = new WalletModel(w.getWallet_id(), w.getWallet_symple(), x.getKey(), w.getWallet_pay());
                                if(Inc.db.tables.wallet.update.updateVat(wupdate)){
                                    w.setWallet_date(x.getKey());
                                }
                            }
                        });
                    }
                }
            });
            return cell;
        });

        tbcPay.setCellFactory(param -> {
            TableCell<WalletModel, String> cell = new TableCell<WalletModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty){
                        WalletModel walletModel = getTableView().getItems().get(getIndex());
                        setText(MathTools.toPriceAsString(walletModel.getWallet_pay()));

                        setTextFill(Paint.valueOf("#000000"));
                        this.setStyle("-fx-background-color:#FFFFFF ;");
                    }else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() > 1) {
                        TableCell c = (TableCell) event.getSource();
                        WalletModel w = (WalletModel)c.getTableRow().getItem();

                        TextInputDialog dialog = new TextInputDialog(MathTools.toPriceAsString(w.getWallet_pay()));
                        dialog.setTitle("تعديل خانة");
                        dialog.setHeaderText("تعديل خانة سعر الشراء");
                        dialog.setContentText("سعر الشراء");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()){
                            if(!result.get().isEmpty()){
                                try {
                                    float pay = Float.parseFloat(result.get());
                                    WalletModel wupdate = new WalletModel(w.getWallet_id(), w.getWallet_symple(), w.getWallet_date(), pay);
                                    if(Inc.db.tables.wallet.update.updateVat(wupdate)){
                                        w.setWallet_pay(pay);
                                    }
                                }catch (Exception e){
                                    ButtonType yes = new ButtonType("حسنا", ButtonBar.ButtonData.OK_DONE);
                                    Alert alert = new Alert(Alert.AlertType.WARNING,
                                            "لم تدخل السعر بشكل صحيح",
                                            yes);
                                    alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

                                    alert.setTitle("لم تدخل السعر بشكل صحيح");
                                    alert.showAndWait();
                                }
                            }
                        }
                    }
                }
            });
            return cell;
        });

        tbcLastPrice.setCellFactory(param -> {
            return new TableCell<WalletModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty){
                        WalletModel walletModel = getTableView().getItems().get(getIndex());
                        setText(MathTools.toPriceAsString(walletModel.getWallet_lastPrice()));


                        this.setStyle("-fx-background-color:#323232 ;");
                    }else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
        });

        tbcFinalResult.setCellFactory(param -> {
            return new TableCell<WalletModel, String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(item != null && !empty){
                        WalletModel walletModel = getTableView().getItems().get(getIndex());
                        setText(MathTools.toPriceAsString(walletModel.wallet_lastPriceProperty().subtract(walletModel.wallet_payProperty()).floatValue()));


                        this.setStyle("-fx-background-color:#323232 ;");
                    }else {
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
        });



        dpDate.setValue(LocalDate.now());
    }

    @FXML public void onClickDeleteAll() {
        ButtonType yes = new ButtonType("نعم", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("لا", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "هل ترغب بحذف جميع الأسعر",
                yes,
                no);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        alert.setTitle("حذف جميع الأسعر");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            if(Inc.db.tables.wallet.delete.deleteAll()){
                Inc.model.walletModels.clear();
            }
            tbl.refresh();
        }

    }

    @FXML public void onClickDeleteSelected() {
        ButtonType yes = new ButtonType("نعم", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("لا", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "هل ترغب بحذف الأسعر المحددة",
                yes,
                no);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        alert.setTitle("حذف الأسعر المحددة");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            ArrayList<Integer> arrDelete = new ArrayList<Integer>();
            for (WalletModel w:Inc.model.walletModels) {
                if(w.isIsSelectedToDelete()){
                    arrDelete.add(w.getWallet_id());
                }
            }
            if(Inc.db.tables.wallet.delete.deleteAll(arrDelete)){
                ArrayList<WalletModel> arr = new ArrayList<WalletModel>();
                for (WalletModel w:Inc.model.walletModels) {
                    if(arrDelete.contains(w.getWallet_id())){
                        arr.add(w);
                    }
                }
                Inc.model.walletModels.removeAll(arr);
            }
            tbl.refresh();
        }
    }

    @FXML public void onClickRefreshLastPrice() {
        ButtonType yes = new ButtonType("نعم", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("لا", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "هل ترغب بتحديث آخر سعر",
                yes,
                no);
        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        alert.setTitle("تحديث آخر سعر");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            Inc.model.walletModels.forEach(walletModel -> {
                FloatProperty f = Inc.controller.stock.obsTblRefrishClose.get(walletModel.getWallet_symple());
                if(f != null){
                    walletModel.setWallet_lastPrice(f.get());
                }
            });
            tbl.refresh();
        }
    }

    @FXML public void onClickSave() {
        if(txtSymple.getText().isEmpty() || txtPay.getText().isEmpty() || dpDate.getValue() == null){
            ButtonType yes = new ButtonType("حسنا", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "أحد الحقول فارغة",
                    yes);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            alert.setTitle("أحد الحقول فارغة");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        try{
            float pay = Float.parseFloat(txtPay.getText());
            WalletModel w = new WalletModel(0,txtSymple.getText(),dpDate.getValue(),pay);
            WalletModel newW = Inc.db.tables.wallet.insert.insert(w);
            if(newW != null){
                Inc.model.walletModels.add(newW);
                txtSymple.setText("");
                txtPay.setText("");
            }
        }catch (Exception e){
            ButtonType yes = new ButtonType("حسنا", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "لم تدخل السعر بشكل صحيح",
                    yes);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            alert.setTitle("لم تدخل السعر بشكل صحيح");
            Optional<ButtonType> result = alert.showAndWait();
        }
        tbl.refresh();
    }


    public void init() {
    }
}

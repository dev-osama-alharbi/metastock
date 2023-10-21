package client_1.controller;

import client_1.inc.Inc;
import client_1.inc.MathTools;
import client_1.metastock.model.FileNotFoundException_stok;
import client_1.model.QuoteModel;
import client_1.model.RowModel;
import client_1.model.SettingModel;
import client_1.model.WorkGroupModel;
import client_1.view.IndexView;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXProgressBar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Stock implements Initializable {

    @FXML public TableView<RowModel> tbl;
    public ObservableList<RowModel> obsTbl;
    public ObservableMap<String, FloatProperty> obsTblRefrishClose = null;
    @FXML public TableColumn<RowModel, String> tcTicker;
    @FXML public TableColumn<RowModel, Number> tcN;
    @FXML public TableColumn<RowModel, Number> tcRefrishClose,tcDataClose,tcU2,tcU1,tcAU,tcDataH,tcO,tcM,tcE,tcDataL,tcAD,tcD1,tcD2;
    @FXML public JFXDatePicker dypDate;
    @FXML public Spinner<Integer> spnLastDays;
    @FXML public Label lblDateFromTo;
    public BooleanProperty updateLastPrice = null;
    private Timeline timelineUpdateLastPrice = null;
//    private Timeline testApp = null;
    @FXML public FontAwesomeIconView fontStartAll,fontLastPrice,fontLastPriceStop;
//    public void updateSpinnerValue(Integer newValue) {
//        spnLastDays.getValueFactory().setValue(newValue);
//    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcDataClose.setSortable(true);
        tcRefrishClose.setSortable(true);
//        testApp = new Timeline(
//                new KeyFrame(Duration.seconds(5),
//                        new EventHandler<ActionEvent>() {
//                            @Override
//                            public void handle(ActionEvent event) {
//                                boolean isClose = false;
//                                LocalDate d = LocalDate.of(2021,2,11);
//                                if(LocalDate.now().isAfter(d)){
//                                    isClose = true;
//                                }else{
//                                    isClose = false;
//                                }
//                                if(isClose){
//                                    Platform.runLater(() -> {
//                                        testApp.stop();
//                                        ButtonType yes = new ButtonType("حسنا", ButtonBar.ButtonData.OK_DONE);
//                                        Alert alert = new Alert(Alert.AlertType.WARNING,
//                                                "تم تجاوز المدة المحددة للبرنامج سيتم اغلاق البرنامج",
//                                                yes);
//                                        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
//
//                                        alert.setTitle("");
//                                        alert.showAndWait();
//                                        System.exit(0);
//                                    });
//                                }
//                            }
//                        }));
//        testApp.setCycleCount(Timeline.INDEFINITE);
//        testApp.play();


        timelineUpdateLastPrice = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                loadIntraday();
                                System.gc();
                            }
                        }));
        timelineUpdateLastPrice.setCycleCount(Timeline.INDEFINITE);

        updateLastPrice = new SimpleBooleanProperty(false);
        updateLastPrice.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                fontLastPrice.setFill(Paint.valueOf("#1e7f0d"));
                fontLastPriceStop.setFill(Paint.valueOf("#000000"));
                timelineUpdateLastPrice.play();
            }else {
                fontLastPriceStop.setFill(Paint.valueOf("#1e7f0d"));
                fontLastPrice.setFill(Paint.valueOf("#000000"));
                timelineUpdateLastPrice.stop();
            }
        });
        fontLastPriceStop.setFill(Paint.valueOf("#1e7f0d"));
        fontLastPrice.setFill(Paint.valueOf("#000000"));
        fontStartAll.setFill(Paint.valueOf("#000000"));
//        LocalDate to = LocalDate.of(2021,3,7);
////        System.out.println(getBusinessDays(to,from));
//        int businessDays = 3*(-1);
//        LocalDate from = to.minusDays(getAllDays(to.getDayOfWeek().getValue(), businessDays));
//
//        System.out.println("from = "+from);
//        System.out.println("to = "+to);

        obsTbl = FXCollections.observableArrayList();
        obsTblRefrishClose = FXCollections.observableHashMap();
        obsTbl.addListener(new ListChangeListener<RowModel>() {
            @Override
            public void onChanged(Change<? extends RowModel> c) {
                while (c.next()){
                    if (c.wasAdded()){
                        c.getAddedSubList().forEach(rowModel -> {
                            obsTblRefrishClose.put(rowModel.getTicker(),rowModel.refrishCloseProperty());
                        });
                    }
                    if(c.wasRemoved()){
                        c.getRemoved().forEach(rowModel -> {
                            obsTblRefrishClose.put(rowModel.getTicker(),rowModel.refrishCloseProperty());
                        });
                    }
                }
            }
        });

        tbl.setItems(obsTbl);
        AnchorPane anchorPane = new AnchorPane();
        Label lbl = new Label("لا توجد حقول");
        lbl.setTextFill(Paint.valueOf("#FFFFFF"));
        lbl.setTextAlignment(TextAlignment.CENTER);
        lbl.setFont(new Font(26.0));
        VBox box = new VBox(lbl);
        box.setAlignment(Pos.CENTER);
        anchorPane.getChildren().add(box);
        AnchorPane.setLeftAnchor(box, 0.0);
        AnchorPane.setRightAnchor(box, 0.0);
        AnchorPane.setBottomAnchor(box,0.0);
        AnchorPane.setTopAnchor(box,0.0);
        anchorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#323232"),null,null)));

        lblDateFromTo.setText("لا توجد حقول");

        tbl.setPlaceholder(anchorPane);
//        tbl.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
//        float ff = 0.09999981f;
//        System.out.format("%.2f \n",ff);
//        ff = 0.9999981f;
//        System.out.format("%.2f \n",ff);
//        ff = 9.999981f;
//        System.out.format("%.2f \n",ff);
//        ff = 99.99981f;
//        System.out.format("%.2f \n",ff);
//        ff = 999.9981f;
//        System.out.format("%.2f \n",ff);
//        ff = 9999.981f;
//        System.out.format("%.2f \n",ff);
//        ff = 99999.81f;
//        System.out.format("%.2f \n",ff);
//        ff = 999998.1f;
//        System.out.format("%.2f \n",ff);
//        ff = 9999981f;
//        System.out.format("%.2f \n",ff);
//        ff = 99999810f;
//        System.out.format("%.2f \n",ff);
//        System.exit(0);


        lblLoad.visibleProperty().bind(progLoad.visibleProperty());
        progLoad.setSecondaryProgress(0);
        progLoad.setProgress(0);
        progLoad.setVisible(false);

        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };
        String INITAL_VALUE = "1";
        TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, filter);
        SpinnerValueFactory s = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 30000, Integer.parseInt(INITAL_VALUE));

        spnLastDays.setValueFactory(s);
        spnLastDays.setEditable(true);
        spnLastDays.getEditor().setTextFormatter(priceFormatter);
        spnLastDays.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                int x = Integer.parseInt(newValue);
                if(x <= 0){
                    spnLastDays.getValueFactory().setValue(Integer.parseInt(oldValue));
//                    spnLastDays.getEditor().setText(oldValue);
                }else{
                    spnLastDays.getValueFactory().setValue(Integer.parseInt(newValue));
                }
            }catch (Exception e){
                spnLastDays.getValueFactory().setValue(Integer.parseInt(oldValue));
                spnLastDays.getEditor().setText(oldValue);
            }
        });
        spnLastDays.getEditor().setText(Inc.model.settingModel.getNumOfLastDays()+"");
        spnLastDays.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                SettingModel oldModel = Inc.model.settingModel;
                SettingModel m = new SettingModel(oldModel.getDaily(),oldModel.getIntraday(),oldModel.getDate(),newValue);
                if(Inc.db.tables.setting.update.update(m)){
                    oldModel.setNumOfLastDays(newValue);
                }
            }
        });



        dypDate.setValue(Inc.model.settingModel.getDate());
        dypDate.setConverter(new StringConverter<LocalDate>() {
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
        dypDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                SettingModel oldModel = Inc.model.settingModel;
                SettingModel m = new SettingModel(oldModel.getDaily(),oldModel.getIntraday(),newValue,oldModel.getNumOfLastDays());
                if(Inc.db.tables.setting.update.update(m)){
                    oldModel.setDate(newValue);
                }
            }
        });
        if(dypDate.getValue() == null){
            dypDate.setValue(LocalDate.now());
        }

//        tcTicker.setCellValueFactory(cell -> cell.getValue().tickerProperty());
//        tcRefrishClose.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().refrishCloseProperty()));
//        tcDataClose.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().dataCloseProperty()));
//        tcN.setCellValueFactory(cell -> cell.getValue().nProperty());
//        tcU2.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().u2Property()));
//        tcU1.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().u1Property()));
//        tcAU.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().aUProperty()));
//        tcDataH.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().dataHProperty()));
//        tcO.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().oProperty()));
//        tcM.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().mProperty()));
//        tcE.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().eProperty()));
//        tcDataL.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().dataLProperty()));
//        tcAD.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().aDProperty()));
//        tcD1.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().d1Property()));
//        tcD2.setCellValueFactory(cell -> MathTools.toStringExpression(cell.getValue().d2Property()));

        tcTicker.setCellValueFactory(cell -> cell.getValue().tickerProperty());
        tcRefrishClose.setCellValueFactory(cell -> cell.getValue().refrishCloseProperty());
        tcDataClose.setCellValueFactory(cell -> cell.getValue().dataCloseProperty());
        tcN.setCellValueFactory(cell -> cell.getValue().nProperty());
        tcU2.setCellValueFactory(cell -> cell.getValue().u2Property());
        tcU1.setCellValueFactory(cell -> cell.getValue().u1Property());
        tcAU.setCellValueFactory(cell -> cell.getValue().aUProperty());
        tcDataH.setCellValueFactory(cell -> cell.getValue().dataHProperty());
        tcO.setCellValueFactory(cell -> cell.getValue().oProperty());
        tcM.setCellValueFactory(cell -> cell.getValue().mProperty());
        tcE.setCellValueFactory(cell -> cell.getValue().eProperty());
        tcDataL.setCellValueFactory(cell -> cell.getValue().dataLProperty());
        tcAD.setCellValueFactory(cell -> cell.getValue().aDProperty());
        tcD1.setCellValueFactory(cell -> cell.getValue().d1Property());
        tcD2.setCellValueFactory(cell -> cell.getValue().d2Property());


        tbl.getSelectionModel().setCellSelectionEnabled(true);

        tcDataClose.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){

                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getDataClose()));

                            if(rowModel.getDataClose() > rowModel.getM()){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else if(rowModel.getDataClose() < rowModel.getM()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });
        tcRefrishClose.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getRefrishClose()));

                            if(rowModel.getRefrishClose() > rowModel.getU2()){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else if(rowModel.getRefrishClose() < rowModel.getD2()){
//                            }else if(rowModel.getRefrishClose() > rowModel.getD2()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });
        tcM.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getM()));

                            this.setStyle("-fx-background-color:#fe9800 ;");
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });
        tcN.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getN()));

                            this.setStyle("-fx-background-color:#323232 ;");
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcTicker.setCellFactory(new Callback<TableColumn<RowModel, String>, TableCell<RowModel, String>>() {
            @Override
            public TableCell<RowModel, String> call(TableColumn<RowModel, String> param) {
                return new TableCell<RowModel, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(rowModel.getTicker());

                            if(rowModel.getDataClose() > ((rowModel.getM()+rowModel.getE())/2) && rowModel.getDataClose() < ((rowModel.getM()+rowModel.getO())/2)){
                                this.setStyle("-fx-background-color:#808000 ;");
                            }else if(rowModel.getDataClose() > ((rowModel.getO()+rowModel.getDataH())/2) || rowModel.getDataClose() < ((rowModel.getE()+rowModel.getDataL())/2)){
                                this.setStyle("-fx-background-color:#3982c0 ;");
                            }else if(rowModel.getDataClose() > rowModel.getO() && rowModel.getDataClose() < (rowModel.getO()+rowModel.getDataH())){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else if(rowModel.getDataClose() > (rowModel.getE()+rowModel.getDataL()) && rowModel.getDataClose() < rowModel.getE()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcU1.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getU1()));

                            if(rowModel.getRefrishClose() > rowModel.getaU()){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcDataH.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getDataH()));

                            if(rowModel.getRefrishClose() > rowModel.getaU()){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcO.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getO()));

                            if(rowModel.getRefrishClose() > rowModel.getaU()){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcAU.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getaU()));

                            if(rowModel.getRefrishClose() > rowModel.getaU()){
                                this.setStyle("-fx-background-color:#bdbdbd ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcDataL.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getDataL()));

                            if(rowModel.getRefrishClose() < rowModel.getaD()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcE.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getE()));

                            if(rowModel.getRefrishClose() < rowModel.getaD()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcD1.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getD1()));

                            if(rowModel.getRefrishClose() < rowModel.getaD()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcAD.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getaD()));

                            if(rowModel.getRefrishClose() < rowModel.getaD()){
                                this.setStyle("-fx-background-color:#bdbdbd ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcD2.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getD2()));

                            if(rowModel.getDataClose() < rowModel.getE() && rowModel.getRefrishClose() < rowModel.getaD()){
                                this.setStyle("-fx-background-color:#b80000 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

        tcU2.setCellFactory(new Callback<TableColumn<RowModel, Number>, TableCell<RowModel, Number>>() {
            @Override
            public TableCell<RowModel, Number> call(TableColumn<RowModel, Number> param) {
                return new TableCell<RowModel, Number>(){
                    @Override
                    protected void updateItem(Number item, boolean empty) {
                        super.updateItem(item, empty);
                        if(!empty && item != null){
                            RowModel rowModel = getTableView().getItems().get(getIndex());
                            setText(MathTools.toPriceAsString(rowModel.getU2()));

                            if(rowModel.getDataClose() > rowModel.getO() && rowModel.getRefrishClose() > rowModel.getaU()){
                                this.setStyle("-fx-background-color:#007d00 ;");
                            }else{
                                this.setStyle("-fx-background-color:#323232 ;");
                            }
                        }else{
                            setGraphic(null);
                            setText(null);
                        }
                    }
                };
            }
        });

//        WorkGroupModel workGroupModel = new WorkGroupModel("C:\\Users\\Osama AL-Harbi\\Desktop\\x\\f\\Daily\\");
//
//        workGroupModel.getInstrumentModel().forEach(i->{
//            obsTbl.add(new RowModel(i).initDaily(LocalDate.now(), 10));
//        });

    }

    private WorkGroupModel workGroupDailyModel = null;
    private WorkGroupModel workGroupIntradayModel = null;


    @FXML public void onClickSrc(){
        popup_Src();
    }

    private BooleanProperty isLoading = new SimpleBooleanProperty(false);
    @FXML public JFXProgressBar progLoad;
    @FXML public Label lblLoad;
    @FXML public void onClickViewDaily(){
        File f = new File(Inc.model.settingModel.getDaily()+"MASTER");
        if(!f.exists() || !f.isFile() || Inc.model.settingModel.getDaily().equals("\\")){
            ButtonType yes = new ButtonType("حسنا", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "لم يتم تحديد الملف اليومي",
                    yes);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            alert.setTitle("تحديد الملف اليومي");
            Optional<ButtonType> result = alert.showAndWait();

            return;
        }
        if(!isLoading.get()){
            fontStartAll.setFill(Paint.valueOf("#1e7f0d"));
            isLoading.set(true);
            progLoad.setSecondaryProgress(1);
            progLoad.setProgress(0.01);
            progLoad.setVisible(true);
            lblLoad.setText("جاري تحميل عرض الاهداف ,,,,,,,,,");

                Platform.runLater(() -> {
                    progLoad.setProgress(0.05);
                    new Thread(() -> {

                        progLoad.setProgress(0.10);
                        workGroupDailyModel = null;
                        workGroupDailyModel = WorkGroupModel.genereate(Inc.model.settingModel.getDaily(),progLoad,true);
                        if(workGroupDailyModel == null){
                            Platform.runLater(() -> {
                                lblLoad.setText("حدث خطأ اثناء قرائة الملف لتعارضه مع برنامج آخر");
                                progLoad.setSecondaryProgress(0);
                                progLoad.setProgress(0);
                                isLoading.set(false);
//                                progLoad.setVisible(false);
                            });
                            return;
                        }

                        progLoad.setProgress(0.80);
                        Platform.runLater(() -> {
                            progLoad.setProgress(0.85);
                            obsTbl.clear();
                            System.gc();
                        });
                        progLoad.setProgress(0.86);
                        progLoad.setProgress(0.87);
                        Platform.runLater(() -> {

                            LocalDate to = dypDate.getValue();
                            int businessDays = (Integer.parseInt(spnLastDays.getEditor().getText())*(-1))+1;
                            LocalDate from = to.minusDays(getAllDays(to.getDayOfWeek().getValue(), businessDays));


                            if(from.getDayOfWeek() == DayOfWeek.FRIDAY){
                                from = from.plusDays(2);
                            }else if(from.getDayOfWeek() == DayOfWeek.SATURDAY){
                                from = from.plusDays(1);
                            }
                            LocalDate finalFrom = from;

                            if(to.getDayOfWeek() == DayOfWeek.SATURDAY){
                                to = to.minusDays(2);
                            }else if(to.getDayOfWeek() == DayOfWeek.FRIDAY){
                                to = to.minusDays(1);
                            }
                            LocalDate finalTo = to;


                            lblDateFromTo.setText("عرض من "+finalFrom+" إلى "+finalTo);

//                            System.out.println("sleep 20000");
//                            try {
//                                Thread.sleep(20000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("End sleep 20000");
                            progLoad.setProgress(0.90);
                            workGroupDailyModel.getInstrumentModel().forEach(i->{
                                Platform.runLater(() -> {
                                    obsTbl.add(new RowModel(i).initDaily_v2(finalFrom, finalTo));
                                });
                            });
                            progLoad.setProgress(0.95);

                            progLoad.setSecondaryProgress(1);
                            progLoad.setProgress(1);


                            Platform.runLater(() -> {
                                tbl.refresh();
                                System.gc();
                            });

                            Platform.runLater(() -> {
                                progLoad.setSecondaryProgress(0);
                                progLoad.setProgress(0);
                                isLoading.set(false);
                                progLoad.setVisible(false);
                            });
                        });
                    }).start();
                });




        }else{
//            System.out.println("جاري التحميل");
        }


    }

    @FXML public void onClickViewIntraday(){
        File f = new File(Inc.model.settingModel.getIntraday()+"MASTER");
        if(!f.exists() || !f.isFile() || Inc.model.settingModel.getIntraday().equals("\\")){
            ButtonType yes = new ButtonType("حسنا", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "لم يتم تحديد الملف اللحظي",
                    yes);
            alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

            alert.setTitle("تحديد الملف اللحظي");
            Optional<ButtonType> result = alert.showAndWait();

            return;
        }


        loadIntraday();
//        tbl.refresh();
        updateLastPrice.set(true);

    }
//    private int tempCountTimeLineLoadIntraday = 0;
    public void loadIntraday(){
        System.out.println("XXXsss");
        File f = new File(Inc.model.settingModel.getIntraday()+"MASTER");
        if(!f.exists() || !f.isFile() || Inc.model.settingModel.getIntraday().equals("\\")){
            return;
        }
//        tempCountTimeLineLoadIntraday++;
        if(!isLoading.get()) {
            isLoading.set(true);
            progLoad.setSecondaryProgress(1);
            progLoad.setProgress(0.01);
            progLoad.setVisible(true);
            lblLoad.setText("جاري تحميل اخر سعر ,,,,,,,,,");
            ArrayList<RowModel> newRowModelArr = new ArrayList<>();

            Platform.runLater(() -> {
                progLoad.setProgress(0.05);
                new Thread(() -> {
                    progLoad.setProgress(0.10);
                    workGroupIntradayModel = null;
                    workGroupIntradayModel = WorkGroupModel.genereate(Inc.model.settingModel.getIntraday(),progLoad,false);
                    if(workGroupIntradayModel == null){
                        Platform.runLater(() -> {
                            lblLoad.setText("حدث خطأ اثناء قرائة الملف لتعارضه مع برنامج آخر");
                            progLoad.setSecondaryProgress(0);
                            progLoad.setProgress(0);
                            isLoading.set(false);
//                            progLoad.setVisible(false);
                        });
                        return;
                    }


                    HashMap<String, QuoteModel> map = workGroupIntradayModel.getLastQuoteMap();
//                    for (RowModel i:obsTbl) {
//                        i.setRefrishClose(0);
//                    }

                    for (RowModel i:obsTbl) {
                        if(!i.initIntraday_v2(map.get(i.getTicker()))){
                            newRowModelArr.add(new RowModel(i.getTicker(),0.0f));
                        }
                    }
//                    obsTbl.addAll(newRowModelArr);


                    progLoad.setSecondaryProgress(0);
                    progLoad.setProgress(0);
                    isLoading.set(false);
                    progLoad.setVisible(false);

                    Platform.runLater(() -> {
                        tbl.refresh();
                        System.gc();
                    });

//                    tempCountTimeLineLoadIntraday = 0;
                }).start();
            });

        }else{
            System.out.println("جاري التحميل");
//            new Thread(() -> {
//                Platform.runLater(() -> {
//                    if(tempCountTimeLineLoadIntraday >= 2){
//                        timelineUpdateLastPrice.stop();
//                        tempCountTimeLineLoadIntraday = 0;
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                        }
//                        timelineUpdateLastPrice.play();
//                    }
//                });
//            }).start();
        }
    }

    @FXML public void onClickReset(){
        updateLastPrice.set(false);
    }

    public Stage tempStageSrc = null;
    private void popup_Src(){
        if(tempStageSrc == null){
            tempStageSrc = new Stage();
        }
        if(tempStageSrc.getScene() == null){
            tempStageSrc.setScene(new Scene(Inc.gui.gui_Src));
        }else{
            if(!tempStageSrc.isShowing()){
                tempStageSrc.show();
                Inc.controller.src.init();
                return;
            }
        }
        if(tempStageSrc.isShowing()){
            return;
        }
        tempStageSrc.initModality(Modality.WINDOW_MODAL);
        Inc.controller.src.init();
        tempStageSrc.show();
    }
    @FXML public void onClickSort_1(){
        tbl.getSortOrder().clear();
        tcN.setSortType(TableColumn.SortType.ASCENDING);
        tbl.getSortOrder().add(tcN);
        tcN.setSortable(true);
        tcN.setSortable(false);
//        System.out.println("TableColumn.SortType.ASCENDING");
    }
    @FXML public void onClickSort_2(){
        tbl.getSortOrder().clear();
        tcN.setSortType(TableColumn.SortType.DESCENDING);
        tbl.getSortOrder().add(tcN);
        tcN.setSortable(true);
        tcN.setSortable(false);
//        System.out.println("TableColumn.SortType.DESCENDING");
    }
    @FXML public void onClickSort_3(){
        tbl.getSortOrder().clear();
        tcTicker.setSortType(TableColumn.SortType.ASCENDING);
        tbl.getSortOrder().add(tcTicker);
        tcTicker.setSortable(true);
        tcTicker.setSortable(false);
//        System.out.println("null");
    }


    @FXML public void onClickWallet(){
        popup_Wallet();
    }


    public Stage tempStageWallet = null;
    private void popup_Wallet(){
        if(tempStageWallet == null){
            tempStageWallet = new Stage();
        }
        if(tempStageWallet.getScene() == null){
            tempStageWallet.setScene(new Scene(Inc.gui.gui_Wallet));
            tempStageWallet.getScene().getStylesheets().add(IndexView.class.getResource("css.css").toExternalForm());
        }else{
            if(!tempStageWallet.isShowing()){
                tempStageWallet.show();
                Inc.controller.wallet.init();
                return;
            }
        }
        if(tempStageWallet.isShowing()){
            return;
        }
        tempStageWallet.initModality(Modality.WINDOW_MODAL);
        Inc.controller.wallet.init();
        tempStageWallet.show();
    }

//    public int getBusinessDays(LocalDate startInclusive, LocalDate endExclusive) {
//        if (startInclusive.isAfter(endExclusive)) {
////            String msg = "Start date " + startInclusive
////                    + " must be earlier than end date " + endExclusive;
////            throw new IllegalArgumentException(msg);
//            return 0;
//        }
//        int businessDays = 0;
//        LocalDate d = startInclusive;
//        while (d.isBefore(endExclusive)) {
//            DayOfWeek dw = d.getDayOfWeek();
//            if (dw != DayOfWeek.FRIDAY
//                    && dw != DayOfWeek.SATURDAY) {
//                businessDays++;
//            }
//            d = d.plusDays(1);
//        }
//        if(!endExclusive.getDayOfWeek().equals(DayOfWeek.FRIDAY) && !endExclusive.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
//            businessDays++;
//        }
//        return businessDays;
//    }
//    public LocalDate getLastOfWorkDaysAsLocalDate(LocalDate date, int lastOfWorkDays) {
//        if (lastOfWorkDays <= 0) {
//            return null;
//        }
//        int businessDays = 0;
//        LocalDate d = null;
//        do{
//            d = date.minusDays(businessDays);
//
//        }while (d.isBefore(d));
//
//        while (d.isBefore(endExclusive)) {
//            DayOfWeek dw = d.getDayOfWeek();
//            if (dw != DayOfWeek.FRIDAY
//                    && dw != DayOfWeek.SATURDAY) {
//                businessDays++;
//            }
//            d = d.plusDays(1);
//        }
//
//        return LocalDate.now();
//    }

    public long getAllDays(int dayOfWeek, long businessDays) {
        long result = 0;
        if (businessDays != 0) {
            boolean isStartOnWorkday = dayOfWeek < 6;
            long absBusinessDays = Math.abs(businessDays);

            if (isStartOnWorkday) {
                // if negative businessDays: count backwards by shifting weekday
                int shiftedWorkday = businessDays > 0 ? dayOfWeek : 6 - dayOfWeek;
                result = absBusinessDays + (absBusinessDays + shiftedWorkday - 1) / 5 * 2;
            } else { // start on weekend
                // if negative businessDays: count backwards by shifting weekday
                int shiftedWeekend = businessDays > 0 ? dayOfWeek : 13 - dayOfWeek;
                result = absBusinessDays + (absBusinessDays - 1) / 5 * 2 + (7 - shiftedWeekend);
            }
        }
        return result;
    }
}
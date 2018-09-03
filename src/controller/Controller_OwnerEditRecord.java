package controller;

import application.Main;
import data.Data_FamilyTable;
import data.Data_HouseTable;
import data.Data_OwnerTable;
import data.Data_ParkingTable;
import util.SQL_Connect;
import util.StageManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.regex.Pattern;

public class Controller_OwnerEditRecord {
    public Data_OwnerTable data_ownerTable;
    //业主信息组件
    /*业主信息    ONo业主编号    OName业主姓名    OSex业主性别    OTel业主电话    OID业主身份证号码    ONote业主备注*/
    public Label ONo_Label,OwnerSave_Label;
    public TextField OName_TextField,OTel_TextField,OID_TextField;
    public ChoiceBox OSex_ChoiceBox;
    public TextArea ONote_TextArea;
    //房屋信息组件
    /*房屋信息    HNo房屋编号    HBuild房屋楼宇号    HPark房屋单元号    HFloor房屋楼层号    HRoom房屋门牌号    HArea房屋面积    HState房屋状态    HType房屋户型    HNote房屋备注    ONo业主编号*/
    public TableView<Data_HouseTable> House_TableView;
    public TableColumn<Data_HouseTable,String> HNo_TableColumn;
    public TableColumn<Data_HouseTable,String> HBuild_TableColumn;
    public TableColumn<Data_HouseTable,String> HPark_TableColumn;
    public TableColumn<Data_HouseTable,String> HFloor_TableColumn;
    public TableColumn<Data_HouseTable,String> HRoom_TableColumn;
    public TableColumn<Data_HouseTable,String> HArea_TableColumn;
    public TableColumn<Data_HouseTable,String> HType_TableColumn;
    public TableColumn<Data_HouseTable,String> HState_TableColumn;
    public TableColumn<Data_HouseTable,String> HNote_TableColumn;
    ObservableList<Data_HouseTable> HouseTableView_List = FXCollections.observableArrayList();
    //车位信息组件
    /*车位信息    PNo车位编号    PRegion车位区域    PState车位状态    CarNo车牌号    PNote车位备注    ONo业主编号*/
    public TableView<Data_ParkingTable> Parking_TableView;
    public TableColumn<Data_ParkingTable ,String> PRegion_TableColumn;
    public TableColumn<Data_ParkingTable ,String> PNo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> PState_TableColumn;
    public TableColumn<Data_ParkingTable ,String> CarNo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> PNote_TableColumn;
    ObservableList<Data_ParkingTable> ParkingTableView_List = FXCollections.observableArrayList();
    //家庭成员信息组件
    /*家庭成员信息    FNo家庭成员编号    FName家庭成员姓名    FSex家庭成员性别    FTel家庭成员电话    FID家庭成员身份证号码    FRelation与业主关系    FNote家庭成员备注*/
    public TableView<Data_FamilyTable> Family_TableView;
    public TableColumn<Data_FamilyTable ,String> FName_TableColumn;
    public TableColumn<Data_FamilyTable ,String> FSex_TableColumn;
    public TableColumn<Data_FamilyTable ,String> FTel_TableColumn;
    public TableColumn<Data_FamilyTable ,String> FID_TableColumn;
    public TableColumn<Data_FamilyTable ,String> FRelation_TableColumn;
    public TableColumn<Data_FamilyTable ,String> FNote_TableColumn;
    ObservableList<Data_FamilyTable> FamilyTableView_List = FXCollections.observableArrayList();
    //数据库代码以及返回结果
    String query;
    ResultSet result;
    //检查是否已保存业主信息
    int cheak_OwnerSave;

    public void setdata_ownerTable(Data_OwnerTable data_ownerTable){
        //传参业主信息
        this.data_ownerTable = data_ownerTable;
        ONo_Label.setText("#" + data_ownerTable.getONo().get().trim());
        OName_TextField.setText(data_ownerTable.getOName().get().trim());
        OSex_ChoiceBox.setValue(data_ownerTable.getOSex().get().trim());
        OTel_TextField.setText(data_ownerTable.getOTel().get().trim());
        OID_TextField.setText(data_ownerTable.getOID().get().trim());
        ONote_TextArea.setText(data_ownerTable.getONote().get());
        //默认置业主信息已修改，如果输入框变更就会置0
        cheak_OwnerSave = 1;
        //从数据库读取数据并显示在House_TableView中
        showHouseTableView();
        //从数据库读取数据并显示在Parking_TableView中
        showParkingTableView();
        //从数据库读取数据并显示在Family_TableView中
        showFamilyTableView();
    }
    public void initialize(){
        //初始化
        //将"业主管理-编辑"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_OwnerEditRecord", this);
        //ChoiceBox加入选择项
        OSex_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );
        //业主信息输入框变更监听
        OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cheak_OwnerSave = 0;
            }
        });
        OSex_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cheak_OwnerSave = 0;
            }
        });
        OTel_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cheak_OwnerSave = 0;
            }
        });
        OID_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cheak_OwnerSave = 0;
            }
        });
        ONote_TextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                cheak_OwnerSave = 0;
            }
        });
        //设置TableView数据来自ObservableList
        House_TableView.setItems(HouseTableView_List);
        //将每个TableColumn列分别与对应的Data的get方法绑定
        HNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHNo());
        HBuild_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHBuild() );
        HPark_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHPark() );
        HFloor_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHFloor() );
        HRoom_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHRoom() );
        HArea_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHArea() );
        HType_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHType() );
        HState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHState() );
        HNote_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHNote() );
        //设置TableView数据来自ObservableList
        Parking_TableView.setItems(ParkingTableView_List);
        //将每个TableColumn列分别与对应的Data的get方法绑定
        PRegion_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPRegion());
        PNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPNo() );
        PState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPState() );
        CarNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCarNo() );
        PNote_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPNote() );
        //设置TableView数据来自ObservableList
        Family_TableView.setItems(FamilyTableView_List);
        //将每个TableColumn列分别与对应的Data的get方法绑定
        FName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getFName());
        FSex_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getFSex() );
        FTel_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getFTel() );
        FID_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getFID() );
        FRelation_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getFRelation() );
        FNote_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getFNote() );
        //TableView的双击监听
        House_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_HouseEditLabel();
                }
            }
        });
        Parking_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_ParkingEditLabel();
                }
            }
        });
        Family_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_FamilyEditLabel();
                }
            }
        });
    }
    void showHouseTableView(){
        //从数据库读取数据并显示在TableView中
        //数据库指令
        query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,ONo FROM House_Info WHERE ONo=" + data_ownerTable.getONo().get();
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                HouseTableView_List.add(new Data_HouseTable(result.getString("HNo"),
                        result.getString("HBuild") + "幢",
                        result.getString("HPark") + "单元",
                        result.getString("HFloor") + "层",
                        result.getString("HRoom") + "室",
                        result.getString("HArea") + "㎡",
                        result.getString("HState"),
                        result.getString("HType"),
                        result.getString("HNote"),
                        result.getString("ONo"),
                        data_ownerTable.getOName().get(),
                        data_ownerTable.getOSex().get(),
                        data_ownerTable.getOTel().get(),
                        data_ownerTable.getOID().get(),
                        data_ownerTable.getONote().get()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    void showParkingTableView(){
        //从数据库读取数据并显示在Parking_TableView中
        //数据库指令
        query = "SELECT PRegion,PNo,PState,CarNo,PNote,ONo FROM Parking_Info WHERE ONo=" + data_ownerTable.getONo().get();
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                ParkingTableView_List.add(new Data_ParkingTable(result.getString("PNo"),
                        result.getString("PRegion") + "区",
                        result.getString("PState"),
                        result.getString("CarNo"),
                        result.getString("PNote"),
                        result.getString("ONo"),
                        data_ownerTable.getOName().get(),
                        data_ownerTable.getOSex().get(),
                        data_ownerTable.getOTel().get(),
                        data_ownerTable.getOID().get(),
                        data_ownerTable.getONote().get()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    void showFamilyTableView(){
        //从数据库读取数据并显示在Family_TableView中
        //数据库指令
        query = "SELECT FNo,FName,FSex,FTel,FID,FRelation,FNote,ONo FROM Family_Info WHERE ONo=" + data_ownerTable.getONo().get();
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            //while循环分别获取数据直到ResultSet的结尾，并new一个使用Data方法赋值的data变量
            while (result.next()){
                FamilyTableView_List.add(new Data_FamilyTable(result.getString("FNo"),
                        result.getString("FName"),
                        result.getString("FSex"),
                        result.getString("FTel"),
                        result.getString("FID"),
                        result.getString("FRelation"),
                        result.getString("FNote"),
                        result.getString("ONo")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_OwnerSaveLabel(){
        //按下业主信息-"保存"
        //确认是否修改该保修单
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否修改该业主信息？");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            //如果选择了"否"
            return;
        }
        if (OName_TextField.getText()==null || OName_TextField.getText().length()==0){
            //如果未输入业主姓名
            error_NullOName();
            return;
        }
        if (length(OName_TextField.getText())>8){
            //业主姓名超出长度
            error_LangOName();
            return;
        }
        if (OTel_TextField.getText()==null || OTel_TextField.getText().length()==0){
            //如果未输入业主联系电话(手机号码或者固定电话)
            error_NullOTel();
            return;
        }
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";      //手机号码正则
        String regex2 = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";  //固定电话正则
        if (Pattern.compile(regex).matcher(OTel_TextField.getText()).matches() == false && Pattern.compile(regex2).matcher(OTel_TextField.getText()).matches() == false){
            //业主联系电话(手机号码或者固定电话)输入错误
            error_WrongOTel();
            return;
        }
        if (OID_TextField.getText()==null || OID_TextField.getText().length()==0){
            //如果未输入业主身份证号码
            error_NullOID();
            return;
        }
        regex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";    //身份证号码正则
        if (Pattern.compile(regex).matcher(OID_TextField.getText()).matches() == false){
            //业主身份证号码输入错误
            error_WrongOID();
            return;
        }
        if (ONote_TextArea.getText()==null || ONote_TextArea.getText().length()==0){
            //业主备注为空时
            //修改数据到数据库(业主备注为空)
            editOwnerInfoToSQL_NullONote();
            //刷新"业主信息管理"窗口的TableView
            flush_TableView();
            //成功修改弹窗
            succeed_Edit();
            //置业主信息已修改
            cheak_OwnerSave = 1;
        }
        else {
            if (length(ONote_TextArea.getText())>100){
                //业主备注超出长度
                error_LangONote();
                return;
            }
            //业主备注不为空时
            //修改数据到数据库(业主备注不为空)
            editOwnerInfoToSQL_FullONote();
            //刷新"业主信息管理"窗口的TableView
            flush_TableView();
            //成功修改弹窗
            succeed_Edit();
            //置业主信息已修改
            cheak_OwnerSave = 1;
        }
    }
    void editOwnerInfoToSQL_NullONote(){
        //修改数据到数据库(业主备注为空)
        query = "UPDATE Owner_Info SET " +
                "OName=\'" + OName_TextField.getText() + "\'," +
                "OSex=\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString() + "\'," +
                "OTel=\'" + OTel_TextField.getText() + "\'," +
                "OID=\'" + OID_TextField.getText() + "\'," +
                "ONote=NULL " +
                "WHERE ONo=" + data_ownerTable.getONo().get();
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editOwnerInfoToSQL_FullONote(){
        //修改数据到数据库(业主备注不为空)
        query = "UPDATE Owner_Info SET " +
                "OName=\'" + OName_TextField.getText() + "\'," +
                "OSex=\'" + OSex_ChoiceBox.getSelectionModel().getSelectedItem().toString() + "\'," +
                "OTel=\'" + OTel_TextField.getText() + "\'," +
                "OID=\'" + OID_TextField.getText() + "\'," +
                "ONote=\'" + ONote_TextArea.getText() + "\' " +
                "WHERE ONo=" + data_ownerTable.getONo().get();
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    public void click_HouseNewLabel(){
        //单击房屋信息-"新增"文本
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"业主信息管理-编辑-房屋信息-新增"窗口
            Stage Stage_OwnerEditRecord_NewHouse;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord_NewHouse.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord_NewHouse = new Stage();
            Stage_OwnerEditRecord_NewHouse.setTitle("业主信息管理 - 房屋信息 - 新增");
            Stage_OwnerEditRecord_NewHouse.setScene(new Scene(page, 350, 66));
            Stage_OwnerEditRecord_NewHouse.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord_NewHouse.setX((Main.width-350)/2);
            Stage_OwnerEditRecord_NewHouse.setY((Main.height-66)/2);
            Stage_OwnerEditRecord_NewHouse.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord_NewHouse.setResizable(false);
            Stage_OwnerEditRecord_NewHouse.show();
            //将"业主信息管理 - 房屋信息 - 新增"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord_NewHouse", Stage_OwnerEditRecord_NewHouse);
            //从map调取"业主信息管理 - 房屋信息 - 新增"控制器并调用set方法传投诉单数据
            Controller_OwnerEditRecord_NewHouse controller_ownerEditRecord_newHouse=(Controller_OwnerEditRecord_NewHouse) StageManager.CONTROLLER.get("Controller_OwnerEditRecord_NewHouse");
            controller_ownerEditRecord_newHouse.setONo(data_ownerTable.getONo().get());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"业主信息管理 - 房屋信息 - 新增"窗口和其控制器
            Stage_OwnerEditRecord_NewHouse.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_OwnerEditRecord_NewHouse");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_NewHouse");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_HouseEditLabel(){
        //单击房屋信息-"编辑"文本
        //未选择需要编辑的信息的报错
        if(House_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(StageManager.STAGE.get("Stage_OwnerEditRecord"));
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"业主信息管理-编辑-房屋信息-新增"窗口
            Stage Stage_OwnerEditRecord_EditHouse;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord_EditHouse.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord_EditHouse = new Stage();
            Stage_OwnerEditRecord_EditHouse.setTitle("业主信息管理 - 房屋信息 - 修改");
            Stage_OwnerEditRecord_EditHouse.setScene(new Scene(page, 350, 66));
            Stage_OwnerEditRecord_EditHouse.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord_EditHouse.setX((Main.width-350)/2);
            Stage_OwnerEditRecord_EditHouse.setY((Main.height-66)/2);
            Stage_OwnerEditRecord_EditHouse.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord_EditHouse.setResizable(false);
            Stage_OwnerEditRecord_EditHouse.show();
            //将"业主信息管理 - 房屋信息 - 修改"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord_EditHouse", Stage_OwnerEditRecord_EditHouse);
            //从map调取"业主信息管理 - 房屋信息 - 修改"控制器并调用set方法传投诉单数据
            Controller_OwnerEditRecord_EditHouse controller_ownerEditRecord_editHouse=(Controller_OwnerEditRecord_EditHouse) StageManager.CONTROLLER.get("Controller_OwnerEditRecord_EditHouse");
            controller_ownerEditRecord_editHouse.setHNo(House_TableView.getSelectionModel().getSelectedItem().getHNo().get());
            controller_ownerEditRecord_editHouse.setHState(House_TableView.getSelectionModel().getSelectedItem().getHState().get());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"业主信息管理 - 房屋信息 - 修改"窗口和其控制器
            Stage_OwnerEditRecord_EditHouse.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Controller_OwnerEditRecord_EditHouse");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_EditHouse");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ParkingNewLabel(){
        //单击车位信息-"新增"文本
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"业主信息管理-编辑-车位信息-新增"窗口
            Stage Stage_OwnerEditRecord_NewParking;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord_NewParking.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord_NewParking = new Stage();
            Stage_OwnerEditRecord_NewParking.setTitle("业主信息管理 - 车位信息 - 新增");
            Stage_OwnerEditRecord_NewParking.setScene(new Scene(page, 500, 66));
            Stage_OwnerEditRecord_NewParking.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord_NewParking.setX((Main.width-500)/2);
            Stage_OwnerEditRecord_NewParking.setY((Main.height-66)/2);
            Stage_OwnerEditRecord_NewParking.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord_NewParking.setResizable(false);
            Stage_OwnerEditRecord_NewParking.show();
            //将"业主信息管理 - 车位信息 - 新增"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord_NewParking", Stage_OwnerEditRecord_NewParking);
            //从map调取"业主信息管理 - 房屋信息 - 新增"控制器并调用set方法传投诉单数据
            Controller_OwnerEditRecord_NewParking controller_ownerEditRecord_newParking =(Controller_OwnerEditRecord_NewParking) StageManager.CONTROLLER.get("Controller_OwnerEditRecord_NewParking");
            controller_ownerEditRecord_newParking.setONo(data_ownerTable.getONo().get());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"业主信息管理 - 房屋信息 - 新增"窗口和其控制器
            Stage_OwnerEditRecord_NewParking.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_OwnerEditRecord_NewParking");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_NewParking");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ParkingEditLabel(){
        //单击车位信息-"编辑"文本
        //未选择需要编辑的信息的报错
        if(Parking_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(StageManager.STAGE.get("Stage_OwnerEditRecord"));
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"业主信息管理-编辑-车位信息-新增"窗口
            Stage Stage_OwnerEditRecord_EditParking;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord_EditParking.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord_EditParking = new Stage();
            Stage_OwnerEditRecord_EditParking.setTitle("业主信息管理 - 车位信息 - 修改");
            Stage_OwnerEditRecord_EditParking.setScene(new Scene(page, 500, 66));
            Stage_OwnerEditRecord_EditParking.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord_EditParking.setX((Main.width-500)/2);
            Stage_OwnerEditRecord_EditParking.setY((Main.height-66)/2);
            Stage_OwnerEditRecord_EditParking.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord_EditParking.setResizable(false);
            Stage_OwnerEditRecord_EditParking.show();
            //将"业主信息管理 - 车位信息 - 修改"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord_EditParking", Stage_OwnerEditRecord_EditParking);
            //从map调取"业主信息管理 - 车位信息 - 修改"控制器并调用set方法传投诉单数据
            Controller_OwnerEditRecord_EditParking controller_ownerEditRecord_editParking=(Controller_OwnerEditRecord_EditParking) StageManager.CONTROLLER.get("Controller_OwnerEditRecord_EditParking");
            controller_ownerEditRecord_editParking.setPNo(Parking_TableView.getSelectionModel().getSelectedItem().getPNo().get());
            controller_ownerEditRecord_editParking.setPState(Parking_TableView.getSelectionModel().getSelectedItem().getPState().get());
            controller_ownerEditRecord_editParking.setCarNo(Parking_TableView.getSelectionModel().getSelectedItem().getCarNo().get());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"主信息管理 - 车位信息 - 修改"窗口和其控制器
            Stage_OwnerEditRecord_EditParking.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Controller_OwnerEditRecord_EditParking");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_EditParking");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_FamilyNewLabel(){
        //单击家庭成员信息-"新增"文本
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"业主信息管理 - 编辑 - 家庭成员信息 - 新增"窗口
            Stage Stage_OwnerEditRecord_NewFamily;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord_NewFamily.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord_NewFamily = new Stage();
            Stage_OwnerEditRecord_NewFamily.setTitle("业主信息管理 - 家庭成员信息 - 新增");
            Stage_OwnerEditRecord_NewFamily.setScene(new Scene(page, 333, 245));
            Stage_OwnerEditRecord_NewFamily.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord_NewFamily.setX((Main.width-333)/2);
            Stage_OwnerEditRecord_NewFamily.setY((Main.height-245)/2);
            Stage_OwnerEditRecord_NewFamily.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord_NewFamily.setResizable(false);
            Stage_OwnerEditRecord_NewFamily.show();
            //将"业主信息管理 - 家庭成员信息 - 新增"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord_NewFamily", Stage_OwnerEditRecord_NewFamily);
            //从map调取"业主信息管理 - 家庭成员信息 - 新增"控制器并调用set方法传投诉单数据
            Controller_OwnerEditRecord_NewFamily controller_ownerEditRecord_newFamily =(Controller_OwnerEditRecord_NewFamily) StageManager.CONTROLLER.get("Controller_OwnerEditRecord_NewFamily");
            controller_ownerEditRecord_newFamily.setONo(data_ownerTable.getONo().get());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"业主信息管理 - 房屋信息 - 新增"窗口和其控制器
            Stage_OwnerEditRecord_NewFamily.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_OwnerEditRecord_NewFamily");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_NewFamily");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_FamilyEditLabel() {
        //单击家庭成员信息-"编辑"文本
        //未选择需要编辑的信息的报错
        if (Family_TableView.getSelectionModel().getSelectedIndex() < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(StageManager.STAGE.get("Stage_OwnerEditRecord"));
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try {
            //创建"业主信息管理-编辑-家庭成员信息-新增"窗口
            Stage Stage_OwnerEditRecord_EditFamily;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_OwnerEditRecord_EditFamily.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_OwnerEditRecord_EditFamily = new Stage();
            Stage_OwnerEditRecord_EditFamily.setTitle("业主信息管理 - 家庭成员信息 - 修改");
            Stage_OwnerEditRecord_EditFamily.setScene(new Scene(page, 333, 245));
            Stage_OwnerEditRecord_EditFamily.getIcons().add(new Image("/image/logo.png"));
            Stage_OwnerEditRecord_EditFamily.setX((Main.width - 333) / 2);
            Stage_OwnerEditRecord_EditFamily.setY((Main.height - 245) / 2);
            Stage_OwnerEditRecord_EditFamily.initModality(Modality.APPLICATION_MODAL);
            Stage_OwnerEditRecord_EditFamily.setResizable(false);
            Stage_OwnerEditRecord_EditFamily.show();
            //将"业主信息管理 - 家庭成员信息 - 修改"窗口保存到map中
            StageManager.STAGE.put("Stage_OwnerEditRecord_EditFamily", Stage_OwnerEditRecord_EditFamily);
            //从map调取"业主信息管理 - 家庭成员信息 - 修改"控制器并调用set方法传数据
            Controller_OwnerEditRecord_EditFamily controller_ownerEditRecord_editFamily = (Controller_OwnerEditRecord_EditFamily) StageManager.CONTROLLER.get("Controller_OwnerEditRecord_EditFamily");
            controller_ownerEditRecord_editFamily.setFNo(Family_TableView.getSelectionModel().getSelectedItem().getFNo().get());
            controller_ownerEditRecord_editFamily.setFame(Family_TableView.getSelectionModel().getSelectedItem().getFName().get());
            controller_ownerEditRecord_editFamily.setFSex(Family_TableView.getSelectionModel().getSelectedItem().getFSex().get());
            controller_ownerEditRecord_editFamily.setFTel(Family_TableView.getSelectionModel().getSelectedItem().getFTel().get());
            controller_ownerEditRecord_editFamily.setFID(Family_TableView.getSelectionModel().getSelectedItem().getFID().get());
            controller_ownerEditRecord_editFamily.setFRelation(Family_TableView.getSelectionModel().getSelectedItem().getFRelation().get());
            controller_ownerEditRecord_editFamily.setFNote(Family_TableView.getSelectionModel().getSelectedItem().getFNote().get());
            //监听"业主信息-修改"窗口如果按窗口右上角X退出，remove"业主信息管理 - 家庭成员信息信息 - 修改"窗口和其控制器
            Stage_OwnerEditRecord_EditFamily.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Controller_OwnerEditRecord_EditFamily");
                    StageManager.CONTROLLER.remove("Controller_OwnerEditRecord_EditFamily");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_FamilyDelLabel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否删除该家庭成员信息？");
        alert.initOwner(Family_TableView.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        query = "DELETE Family_Info WHERE FNo=" + Family_TableView.getSelectionModel().getSelectedItem().getFNo().get();
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
        FamilyTableView_List.clear();
        showFamilyTableView();
    }
    void flush_TableView(){
        //刷新"维修单管理-主界面"窗口的TableView
        Controller_OwnerMain controller_ownerMain = (Controller_OwnerMain) StageManager.CONTROLLER.get("Controller_OwnerMain");
        controller_ownerMain.OwnerTableView_List.clear();
        controller_ownerMain.showOwnerTableView();
    }
    void error_NullOName(){
        //未输入业主姓名时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主姓名不能为空，请输入姓名！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangOName(){
        //业主姓名超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主姓名超出长度，仅能输入8个字符(每个汉字占2个字符)！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOTel(){
        //未输入业主联系方式的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主联系电话不能为空，请输入联系电话！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongOTel(){
        //业主联系电话(手机号码或者固定电话)输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到业主联系电话输入错误，请输入正确的电话号码！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullOID(){
        //未输入业主身份证号码的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主身份证号码不能为空，请输入身份证号码！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongOID(){
        //业主身份证号码输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到业主身份证号码输入错误，请输入正确的身份证号码！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangONote(){
        //业主备注超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主备注超出长度，仅能输入100个字符(每个汉字占2个字符)！");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    void succeed_Edit(){
        //成功修改弹窗
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("业主修改成功");
        alert.initOwner(OwnerSave_Label.getScene().getWindow());
        alert.showAndWait();
    }
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            //获取一个字符
            String temp = value.substring(i, i + 1);
            //判断是否为中文字符
            if (temp.matches(chinese)) {
                //中文字符长度为2
                valueLength += 2;
            } else {
                //其他字符长度为1
                valueLength ++;
            }
        }
        return valueLength;
    }
}

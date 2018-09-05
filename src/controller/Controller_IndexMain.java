package controller;

import data.Data_HouseTable;
import javafx.collections.ObservableList;
import javafx.stage.WindowEvent;
import util.SQL_Connect;
import util.StageManager;
import application.Main;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.Optional;

public class Controller_IndexMain {
    public Label LoginUser_Label;

    public TreeTableView<String> HBulid_TreeTableView;
    public TreeTableColumn<String,String> HBuild_TreeTableColumn;
    public TreeItem<String> HBuild_Root;

    /*房屋信息    HNo房屋编号    HBuild房屋楼宇号    HPark房屋单元号    HFloor房屋楼层号    HRoom房屋门牌号    HArea房屋面积    HState房屋状态    HType房屋户型    HNote房屋备注    ONo业主编号*/
    public TableView<Data_HouseTable> House_TableView;
    public TableColumn<Data_HouseTable,String> HNo_TableColumn;
    public TableColumn<Data_HouseTable,String> HBuild_TableColumn;
    public TableColumn<Data_HouseTable,String> HPark_TableColumn;
    public TableColumn<Data_HouseTable,String> HFloor_TableColumn;
    public TableColumn<Data_HouseTable,String> HRoom_TableColumn;
    public TableColumn<Data_HouseTable,String> HArea_TableColumn;
    public TableColumn<Data_HouseTable,String> HState_TableColumn;
    public TableColumn<Data_HouseTable,String> ONo_TableColumn;
    public TableColumn<Data_HouseTable,String> OName_TableColumn;
    ObservableList<Data_HouseTable> HouseTableData_List = FXCollections.observableArrayList();

    public Label HNo_Label,HAddress_Label,HArea_Label,HState_Label,HType_Label,HNote_Label;
    public Label ONo_Label,OName_Label,OSex_Label,OTel_Label,OID_Label,ONote_Label;

    public TextField Search_OName_TextField,Search_HBuild_TextField,Search_HPark_TextField,Search_HFloor_TextField,Search_HRoom_TextField;

    String query;
    ResultSet result;

    public void initialize() {
        //初始化
        //将"报修单管理-主界面"控制器保存到map中
        StageManager.CONTROLLER.put("Controller_IndexMain", this);
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //设置TreeTableView的根TreeItem
        HBuild_Root = new TreeItem<>("显示所有");
        HBuild_Root.setGraphic(new ImageView (new Image(getClass().getResourceAsStream("/image/building.png"))));
        HBuild_Root.setExpanded(true);
        //定义列的单元格内容
        HBuild_TreeTableColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue()));
        HBulid_TreeTableView.setRoot(HBuild_Root);

        //初始化TreeTableView
        showHouseTreeTable();

        //选择行监听
        HBulid_TreeTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showHouseTable(newValue.getValue()));
        House_TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMoreInfo(newValue));

        House_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    click_EditButton();
                }
            }
        });

        //House_TableView各列从HouseTableData取值
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
        HState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getHState() );
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo() );
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName() );

        House_TableView.setItems(HouseTableData_List);

        //初始化House_TableView，读取数据库中所有数据
        showHouseTable("初始化");

        //搜索文本栏变动监听
        Search_OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_House();
            }
        });
        Search_HBuild_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_House();
            }
        });
        Search_HPark_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_House();
            }
        });
        Search_HFloor_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_House();
            }
        });
        Search_HRoom_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_House();
            }
        });
    }
    public void showHouseTreeTable(){
        //加载TreeTableView数据，此处为初始化，读取数据库中有多少楼宇并显示
        SQL_Connect sql_connect = new SQL_Connect();
        query = "SELECT HBuild FROM House_Info GROUP BY HBuild";
        result = sql_connect.sql_Query(query);
        try{
            while (result.next()) {
                HBuild_Root.getChildren().add(new TreeItem<>(result.getString("HBuild")+"幢"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showHouseTable(String buildNum){
        //加载TableView数据
        //清空原有数据
        HouseTableData_List.clear();
        if(buildNum.equals("初始化")|| buildNum.equals("显示所有")) {
            //初始化时数据库指令
            query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo";
        }
        else {
            //选择对应楼宇时数据库指令
            query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo WHERE HBuild=" + buildNum.replace("幢","");
        }
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                HouseTableData_List.add(new Data_HouseTable(result.getString("HNo"),
                        result.getString("HBuild") + "幢",
                        result.getString("HPark") + "单元",
                        result.getString("HFloor") + "层",
                        result.getString("HRoom") + "室",
                        result.getString("HArea") + "㎡",
                        result.getString("HState"),
                        result.getString("HType").trim(),
                        result.getString("HNote"),
                        result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMoreInfo(Data_HouseTable houseTableData){
        //在TableView中选择后，右侧显示房屋详细信息
        //获取选择行，仅当>=0时才进行显示房屋详细信息
        if(House_TableView.getSelectionModel().getSelectedIndex() >= 0 ){
            HNo_Label.setText(houseTableData.getHNo().getValue());
            HAddress_Label.setText("东莞市寮步镇光明小区" + houseTableData.getHBuild().getValue() + houseTableData.getHPark().getValue() + houseTableData.getHFloor().getValue() + houseTableData.getHRoom().getValue());
            HArea_Label.setText(houseTableData.getHArea().getValue());
            HState_Label.setText(houseTableData.getHState().getValue());
            HType_Label.setText(houseTableData.getHType().getValue());
            HNote_Label.setText(houseTableData.getHNote().getValue());
            ONo_Label.setText(houseTableData.getONo().getValue());
            OName_Label.setText(houseTableData.getOName().getValue());
            OSex_Label.setText(houseTableData.getOSex().getValue());
            OTel_Label.setText(houseTableData.getOTel().getValue());
            OID_Label.setText(houseTableData.getOID().getValue());
            ONote_Label.setText(houseTableData.getONote().getValue());
        }
    }
    public void click_NewButton() {
        //单击"新增"按钮
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"房屋信息管理 - 新增"窗口
            Stage Stage_IndexNewHouse;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_IndexNewHouse.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_IndexNewHouse = new Stage();
            Stage_IndexNewHouse.setTitle("小区物业管理系统 - 房屋信息 - 新增");
            Stage_IndexNewHouse.setScene(new Scene(page, 333, 505));
            Stage_IndexNewHouse.getIcons().add(new Image("/image/logo.png"));
            Stage_IndexNewHouse.setX((Main.width-333)/2);
            Stage_IndexNewHouse.setY((Main.height-505)/2);
            Stage_IndexNewHouse.initModality(Modality.APPLICATION_MODAL);
            Stage_IndexNewHouse.setResizable(false);
            Stage_IndexNewHouse.show();
            //将"房屋信息管理 - 新增"窗口保存到map中
            StageManager.STAGE.put("Stage_IndexNewHouse", Stage_IndexNewHouse);
            //监听"房屋信息管理 - 新增"窗口如果按窗口右上角X退出，remove"房屋信息管理 - 新增"窗口和其控制器
            Stage_IndexNewHouse.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_IndexNewHouse");
                    StageManager.CONTROLLER.remove("Controller_IndexNewHouse");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_EditButton(){
        //单击"新增"按钮
        if(House_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
            return;
        }
        //FXMLLoader的load方法需要try-catch输出报错
        try{
            //创建"房屋信息管理 - 修改"窗口
            Stage Stage_IndexEditHouse;
            //加载FXML窗口
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Controller_IndexMain.class.getResource("/GUI/GUI_IndexEditHouse.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage_IndexEditHouse = new Stage();
            Stage_IndexEditHouse.setTitle("小区物业管理系统 - 房屋信息 - 修改");
            Stage_IndexEditHouse.setScene(new Scene(page, 333, 505));
            Stage_IndexEditHouse.getIcons().add(new Image("/image/logo.png"));
            Stage_IndexEditHouse.setX((Main.width-333)/2);
            Stage_IndexEditHouse.setY((Main.height-505)/2);
            Stage_IndexEditHouse.initModality(Modality.APPLICATION_MODAL);
            Stage_IndexEditHouse.setResizable(false);
            Stage_IndexEditHouse.show();
            //将"房屋信息管理 - 修改"窗口保存到map中
            StageManager.STAGE.put("Stage_IndexEditHouse", Stage_IndexEditHouse);
            //从map调取"房屋信息管理 - 修改"控制器并调用setdata_houseTable方法传房屋数据
            Controller_IndexEditHouse controller_indexEditHouse=(Controller_IndexEditHouse) StageManager.CONTROLLER.get("Controller_IndexEditHouse");
            controller_indexEditHouse.setdata_houseTable(House_TableView.getSelectionModel().getSelectedItem());
            //监听"房屋信息管理 - 修改"窗口如果按窗口右上角X退出，remove"房屋信息管理 - 修改"窗口和其控制器
            Stage_IndexEditHouse.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    StageManager.STAGE.remove("Stage_IndexEditHouse");
                    StageManager.CONTROLLER.remove("Controller_IndexEditHouse");
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_DelButton(){
        //SQL语句
        if(House_TableView.getSelectionModel().getSelectedIndex() < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要删除的信息，无法删除");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您确认是否删除该条信息？");
            alert.initOwner(Main.Login_Stage);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                //确认删除
                query = "DELETE House_Info WHERE HNo=\'" + HNo_Label.getText().trim() + "\'";
                try{
                    SQL_Connect sql_connect = new SQL_Connect();
                    int sqlresult = sql_connect.sql_Update(query);
                    clearLabelText();
                    if(sqlresult==0) {
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("小区物业管理系统");
                        alert2.setHeaderText("信息删除成功！");
                        alert2.initOwner(Main.Login_Stage);
                        alert2.showAndWait();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                HouseTableData_List.clear();
                showHouseTable("初始化");
            }
        }
    }
    public void clearLabelText(){
        HNo_Label.setText("");
        HAddress_Label.setText("");
        HArea_Label.setText("");
        HState_Label.setText("");
        HType_Label.setText("");
        HNote_Label.setText("");
        ONo_Label.setText("");
        OName_Label.setText("");
        OSex_Label.setText("");
        OTel_Label.setText("");
        OID_Label.setText("");
        ONote_Label.setText("");
    }
    public void search_House(){
        HouseTableData_List.clear();
        query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo WHERE HBuild LIKE \'%" +
                Search_HBuild_TextField.getText() + "%\' AND HPark LIKE \'%" + Search_HPark_TextField.getText() + "%\' AND HFloor LIKE \'%" + Search_HFloor_TextField.getText() +
                "%\' AND HRoom LIKE \'%" + Search_HRoom_TextField.getText() + "%\' AND OName LIKE \'%" + Search_OName_TextField.getText() + "%\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                HouseTableData_List.add(new Data_HouseTable(result.getString("HNo").trim(),
                        result.getString("HBuild").trim() + "幢",
                        result.getString("HPark").trim() + "单元",
                        result.getString("HFloor").trim() + "层",
                        result.getString("HRoom").trim() + "室",
                        result.getString("HArea").trim() + "㎡",
                        result.getString("HState"),
                        result.getString("HType").trim(),
                        result.getString("HNote"),
                        result.getString("ONo"),
                        result.getString("OName"),
                        result.getString("OSex"),
                        result.getString("OTel"),
                        result.getString("OID"),
                        result.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_OwnerToggleButton(){
        //业主管理 界面切换
        try {
            Parent Family_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_OwnerMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统 - 业主管理界面");
            Main.Login_Stage.setScene(new Scene(Family_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_IndexMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_CarToggleButton(){
        //车位管理 界面切换
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_CarMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统 - 车位管理");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_IndexMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ChargeToggleButton(){
        //收费管理 界面切换
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_ChargeMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统 - 收费管理");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_IndexMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_RepairToggleButton(){
        //报修管理 界面切换
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_RepairMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统 - 报修管理");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_IndexMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_ComplaintToggleButton(){
        //投诉管理 界面切换
        try {
            Parent Complaint_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_ComplaintMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统 - 投诉管理");
            Main.Login_Stage.setScene(new Scene(Complaint_Root, 1000, 615));
            StageManager.CONTROLLER.remove("Controller_IndexMain");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
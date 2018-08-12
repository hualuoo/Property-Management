import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.Optional;

public class IndexMain_Controller {
    public TreeItem<String> root;
    public TreeTableView<String> Bulid_TreeTableView;
    public TreeTableColumn<String,String> Build_TreeTableColumn;

    public TableView<HouseTableData> House_TableView;
    public TableColumn<HouseTableData,String> HNo_TableColumn;
    public TableColumn<HouseTableData,String> HBuild_TableColumn;
    public TableColumn<HouseTableData,String> HPark_TableColumn;
    public TableColumn<HouseTableData,String> HFloor_TableColumn;
    public TableColumn<HouseTableData,String> HRoom_TableColumn;
    public TableColumn<HouseTableData,String> HArea_TableColumn;
    public TableColumn<HouseTableData,String> HState_TableColumn;
    public TableColumn<HouseTableData,String> ONo_TableColumn;
    public TableColumn<HouseTableData,String> OName_TableColumn;
    ObservableList<HouseTableData> list = FXCollections.observableArrayList();

    public Label HNo_Label,HAddress_Label,HArea_Label,HState_Label,HType_Label,HNote_Label;
    public Label ONo_Label,OName_Label,OSex_Label,OTel_Label,OID_Label,ONote_Label;
    public Label LoginUser_Label;
    public Button New_Button,Edit_Button,Del_Button;

    public String query;
    public TextField Search_OName_TextField,Search_HBuild_TextField,Search_HPark_TextField,Search_HFloor_TextField,Search_HRoom_TextField;

    public void initialize() {
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //设置TreeTableView的根TreeItem
        root = new TreeItem<>("光明小区");
        root.setGraphic(new ImageView (new Image(getClass().getResourceAsStream("/image/building.png"))));
        root.setExpanded(true);
        //定义列的单元格内容
        Build_TreeTableColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue()));
        Bulid_TreeTableView.setRoot(root);

        //展示TreeTableView
        showHouseTreeTable();

        //选择行监听
        Bulid_TreeTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showHouseTable(newValue.getValue()));
        House_TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMoreInfo(newValue));

        //cellData通过把值传给了tableBiew
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

        House_TableView.setItems(list);

        showHouseTable("初始化");
        //搜索文本栏变动监听
        Search_OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_OName(newValue);
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
    public void car_ToggleButton_Click(){
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("CarGUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-车辆管理界面");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void family_ToggleButton_Click(){
        try {
            Parent Family_Root = FXMLLoader.load(getClass().getResource("FamilyGUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-家庭信息界面");
            Main.Login_Stage.setScene(new Scene(Family_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void charge_ToggleButton_Click(){
        try {
            Parent Charge_Root = FXMLLoader.load(getClass().getResource("ChargeGUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-收费信息界面");
            Main.Login_Stage.setScene(new Scene(Charge_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void repair_ToggleButton_Click(){
        try {
            Parent Repair_Root = FXMLLoader.load(getClass().getResource("RepairGUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-维修信息界面");
            Main.Login_Stage.setScene(new Scene(Repair_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void complaint_ToggleButton_Click(){
        try {
            Parent Complaint_Root = FXMLLoader.load(getClass().getResource("ComplaintGUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-投诉信息界面");
            Main.Login_Stage.setScene(new Scene(Complaint_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showHouseTreeTable(){
        SQL_Connect sql_connect = new SQL_Connect();
        query = "SELECT HBuild FROM House_Info GROUP BY HBuild";
        ResultSet BuildData = sql_connect.sql_Query(query);
        try{
            while (BuildData.next()) {
                root.getChildren().add(new TreeItem<>(BuildData.getString("HBuild")+"幢"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showHouseTable(String buildNum){
        list.clear();
        SQL_Connect sql_connect = new SQL_Connect();
        if(buildNum.equals("初始化")|| buildNum.equals("光明小区")) {
            query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo";
        }
        else {
            query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo WHERE HBuild=" + buildNum.replace("幢","");
        }
        ResultSet HouseData = sql_connect.sql_Query(query);
        try {
            while (HouseData.next()){
                list.add(new HouseTableData(HouseData.getString("HNo").trim(),
                        HouseData.getString("HBuild").trim() + "幢",
                        HouseData.getString("HPark").trim() + "单元",
                        HouseData.getString("HFloor").trim() + "层",
                        HouseData.getString("HRoom").trim() + "室",
                        HouseData.getString("HArea").trim() + "㎡",
                        HouseData.getString("HState"),
                        HouseData.getString("HType").trim(),
                        HouseData.getString("HNote"),
                        HouseData.getString("ONo"),
                        HouseData.getString("OName"),
                        HouseData.getString("OSex"),
                        HouseData.getString("OTel"),
                        HouseData.getString("OID"),
                        HouseData.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMoreInfo(HouseTableData houseTableData){
        int selectedIndex = House_TableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0 ){
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
    public void new_Button_Click() {
        try{
            Stage New_Stage;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(IndexMain_Controller.class.getResource("HouseNewGUI.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            New_Stage = new Stage();
            New_Stage.setTitle("小区物业管理系统-新增");
            New_Stage.setScene(new Scene(page, 333, 505));
            New_Stage.getIcons().add(new Image("/image/logo.png"));
            New_Stage.setX((Main.width-333)/2);
            New_Stage.setY((Main.height-505)/2);
            New_Stage.show();
            New_Stage.setResizable(false);
            HouseNew_Controller controller = loader.getController();
            controller.setDialogStage(New_Stage);
            HouseTableData newhouseTableData = new HouseTableData("","","","","","","","","","","","","","","");
            controller.setHouseTableData(newhouseTableData);
            newhouseTableData = controller.getHouseTableData();
            list.add(newhouseTableData);
            //controller.setHouse(House_Table.getSelectionModel().getSelectedItem());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void edit_Button_Click(){
        if(HNo_Label.getText().trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("您未选择需要编辑的信息，无法编辑");
            alert.initOwner(Main.Login_Stage);
            alert.showAndWait();
        }
        else{
            try{
                Stage Edit_Stage;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(IndexMain_Controller.class.getResource("HouseEditGUI.fxml"));
                AnchorPane page = (AnchorPane) loader.load();
                Edit_Stage = new Stage();
                Edit_Stage.setTitle("小区物业管理系统-修改");
                Edit_Stage.setScene(new Scene(page, 333, 505));
                Edit_Stage.getIcons().add(new Image("/image/logo.png"));
                Edit_Stage.setX((Main.width-333)/2);
                Edit_Stage.setY((Main.height-505)/2);
                Edit_Stage.show();
                Edit_Stage.setResizable(false);
                HouseEdit_Controller controller = loader.getController();
                controller.setDialogStage(Edit_Stage);
                controller.setHouse(House_TableView.getSelectionModel().getSelectedItem());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void del_Button_Click(){
        //SQL语句
        if(HNo_Label.getText().trim().equals("")){
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
                //已写触发器
                query = "ALTER TABLE Owner_Info NOCHECK CONSTRAINT ALL DELETE House_Info WHERE HNo=\'" + HNo_Label.getText().trim() + "\'" + "ALTER TABLE Owner_Info CHECK CONSTRAINT ALL";
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
                list.clear();
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
    public void search_OName(String newValue){
        list.clear();
        if(newValue==null || newValue.length()==0) {
            query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo";
        }
        else {
            query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo WHERE OName LIKE \'%" + Search_OName_TextField.getText().trim() + "%\'";
        }
        SQL_Connect sql_connect = new SQL_Connect();
        ResultSet HouseData = sql_connect.sql_Query(query);
        try {
            while (HouseData.next()){
                list.add(new HouseTableData(HouseData.getString("HNo").trim(),
                        HouseData.getString("HBuild").trim() + "幢",
                        HouseData.getString("HPark").trim() + "单元",
                        HouseData.getString("HFloor").trim() + "层",
                        HouseData.getString("HRoom").trim() + "室",
                        HouseData.getString("HArea").trim() + "㎡",
                        HouseData.getString("HState"),
                        HouseData.getString("HType").trim(),
                        HouseData.getString("HNote"),
                        HouseData.getString("ONo"),
                        HouseData.getString("OName"),
                        HouseData.getString("OSex"),
                        HouseData.getString("OTel"),
                        HouseData.getString("OID"),
                        HouseData.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void search_House(){
        String search_HBuild,search_HPark,search_HFloor,search_HRoom;
        search_HBuild = Search_HBuild_TextField.getText().trim();
        search_HPark = Search_HPark_TextField.getText().trim();
        search_HFloor = Search_HFloor_TextField.getText().trim();
        search_HRoom = Search_HRoom_TextField.getText().trim();
        list.clear();
        query = "SELECT HNo,HBuild,HPark,HFloor,HRoom,HArea,HState,HType,HNote,House_Info.ONo,OName,OSex,OTel,OID,ONote FROM House_Info LEFT JOIN Owner_Info ON House_Info.ONo=Owner_Info.ONo WHERE HBuild LIKE \'" +
                search_HBuild + "%\' AND HPark LIKE \'" + search_HPark + "%\' AND HFloor LIKE \'" + search_HFloor + "%\' AND HRoom LIKE \'" + search_HRoom + "%\'";
        SQL_Connect sql_connect = new SQL_Connect();
        ResultSet HouseData = sql_connect.sql_Query(query);
        try {
            while (HouseData.next()){
                list.add(new HouseTableData(HouseData.getString("HNo").trim(),
                        HouseData.getString("HBuild").trim() + "幢",
                        HouseData.getString("HPark").trim() + "单元",
                        HouseData.getString("HFloor").trim() + "层",
                        HouseData.getString("HRoom").trim() + "室",
                        HouseData.getString("HArea").trim() + "㎡",
                        HouseData.getString("HState"),
                        HouseData.getString("HType").trim(),
                        HouseData.getString("HNote"),
                        HouseData.getString("ONo"),
                        HouseData.getString("OName"),
                        HouseData.getString("OSex"),
                        HouseData.getString("OTel"),
                        HouseData.getString("OID"),
                        HouseData.getString("ONote")));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.regex.Pattern;

public class Controller_CarMain {
    public Label LoginUser_Label;

    public TableView<SimpleStringProperty> PRegion_TableView;
    public TableColumn<SimpleStringProperty ,String> PRegion_TableColumn;
    ObservableList<SimpleStringProperty> PRegion_List = FXCollections.observableArrayList();

    /*车位信息    PNo车位编号    PRegion车位区域    PState车位状态    CarNo车牌号    PNote车位备注    ONo业主编号*/
    public TableView<Data_ParkingTable> Parking_TableView;
    public TableColumn<Data_ParkingTable ,String> PNo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> CarNo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> PState_TableColumn;
    public TableColumn<Data_ParkingTable ,String> ONo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> OName_TableColumn;
    ObservableList<Data_ParkingTable> ParkingTableView_List = FXCollections.observableArrayList();

    public TextField Search_PNo_TextField,Search_CarNo_TextField,Search_OName_TextField;

    public Label PRegion_Edit_Label,PNo_Edit_Label,CarNoError_Edit_Label;
    public ChoiceBox PState_Edit_ChoiceBox,OSex_Edit_ChoiceBox;
    public TextArea PNote_Edit_TextArea,ONote_Edit_TextArea;
    public TextField CarNo_Edit_TextField,ONo_Edit_TextField,OName_Edit_TextField,OTel_Edit_TextField,OID_Edit_TextField;
    public Button SearchOwner_Edit_Button,Edit_Button,Del_Button;

    public Label PRegion_New_Label,CarNoError_New_Label;
    public ChoiceBox PState_New_ChoiceBox,OSex_New_ChoiceBox;
    public TextArea PNote_New_TextArea,ONote_New_TextArea;
    public TextField PRegion_New_TextField,PNo_New_TextField,CarNo_New_TextField,ONo_New_TextField,OName_New_TextField,OTel_New_TextField,OID_New_TextField;
    public Button SearchOwner_New_Button,New_Button;

    String query,query_Update,query_Insert;
    ResultSet result;
    public Data_ParkingTable data_parkingTable;
    int check_Edit_CarNo,check_Edit_Search,check_New_CarNo,check_New_Search;

    public void initialize() {
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);
        //ChoiceBox加入选择项
        PState_Edit_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "已出租")
        );
        PState_New_ChoiceBox.setItems(FXCollections.observableArrayList(
                "已销售", "未销售", "已出租")
        );
        PState_New_ChoiceBox.setValue("未销售");
        OSex_Edit_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );
        OSex_New_ChoiceBox.setItems(FXCollections.observableArrayList(
                "男", "女")
        );

        OName_Edit_TextField.setDisable(true);
        OSex_Edit_ChoiceBox.setDisable(true);
        OTel_Edit_TextField.setDisable(true);
        OID_Edit_TextField.setDisable(true);
        ONote_Edit_TextArea.setDisable(true);
        OName_New_TextField.setDisable(true);
        OSex_New_ChoiceBox.setDisable(true);
        OTel_New_TextField.setDisable(true);
        OID_New_TextField.setDisable(true);
        ONote_New_TextArea.setDisable(true);

        PRegion_TableView.setItems(PRegion_List);
        PRegion_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue());
        //加载PRegion_TableView数据，此处为初始化，读取数据库中有多少区域并显示
        showPRegionTableView();
        //初始化House_TableView，读取数据库中所有数据
        showParkingTableView("初始化");
        //选择行监听
        PRegion_TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showParkingTableView(newValue.getValue()));

        Parking_TableView.setItems(ParkingTableView_List);
        PNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPNo());
        CarNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getCarNo());
        PState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getPState());
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo());
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName());
        //选择行监听
        Parking_TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMoreParkingData(newValue));

        //车位销售情况选择框变更监听
        PState_Edit_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
                changePState_Edit_ChoiceBox(newValue));
        PState_New_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
                changePState_New_ChoiceBox(newValue));

        //搜索文本栏变动监听
        Search_PNo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_PNo(newValue);
            }
        });
        Search_CarNo_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_CarNo(newValue);
            }
        });
        Search_OName_TextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_OName(newValue);
            }
        });

        //文本框文字修改监听
        CarNo_Edit_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_CarNoEditTextField();
            }
        });
        PRegion_New_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_PRegionNewTextField();
            }
        });
        CarNo_New_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_CarNoNewTextField();
            }
        });
    }
    public void showPRegionTableView(){
        //加载PRegion_TableView数据，此处为初始化，读取数据库中有多少区域并显示
        PRegion_List.add(new SimpleStringProperty("全部"));
        SQL_Connect sql_connect = new SQL_Connect();
        query = "SELECT PRegion FROM Parking_Info GROUP BY PRegion";
        result = sql_connect.sql_Query(query);
        try{
            while (result.next()) {
                PRegion_List.add(new SimpleStringProperty(result.getString("PRegion")+"区"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showParkingTableView(String PRegion){
        //加载Parking_TableView数据
        //清空原有数据
        ParkingTableView_List.clear();
        if(PRegion.equals("初始化") || PRegion.equals("全部")){
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo";
        }
        else {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo WHERE PRegion=\'" + PRegion.replace("区","") + "\'";
        }
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                ParkingTableView_List.add(new Data_ParkingTable(result.getString("PNo"),
                        result.getString("PRegion") + "区",
                        result.getString("PState"),
                        result.getString("CarNo"),
                        result.getString("PNote"),
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
    public void showMoreParkingData(Data_ParkingTable data_parkingTable){
        //在TableView中选择后，右侧显示车位详细信息
        //获取选择行，仅当>=0时才进行显示车位详细信息
        if(Parking_TableView.getSelectionModel().getSelectedIndex() >= 0 && data_parkingTable.getPState().get().equals("未销售")){
            PRegion_Edit_Label.setText(data_parkingTable.getPRegion().get());
            PNo_Edit_Label.setText(data_parkingTable.getPNo().get());
            PState_Edit_ChoiceBox.setValue(data_parkingTable.getPState().getValue().trim());
            PNote_Edit_TextArea.setText(data_parkingTable.getPNote().get());
            CarNo_Edit_TextField.setText("");
            ONo_Edit_TextField.setText("");
            OName_Edit_TextField.setText("");
            OSex_Edit_ChoiceBox.setValue("男");
            OTel_Edit_TextField.setText("");
            OID_Edit_TextField.setText("");
            ONote_Edit_TextArea.setText("");
            this.data_parkingTable = data_parkingTable;
            return;
        }
        if(Parking_TableView.getSelectionModel().getSelectedIndex() >= 0 && data_parkingTable.getPState().get().equals("未销售")==false){
            PRegion_Edit_Label.setText(data_parkingTable.getPRegion().get());
            PNo_Edit_Label.setText(data_parkingTable.getPNo().get());
            PState_Edit_ChoiceBox.setValue(data_parkingTable.getPState().getValue().trim());
            PNote_Edit_TextArea.setText(data_parkingTable.getPNote().get());
            CarNo_Edit_TextField.setText(data_parkingTable.getCarNo().get().trim());
            ONo_Edit_TextField.setText(data_parkingTable.getONo().get().trim());
            OName_Edit_TextField.setText(data_parkingTable.getOName().get().trim());
            OSex_Edit_ChoiceBox.setValue(data_parkingTable.getOSex().getValue().trim());
            OTel_Edit_TextField.setText(data_parkingTable.getOTel().get().trim());
            OID_Edit_TextField.setText(data_parkingTable.getOID().get().trim());
            ONote_Edit_TextArea.setText(data_parkingTable.getONote().get());
            this.data_parkingTable = data_parkingTable;
            return;
        }
    }
    public void search_PNo(String PNo){
        ParkingTableView_List.clear();
        if(PNo==null || PNo.length()==0) {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo";
        }
        else {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo WHERE PNo LIKE \'%" + PNo.trim() + "%\'";
        }
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                ParkingTableView_List.add(new Data_ParkingTable(result.getString("PNo"),
                        result.getString("PRegion") + "区",
                        result.getString("PState"),
                        result.getString("CarNo"),
                        result.getString("PNote"),
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
    public void search_OName(String OName){
        ParkingTableView_List.clear();
        if(OName==null || OName.length()==0) {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo";
        }
        else {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo WHERE OName LIKE \'%" + OName.trim() + "%\'";
        }
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                ParkingTableView_List.add(new Data_ParkingTable(result.getString("PNo"),
                        result.getString("PRegion") + "区",
                        result.getString("PState"),
                        result.getString("CarNo"),
                        result.getString("PNote"),
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
    public void search_CarNo(String CarNo){
        ParkingTableView_List.clear();
        if(CarNo==null || CarNo.length()==0) {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo";
        }
        else {
            query = "SELECT PNo,PRegion,PState,CarNo,PNote,Parking_Info.ONo,OName,OSex,OTel,OID,ONote FROM Parking_Info LEFT JOIN Owner_Info ON Parking_Info.ONo=Owner_Info.ONo WHERE CarNo LIKE \'%" + CarNo.trim() + "%\'";
        }
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                ParkingTableView_List.add(new Data_ParkingTable(result.getString("PNo"),
                        result.getString("PRegion") + "区",
                        result.getString("PState"),
                        result.getString("CarNo"),
                        result.getString("PNote"),
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
    public void changePState_Edit_ChoiceBox(Number newValue) {
        //修改框的车位销售情况选择框变更监听
        if (newValue.intValue() == 1) {
            //选择第2项"未销售"
            CarNo_Edit_TextField.setText("");
            ONo_Edit_TextField.setText("");
            OName_Edit_TextField.setText("");
            OSex_Edit_ChoiceBox.setValue("男");
            OTel_Edit_TextField.setText("");
            OID_Edit_TextField.setText("");
            ONote_Edit_TextArea.setText("");
            CarNo_Edit_TextField.setDisable(true);
            ONo_Edit_TextField.setDisable(true);
            SearchOwner_Edit_Button.setDisable(true);
            CarNoError_Edit_Label.setText("");
            check_Edit_CarNo = 1;
            check_Edit_Search = 1;
        }
        else {
            //选择其他选项
            CarNo_Edit_TextField.setDisable(false);
            ONo_Edit_TextField.setDisable(false);
            SearchOwner_Edit_Button.setDisable(false);
            check_New_CarNo = 0;
            check_New_Search = 0;
        }
    }
    public void changePState_New_ChoiceBox(Number newValue){
        //新建框的车位销售情况选择框变更监听
        if (newValue.intValue() == 1) {
            //选择第2项"未销售"
            CarNo_New_TextField.setText("");
            ONo_New_TextField.setText("");
            OName_New_TextField.setText("");
            OSex_New_ChoiceBox.setValue("男");
            OTel_New_TextField.setText("");
            OID_New_TextField.setText("");
            ONote_New_TextArea.setText("");
            CarNo_New_TextField.setDisable(true);
            ONo_New_TextField.setDisable(true);
            SearchOwner_New_Button.setDisable(true);
            CarNoError_New_Label.setText("");
            check_New_CarNo = 1;
            check_New_Search = 1;
        }
        else {
            //选择其他选项
            CarNo_New_TextField.setDisable(false);
            ONo_New_TextField.setDisable(false);
            SearchOwner_New_Button.setDisable(false);
            check_New_CarNo = 0;
            check_New_Search = 0;
        }
    }
    public void check_CarNoEditTextField(){
        //检查车牌号码
        String regex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        if(CarNo_Edit_TextField.getText() == null || CarNo_Edit_TextField.getText().length()==0){
            CarNoError_Edit_Label.setText("车牌号码不能为空");
            check_Edit_CarNo = 0;
            return;
        }
        if(Pattern.compile(regex).matcher(CarNo_Edit_TextField.getText()).matches()==false){
            CarNoError_Edit_Label.setText("请输入正确的车牌号码");
            check_Edit_CarNo = 0;
            return;
        }
        CarNoError_Edit_Label.setText("");
        check_Edit_CarNo = 1;
    }
    public void check_CarNoNewTextField(){
        //检查车牌号码
        String regex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        if(CarNo_New_TextField.getText() == null || CarNo_New_TextField.getText().length()==0){
            CarNoError_New_Label.setText("车牌号码不能为空");
            check_New_CarNo = 0;
            return;
        }
        if(Pattern.compile(regex).matcher(CarNo_New_TextField.getText()).matches()==false){
            CarNoError_New_Label.setText("请输入正确的车牌号码");
            check_New_CarNo = 0;
            return;
        }
        CarNoError_New_Label.setText("");
        check_New_CarNo = 1;
    }
    public void check_PRegionNewTextField(){
        //车位区域文本框赋值到车位编号中
        PRegion_New_Label.setText(PRegion_New_TextField.getText());
    }
    public void click_SearchOwnerEditButton(){
        if(ONo_Edit_TextField.getText() == null || ONo_Edit_TextField.getText().length()==0){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("小区物业管理系统");
            alert1.setHeaderText("请输入需要搜索的业主编号再进行搜索！");
            alert1.initOwner(ONote_Edit_TextArea.getScene().getWindow());
            alert1.showAndWait();
            return;
        }
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=\'" + ONo_Edit_TextField.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            if(result.next() == false){
                OName_Edit_TextField.setText("");
                OSex_Edit_ChoiceBox.setValue("男");
                OTel_Edit_TextField.setText("");
                OID_Edit_TextField.setText("");
                ONote_Edit_TextArea.setText("");
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("小区物业管理系统");
                alert1.setHeaderText("搜索不到编号为" + ONo_Edit_TextField.getText().trim() + "的业主信息！");
                alert1.initOwner(ONote_Edit_TextArea.getScene().getWindow());
                alert1.showAndWait();
                return;
            }
            else {
                check_Edit_Search = 1;
                OName_Edit_TextField.setText(result.getString("OName").trim());
                OSex_Edit_ChoiceBox.setValue(result.getString("OSex").trim());
                OTel_Edit_TextField.setText(result.getString("OTel").trim());
                OID_Edit_TextField.setText(result.getString("OID").trim());
                ONote_Edit_TextArea.setText(result.getString("ONote"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_SearchOwnerNewButton(){
        if(ONo_New_TextField.getText() == null || ONo_New_TextField.getText().length()==0){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("小区物业管理系统");
            alert1.setHeaderText("请输入需要搜索的业主编号再进行搜索！");
            alert1.initOwner(ONote_New_TextArea.getScene().getWindow());
            alert1.showAndWait();
            return;
        }
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=\'" + ONo_New_TextField.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            if (result.next() == false) {
                OName_New_TextField.setText("");
                OSex_New_ChoiceBox.setValue("男");
                OTel_New_TextField.setText("");
                OID_New_TextField.setText("");
                ONote_New_TextArea.setText("");
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("小区物业管理系统");
                alert1.setHeaderText("搜索不到编号为" + ONo_New_TextField.getText().trim() + "的业主信息！");
                alert1.initOwner(ONote_New_TextArea.getScene().getWindow());
                alert1.showAndWait();
                return;
            } else {
                check_New_Search = 1;
                OName_New_TextField.setText(result.getString("OName").trim());
                OSex_New_ChoiceBox.setValue(result.getString("OSex").trim());
                OTel_New_TextField.setText(result.getString("OTel").trim());
                OID_New_TextField.setText(result.getString("OID").trim());
                ONote_New_TextArea.setText(result.getString("ONote"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_EditButton(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否编辑该车位信息？");
        alert.initOwner(Edit_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if(PState_Edit_ChoiceBox.getSelectionModel().getSelectedItem().equals("未销售")){
            if(check_Edit_Search != 1){
                errorEdit_CarNo();
                return;
            }
            query_Update = "UPDATE Parking_Info SET " +
                    "PState=\'未销售\'," +
                    "PNote=\'" + PNote_Edit_TextArea.getText() + "\'," +
                    "CarNo=NULL," +
                    "ONo=NULL " +
                    "WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
            query = query_Update;
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            data_parkingTable.setPState("未销售");
            data_parkingTable.setPNote(PNote_Edit_TextArea.getText().trim());
            data_parkingTable.setCarNo("");
            data_parkingTable.setONo("");
            data_parkingTable.setOName("");
            data_parkingTable.setOSex("");
            data_parkingTable.setOTel("");
            data_parkingTable.setOID("");
            data_parkingTable.setONote("");
            succeedEdit();
        }
        else{
            if(check_Edit_CarNo != 1){
                errorEdit_CarNo();
                return;
            }
            if(check_Edit_Search != 1){
                errorEdit_Search();
                return;
            }
            query_Update = "UPDATE Parking_Info SET " +
                    "PState=\'" + PState_Edit_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                    "PNote=\'" + PNote_Edit_TextArea.getText() + "\'," +
                    "CarNo=\'" + CarNo_Edit_TextField.getText().trim() + "\'," +
                    "ONo=\'" + ONo_Edit_TextField.getText().trim() + "\' " +
                    "WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
            query = query_Update;
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            data_parkingTable.setPState(PState_Edit_ChoiceBox.getSelectionModel().getSelectedItem().toString());
            data_parkingTable.setPNote(PNote_Edit_TextArea.getText());
            data_parkingTable.setCarNo(CarNo_Edit_TextField.getText().trim());
            data_parkingTable.setONo(ONo_Edit_TextField.getText().trim());
            data_parkingTable.setOName(OName_Edit_TextField.getText().trim());
            data_parkingTable.setOSex(OSex_Edit_ChoiceBox.getSelectionModel().getSelectedItem().toString());
            data_parkingTable.setOTel(ONo_Edit_TextField.getText().trim());
            data_parkingTable.setOID(OID_Edit_TextField.getText().trim());
            data_parkingTable.setONote(ONote_Edit_TextArea.getText());
            succeedEdit();
        }
    }
    public void click_DelButton(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否删除该车位信息？");
        alert.initOwner(Del_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        query = "DELETE Parking_Info WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
        succeedDel();
        showParkingTableView("初始化");
    }
    public void click_NewButton(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您确认是否新增该车位信息？");
        alert.initOwner(New_Button.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if(PState_New_ChoiceBox.getSelectionModel().getSelectedItem().equals("未销售")){
            if(PRegion_New_TextField.getText() == null || PRegion_New_TextField.getText().length()==0){
                errorEdit_PRegion();
                return;
            }
            if(PNo_New_TextField.getText() == null || PNo_New_TextField.getText().length()==0){
                errorEdit_PNo();
                return;
            }
            if(check_New_Search != 1){
                errorEdit_CarNo();
                return;
            }
            query_Insert = "INSERT INTO Parking_Info VALUES" +
                    "(\'" + PRegion_New_TextField.getText().trim() + "-" + PNo_New_TextField.getText().trim() + "\',\'" + PRegion_New_TextField.getText().trim() + "\',\'" +
                    "未销售" + "\',NULL,\'" + PNote_New_TextArea.getText() + "\',NULL);";
            query = query_Insert;
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            ParkingTableView_List.add(new Data_ParkingTable(PRegion_New_TextField.getText().trim() + "-" + PNo_New_TextField.getText().trim(),
                    PRegion_New_TextField.getText().trim() + "区",
                    "未销售","",PNote_New_TextArea.getText(),"","","","","",""));
            succeedEdit();
        }
        else{
            if(check_New_CarNo != 1){
                errorEdit_CarNo();
                return;
            }
            if(check_New_Search != 1){
                errorEdit_Search();
                return;
            }
            query_Insert = "INSERT INTO Parking_Info VALUES" +
                    "(\'" + PRegion_New_TextField.getText().trim() + "-" + PNo_New_TextField.getText().trim() + "\',\'" + PRegion_New_TextField.getText().trim() + "\',\'" +
                    PState_New_ChoiceBox.getSelectionModel().getSelectedItem() + "\',\'" + CarNo_New_TextField.getText() + "\',\'" + PNote_New_TextArea.getText() + "\',\'" + ONo_New_TextField.getText() + "\');";
            query = query_Insert;
            SQL_Connect sql_connect = new SQL_Connect();
            sql_connect.sql_Update(query);
            ParkingTableView_List.add(new Data_ParkingTable(PRegion_New_TextField.getText().trim() + "-" + PNo_New_TextField.getText().trim(),
                    PRegion_New_TextField.getText().trim() + "区",
                    PState_New_ChoiceBox.getSelectionModel().getSelectedItem().toString(),
                    CarNo_New_TextField.getText(),
                    PNote_New_TextArea.getText(),
                    ONo_New_TextField.getText(),
                    OName_New_TextField.getText(),
                    OSex_New_ChoiceBox.getSelectionModel().getSelectedItem().toString(),
                    OTel_New_TextField.getText(),
                    OID_New_TextField.getText(),
                    ONote_New_TextArea.getText()));
            succeedEdit();
            showPRegionTableView();
        }
    }
    public void errorEdit_CarNo(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("输入的车牌号码数据检测异常！");
        alert2.initOwner(Edit_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void errorEdit_Search(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("请先单击搜索按钮获取业主信息！");
        alert2.initOwner(Edit_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void errorEdit_PRegion(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("请输入车位区域！");
        alert2.initOwner(Edit_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void errorEdit_PNo(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("请输入车位编号！");
        alert2.initOwner(Edit_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void succeedEdit(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息修改成功！");
        alert2.initOwner(Edit_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void succeedDel(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息删除成功！");
        alert2.initOwner(Del_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    public void click_IndexToggleButton(){
        //主界面-房屋管理界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("GUI_IndexMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-主界面");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

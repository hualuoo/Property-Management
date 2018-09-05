package controller;

import data.Data_ParkingTable;
import application.Main;
import util.SQL_Connect;

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

    public Label PRegion_Edit_Label,PNo_Edit_Label;
    public ChoiceBox PState_Edit_ChoiceBox,OSex_Edit_ChoiceBox;
    public TextArea PNote_Edit_TextArea,ONote_Edit_TextArea;
    public TextField CarNo_Edit_TextField,ONo_Edit_TextField,OName_Edit_TextField,OTel_Edit_TextField,OID_Edit_TextField;
    public Button SearchOwner_Edit_Button,Edit_Button,Del_Button;

    public Label PRegion_New_Label;
    public ChoiceBox PState_New_ChoiceBox,OSex_New_ChoiceBox;
    public TextArea PNote_New_TextArea,ONote_New_TextArea;
    public TextField PRegion_New_TextField,PNo_New_TextField,CarNo_New_TextField,ONo_New_TextField,OName_New_TextField,OTel_New_TextField,OID_New_TextField;
    public Button SearchOwner_New_Button,New_Button;

    String query;
    ResultSet result;
    int check_Edit_Search,check_New_Search;

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
        PRegion_New_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                check_PRegionNewTextField();
            }
        });
        ONo_Edit_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                change_ONoEditTextField();
            }
        });
        ONo_New_TextField.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                change_ONoNewTextField();
            }
        });
    }
    void showPRegionTableView(){
        //加载PRegion_TableView数据，此处为初始化，读取数据库中有多少区域并显示
        PRegion_List.add(new SimpleStringProperty("显示所有"));
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
    void showParkingTableView(String PRegion){
        //加载Parking_TableView数据
        //清空原有数据
        ParkingTableView_List.clear();
        if(PRegion.equals("初始化") || PRegion.equals("显示所有")){
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
    void showMoreParkingData(Data_ParkingTable data_parkingTable){
        //在TableView中选择后，右侧显示车位详细信息
        //获取选择行，仅当>=0时才进行显示车位详细信息
        if (Parking_TableView.getSelectionModel().getSelectedIndex() < 0){
            return;
        }
        PRegion_Edit_Label.setText(data_parkingTable.getPRegion().get());
        PNo_Edit_Label.setText(data_parkingTable.getPNo().get());
        PState_Edit_ChoiceBox.setValue(data_parkingTable.getPState().getValue().trim());
        if(data_parkingTable.getPNote().getValue()==null || data_parkingTable.getPNote().getValue().length() == 0)
            PNote_Edit_TextArea.setText("");
        else
            PNote_Edit_TextArea.setText(data_parkingTable.getPNote().get().trim());
        if(data_parkingTable.getPState().get().equals("未销售")){
            CarNo_Edit_TextField.setText("");
            ONo_Edit_TextField.setText("");
            OName_Edit_TextField.setText("");
            OSex_Edit_ChoiceBox.setValue("男");
            OTel_Edit_TextField.setText("");
            OID_Edit_TextField.setText("");
            ONote_Edit_TextArea.setText("");
            check_Edit_Search = 1;
            return;
        }
        if(data_parkingTable.getPState().get().equals("未销售")==false){
            CarNo_Edit_TextField.setText(data_parkingTable.getCarNo().get().trim());
            ONo_Edit_TextField.setText(data_parkingTable.getONo().get().trim());
            OName_Edit_TextField.setText(data_parkingTable.getOName().get().trim());
            OSex_Edit_ChoiceBox.setValue(data_parkingTable.getOSex().getValue().trim());
            OTel_Edit_TextField.setText(data_parkingTable.getOTel().get().trim());
            OID_Edit_TextField.setText(data_parkingTable.getOID().get().trim());
            if(data_parkingTable.getONote().getValue()==null || data_parkingTable.getONote().getValue().length() == 0)
                ONote_Edit_TextArea.setText("");
            else
                ONote_Edit_TextArea.setText(data_parkingTable.getONote().get().trim());
            check_Edit_Search = 1;
            return;
        }
    }
    void search_PNo(String PNo){
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
    void search_OName(String OName){
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
    void search_CarNo(String CarNo){
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
    void changePState_Edit_ChoiceBox(Number newValue) {
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
            check_Edit_Search = 1;
        }
        else {
            //选择其他选项
            CarNo_Edit_TextField.setDisable(false);
            ONo_Edit_TextField.setDisable(false);
            SearchOwner_Edit_Button.setDisable(false);
            check_Edit_Search = 0;
        }
    }
    void changePState_New_ChoiceBox(Number newValue){
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
            check_New_Search = 1;
        }
        else {
            //选择其他选项
            CarNo_New_TextField.setDisable(false);
            ONo_New_TextField.setDisable(false);
            SearchOwner_New_Button.setDisable(false);
            check_New_Search = 0;
        }
    }
    void change_ONoEditTextField(){
        //编辑框的业主编号文本框变动
        OName_Edit_TextField.setText("");
        OTel_Edit_TextField.setText("");
        OID_Edit_TextField.setText("");
        ONote_Edit_TextArea.setText("");
        check_Edit_Search = 0;
    }
    void change_ONoNewTextField(){
        //新建框的业主编号文本框变动
        OName_New_TextField.setText("");
        OTel_New_TextField.setText("");
        OID_New_TextField.setText("");
        ONote_New_TextArea.setText("");
        check_New_Search = 0;
    }
    public void check_PRegionNewTextField(){
        //车位区域文本框赋值到车位编号中
        PRegion_New_Label.setText(PRegion_New_TextField.getText());
    }
    public void click_SearchOwnerEditButton(){
        //单击"搜索"按钮
        if (ONo_Edit_TextField.getText() == null || ONo_Edit_TextField.getText().length()==0){
            //如果 业主编号-文本栏 为空，弹出未输入业主编号时的错误弹窗
            error_NullONo();
            return;
        }
        //数据库指令
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=" + ONo_Edit_TextField.getText().trim();
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            if(result.next() == false){
                //如果数据库查不到业主信息
                check_Edit_Search = 0;
                OName_Edit_TextField.setText("");
                OSex_Edit_ChoiceBox.setValue("男");
                OTel_Edit_TextField.setText("");
                OID_Edit_TextField.setText("");
                ONote_Edit_TextArea.setText("");
                error_NullEditOwener();
                return;
            }
            else {
                //如果数据库查到业主信息
                check_Edit_Search = 1;
                OName_Edit_TextField.setText(result.getString("OName"));
                OSex_Edit_ChoiceBox.setValue(result.getString("OSex").trim());
                OTel_Edit_TextField.setText(result.getString("OTel"));
                OID_Edit_TextField.setText(result.getString("OID"));
                ONote_Edit_TextArea.setText(result.getString("ONote"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void click_SearchOwnerNewButton(){
        //单击"搜索"按钮
        if(ONo_New_TextField.getText() == null || ONo_New_TextField.getText().length()==0){
            //如果 业主编号-文本栏 为空，弹出未输入业主编号时的错误弹窗
            error_NullONo();
            return;
        }
        //数据库指令
        query = "SELECT ONo,OName,OSex,OTel,OID,ONote FROM Owner_Info WHERE ONo=" + ONo_New_TextField.getText().trim();
        //调用SQL方法类获取ResultSet结果
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        //ResultSet的next方法需要try-catch输出报错
        try {
            if(result.next() == false){
                //如果数据库查不到业主信息
                check_New_Search = 0;
                OName_New_TextField.setText("");
                OSex_New_ChoiceBox.setValue("男");
                OTel_New_TextField.setText("");
                OID_New_TextField.setText("");
                ONote_New_TextArea.setText("");
                error_NullNewOwener();
                return;
            }
            else {
                //如果数据库查到业主信息
                check_New_Search = 1;
                OName_New_TextField.setText(result.getString("OName"));
                OSex_New_ChoiceBox.setValue(result.getString("OSex").trim());
                OTel_New_TextField.setText(result.getString("OTel"));
                OID_New_TextField.setText(result.getString("OID"));
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
            //车位"未销售"
            if(PNote_Edit_TextArea.getText() == null || PNote_Edit_TextArea.getText().length()==0){
                //修改数据库中车位数据(未销售，备注为空)
                editParkingInfoToSQL_NoSellNullPNote();
            }
            else {
                if (length(PNote_Edit_TextArea.getText()) > 100) {
                    //车位备注超出长度
                    error_LangPNote();
                    return;
                }
                //修改数据库中车位数据(未销售，备注不为空)
                editParkingInfoToSQL_NoSellFullPNote();
            }
            //刷新"车位管理"窗口的TableView
            flush_TableView();
            //修改成功的弹窗
            succeedEdit();
        }
        else{
            //车位"已销售"、"已出租"
            if (CarNo_Edit_TextField.getText()==null || CarNo_Edit_TextField.getText().length()==0){
                //如果未输入车牌号码
                error_NullCarNo();
                return;
            }
            String regex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
            if(Pattern.compile(regex).matcher(CarNo_Edit_TextField.getText()).matches()==false){
                //车牌号码输入错误
                error_WrongCarNo();
                return;
            }
            if(check_Edit_Search != 1){
                //未搜索业主信息
                errorEdit_Search();
                return;
            }
            if(PNote_Edit_TextArea.getText() == null || PNote_Edit_TextArea.getText().length()==0){
                //修改数据库中车位数据(已销售、已出租，备注为空)
                editParkingInfoToSQL_YesSellNullPNote();
            }
            else {
                if (length(PNote_Edit_TextArea.getText()) > 100) {
                    //车位备注超出长度
                    error_LangPNote();
                    return;
                }
                //修改数据库中车位数据(已销售、已出租，备注不为空)
                editParkingInfoToSQL_YesSellFullPNote();
            }
            //刷新"车位管理"窗口的TableView
            flush_TableView();
            //修改成功的弹窗
            succeedEdit();
        }
    }
    void editParkingInfoToSQL_NoSellNullPNote(){
        //修改数据库中车位数据(未销售、备注为空)
        query = "UPDATE Parking_Info SET " +
                "PState=\'未销售\'," +
                "PNote=NULL," +
                "CarNo=NULL," +
                "ONo=NULL " +
                "WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editParkingInfoToSQL_NoSellFullPNote(){
        //修改数据库中车位数据(未销售、备注不为空)
        query = "UPDATE Parking_Info SET " +
                "PState=\'未销售\'," +
                "PNote=\'" + PNote_Edit_TextArea.getText() + "\'," +
                "CarNo=NULL," +
                "ONo=NULL " +
                "WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editParkingInfoToSQL_YesSellNullPNote(){
        //修改数据库中车位数据(已销售、已出租，备注为空)
        query = "UPDATE Parking_Info SET " +
                "PState=\'" + PState_Edit_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "PNote=NULL," +
                "CarNo=\'" + CarNo_Edit_TextField.getText().trim() + "\'," +
                "ONo=" + ONo_Edit_TextField.getText().trim() + " " +
                "WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void editParkingInfoToSQL_YesSellFullPNote(){
        //修改数据库中车位数据(已销售、已出租，备注不为空)
        query = "UPDATE Parking_Info SET " +
                "PState=\'" + PState_Edit_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "PNote=\'" + PNote_Edit_TextArea.getText() + "\'," +
                "CarNo=\'" + CarNo_Edit_TextField.getText().trim() + "\'," +
                "ONo=" + ONo_Edit_TextField.getText().trim() + " " +
                "WHERE PNo=\'" + PNo_Edit_Label.getText().trim() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void flush_TableView(){
        //刷新"车位管理"窗口的TableView
        PRegion_List.clear();
        showPRegionTableView();
        ParkingTableView_List.clear();
        showParkingTableView("初始化");
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
        Optional<ButtonType> alertresult = alert.showAndWait();
        if (alertresult.get() != ButtonType.OK) {
            return;
        }
        if (PRegion_New_TextField.getText()==null || PRegion_New_TextField.getText().length()==0){
            //如果未输入车位区域
            error_NullPRegion();
            return;
        }
        String regex = "[A-Z]*";
        if (Pattern.compile(regex).matcher(PRegion_New_TextField.getText()).matches() == false){
            //车位区域输入错误
            error_WrongPRegion();
            return;
        }
        if (length(PRegion_New_TextField.getText()) > 2) {
            //车位区域超出长度
            error_LangPRegion();
            return;
        }
        if (PNo_New_TextField.getText()==null || PNo_New_TextField.getText().length()==0){
            //如果未输入车位编号后半部分
            error_NullPNo();
            return;
        }
        regex = "[0-9]*";
        if (Pattern.compile(regex).matcher(PNo_New_TextField.getText()).matches() == false){
            //车位编号后半部分输入错误
            error_WrongPNo();
            return;
        }
        if (length(PNo_New_TextField.getText()) > 4) {
            //车位编号后半部分超出长度
            error_LangPNo();
            return;
        }
        if (PState_New_ChoiceBox.getSelectionModel().getSelectedItem()==null || PState_New_ChoiceBox.getSelectionModel().getSelectedItem().toString().length()==0){
            //如果未选择车位状态
            error_NullPState();
            return;
        }
        query = "SELECT * FROM Parking_Info WHERE PNo=\'" + PRegion_New_TextField.getText() + "-" + PNo_New_TextField.getText() + "\'";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            if (result.next() == true) {
                //如果数据库已有该车位编号
                error_HavePNo();
                return;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(PState_New_ChoiceBox.getSelectionModel().getSelectedItem().equals("未销售")){
            //车位"未销售"
            if(PNote_New_TextArea.getText() == null || PNote_New_TextArea.getText().length()==0){
                //新增数据库中车位数据(未销售，备注为空)
                newParkingInfoToSQL_NoSellNullPNote();
            }
            else {
                if (length(PNote_New_TextArea.getText()) > 100) {
                    //车位备注超出长度
                    error_LangPNote();
                    return;
                }
                //新增数据库中车位数据(未销售，备注不为空)
                newParkingInfoToSQL_NoSellFullPNote();
            }
            //刷新"车位管理"窗口的TableView
            flush_TableView();
            //新增成功的弹窗
            succeedNew();
        }
        else{
            //车位"已销售"、"已出租"
            if (CarNo_New_TextField.getText()==null || CarNo_New_TextField.getText().length()==0){
                //如果未输入车牌号码
                error_NullCarNo();
                return;
            }
            regex = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
            if(Pattern.compile(regex).matcher(CarNo_New_TextField.getText()).matches()==false){
                //车牌号码输入错误
                error_WrongCarNo();
                return;
            }
            if(check_New_Search != 1){
                //未搜索业主信息
                errorEdit_Search();
                return;
            }
            if(PNote_New_TextArea.getText() == null || PNote_New_TextArea.getText().length()==0){
                //新增数据库中车位数据(已销售、已出租，备注为空)
                newParkingInfoToSQL_YesSellNullPNote();
            }
            else {
                if (length(PNote_New_TextArea.getText()) > 100) {
                    //车位备注超出长度
                    error_LangPNote();
                    return;
                }
                //新增数据库中车位数据(已销售、已出租，备注不为空)
                newParkingInfoToSQL_YesSellFullPNote();
            }
            //刷新"车位管理"窗口的TableView
            flush_TableView();
            //修改成功的弹窗
            succeedNew();
        }
    }
    void newParkingInfoToSQL_NoSellNullPNote(){
        //新增数据库中车位数据(未销售，备注为空)
        query = "INSERT INTO Parking_Info VALUES (" +
                "\'" + PRegion_New_TextField.getText() + "-" + PNo_New_TextField.getText() + "\'," +
                "\'" + PRegion_New_TextField.getText() + "\'," +
                "\'未销售\'," +
                "NULL," +
                "NULL," +
                "NULL);";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void newParkingInfoToSQL_NoSellFullPNote(){
        //新增数据库中车位数据(未销售，备注不为空)
        query = "INSERT INTO Parking_Info VALUES (" +
                "\'" + PRegion_New_TextField.getText() + "-" + PNo_New_TextField.getText() + "\'," +
                "\'" + PRegion_New_TextField.getText() + "\'," +
                "\'未销售\'," +
                "NULL," +
                "\'" + PNote_New_TextArea.getText() + "\'," +
                "NULL);";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void newParkingInfoToSQL_YesSellNullPNote(){
        //新增数据库中车位数据(已销售、已出租，备注为空)
        query = "INSERT INTO Parking_Info VALUES (" +
                "\'" + PRegion_New_TextField.getText() + "-" + PNo_New_TextField.getText() + "\'," +
                "\'" + PRegion_New_TextField.getText() + "\'," +
                "\'" + PState_New_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "\'" + CarNo_New_TextField.getText() + "\'," +
                "NULL," +
                ONo_New_TextField.getText() + ");";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void newParkingInfoToSQL_YesSellFullPNote(){
        //新增数据库中车位数据(已销售、已出租，备注不为空)
        query = "INSERT INTO Parking_Info VALUES (" +
                "\'" + PRegion_New_TextField.getText() + "-" + PNo_New_TextField.getText() + "\'," +
                "\'" + PRegion_New_TextField.getText() + "\'," +
                "\'" + PState_New_ChoiceBox.getSelectionModel().getSelectedItem() + "\'," +
                "\'" + CarNo_New_TextField.getText() + "\'," +
                "\'" + PNote_New_TextArea.getText() + "\'," +
                ONo_New_TextField.getText() + ");";
        SQL_Connect sql_connect = new SQL_Connect();
        sql_connect.sql_Update(query);
    }
    void error_NullPRegion(){
        //未输入车位区域时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位区域不能为空，请输入车位区域！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangPRegion(){
        //车位区域超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位区域超出长度，仅能输入1个字符(每个汉字占2个字符)！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullPNo(){
        //未输入车位编号后半部分时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位编号后半部分不能为空，请输入编号！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongPRegion(){
        //车位区域输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位区域仅能为大写字母，请输入正确的区域！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongPNo(){
        //车位编号后半部分输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位编号后半部分仅能为数字，请输入正确的编号！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_LangPNo(){
        //车位编号后半部分超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位编号后半部分超出长度，仅能输入4个字符(每个汉字占2个字符)！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullPState(){
        //未选择车位状态时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("您未选择车位状态，请选择状态！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_HavePNo(){
        //数据库中已有该编号车位信息
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("数据库中已有编号为" + PRegion_New_TextField.getText() + "-" + PNo_New_TextField.getText() + "的车位信息！");
        alert.initOwner(New_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullONo(){
        //未输入业主编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("请输入需要搜索的业主编号再进行搜索！");
        alert.initOwner(SearchOwner_Edit_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullEditOwener(){
        //未查到业主编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("搜索不到编号为" + ONo_Edit_TextField.getText().trim() + "的业主信息！");
        alert.initOwner(SearchOwner_Edit_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullNewOwener(){
        //未查到业主编号时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("搜索不到编号为" + ONo_New_TextField.getText().trim() + "的业主信息！");
        alert.initOwner(SearchOwner_Edit_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void errorEdit_Search(){
        Alert alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("请先单击搜索按钮获取业主信息！");
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
    public void succeedNew(){
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("小区物业管理系统");
        alert2.setHeaderText("信息新增成功！");
        alert2.initOwner(New_Button.getScene().getWindow());
        alert2.showAndWait();
    }
    void error_LangPNote(){
        //车位备注超出长度时的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车位备注超出长度，仅能输入100个字符(每个汉字占2个字符)！");
        alert.initOwner(Edit_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_NullCarNo(){
        //未输入车牌号码的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("车牌号码不能为空，请输入车牌号码！");
        alert.initOwner(Edit_Button.getScene().getWindow());
        alert.showAndWait();
    }
    void error_WrongCarNo(){
        //车牌号码输入错误的错误弹窗
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("小区物业管理系统");
        alert.setHeaderText("检测到车牌号码输入错误，请输入正确的车牌号码！");
        alert.initOwner(Edit_Button.getScene().getWindow());
        alert.showAndWait();
    }
    public void click_IndexToggleButton(){
        //主界面-房屋管理 界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_IndexMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统 - 房屋管理");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

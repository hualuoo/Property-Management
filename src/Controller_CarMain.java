import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.ResultSet;

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

    String query;
    ResultSet result;

    public void initialize() {
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);

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

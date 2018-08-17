import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;

import java.sql.ResultSet;

public class Controller_CarMain {
    public Label LoginUser_Label;

    public TableView<SimpleStringProperty> PRegion_TableView;
    public TableColumn<SimpleStringProperty ,String> PRegion_TableColumn;
    ObservableList<SimpleStringProperty> PRegion_List = FXCollections.observableArrayList();

    /*车位信息    PNo车位编号    PRegion车位区域    PState车位状态    CarNo车牌号    ONo业主编号*/
    public TableView<Data_ParkingTable> Parking_TableView;
    public TableColumn<Data_ParkingTable ,String> PNo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> CarNo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> PState_TableColumn;
    public TableColumn<Data_ParkingTable ,String> ONo_TableColumn;
    public TableColumn<Data_ParkingTable ,String> OName_TableColumn;
    ObservableList<Data_ParkingTable> ParkingTableView_List = FXCollections.observableArrayList();

    String query;
    ResultSet result;

    public void initialize() {
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);

        PRegion_TableView.setItems(PRegion_List);
        PRegion_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue());

        showPRegionTableView();

        //选择行监听
        PRegion_TableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showParkingTableView(newValue.getValue()));


    }
    public void showPRegionTableView(){
        //加载PRegion_TableView数据，此处为初始化，读取数据库中有多少区域并显示
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

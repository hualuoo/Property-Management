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

public class Controller_RepairMain {
    public Label LoginUser_Label;

    /*维修信息    RNo编号    RSubDate提交日期    RTitle标题    RText正文    RState状态    RReply回复    RSolveDate解决日期    ONo业主编号*/
    public TableView<Data_RepairTable> Repair_TableView;
    public TableColumn<Data_RepairTable ,String> RNo_TableColumn;
    public TableColumn<Data_RepairTable ,String> RSubDate_TableColumn;
    public TableColumn<Data_RepairTable ,String> RTitle_TableColumn;
    public TableColumn<Data_RepairTable ,String> RState_TableColumn;
    public TableColumn<Data_RepairTable ,String> ONo_TableColumn;
    public TableColumn<Data_RepairTable ,String> OName_TableColumn;
    ObservableList<Data_RepairTable> RepairTableView_List = FXCollections.observableArrayList();

    String query;
    ResultSet result;

    public void initialize() {
        //初始化
        //显示操作员用户名
        LoginUser_Label.setText("操作员：" + Main.loginUser);

        Repair_TableView.setItems(RepairTableView_List);
        RNo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRNo());
        RSubDate_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRSubDate());
        RTitle_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRTitle());
        RState_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getRState());
        ONo_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getONo());
        OName_TableColumn.setCellValueFactory(
                cellData -> cellData.getValue().getOName());

        showRepairTableView();
    }
    public void showRepairTableView(){
        query = "SELECT RNo,RSubDate,RTitle,RText,RState,RReply,RSolveDate,Repair_Info.ONo,OName,OSex,OTel,OID,ONote FROM Repair_Info LEFT JOIN Owner_Info ON Repair_Info.ONo=Owner_Info.ONo";
        SQL_Connect sql_connect = new SQL_Connect();
        result = sql_connect.sql_Query(query);
        try {
            while (result.next()){
                RepairTableView_List.add(new Data_RepairTable(result.getString("RNo"),
                        result.getString("RSubDate"),
                        result.getString("RTitle"),
                        result.getString("RText"),
                        result.getString("RState"),
                        result.getString("RReply"),
                        result.getString("RSolveDate"),
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
    public void click_CarToggleButton(){
        //车辆管理界面切换
        try {
            Parent Car_Root = FXMLLoader.load(getClass().getResource("GUI_CarMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-车辆管理界面");
            Main.Login_Stage.setScene(new Scene(Car_Root, 1000, 615));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
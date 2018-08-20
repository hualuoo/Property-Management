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
    public void initialize() {
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
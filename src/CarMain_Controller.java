import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class CarMain_Controller {
    public Label LoginUser_Label;
    public void initialize() {
        LoginUser_Label.setText("操作员：" + Main.loginUser);
    }
    public void index_ToggleButton_Click(){
        //主界面-房屋管理界面切换
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("IndexMain_GUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-主界面");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
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
}

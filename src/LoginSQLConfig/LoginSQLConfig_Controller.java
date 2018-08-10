import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
public class LoginSQLConfig_Controller {
    public TextField SQLIP_TextField,SQLUsername_TextField;
    public PasswordField SQLPassword_PasswordField;
    public Button EditSQL_Button,Back_Button;
    public int checksql;
    public void editSQL_ButtonClick(){
        Main.url = "jdbc:sqlserver://" + SQLIP_TextField.getText() + ":1433;DatabaseName=Sql_Curriculum_Design";
        Main.userName = SQLUsername_TextField.getText();
        Main.userPassword = SQLPassword_PasswordField.getText();
        SQL_Connect sql_connect = new SQL_Connect();
        checksql = sql_connect.getConnection();
        if (checksql == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("数据库连接测试成功！");
            alert.initOwner(EditSQL_Button.getScene().getWindow());
            alert.showAndWait();
            back_Login();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("数据库连接测试失败！");
            alert.initOwner(EditSQL_Button.getScene().getWindow());
            alert.showAndWait();
        }
    }
    public void back_Login(){
        try {
            Parent Login_Root = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-登录");
            //Main.Login_Stage.getIcons().add(new Image("/image/logo.png"));
            Main.Login_Stage.setScene(new Scene(Login_Root, 300, 250));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
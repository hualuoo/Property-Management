import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;

public class Controller_LoginSQLConfig {
    public TextField ServerIP_TextField,ServerPort_TextField,SQLTableName_TextField,SQLUserName_TextField;
    public PasswordField SQLPassword_PasswordField;
    public Button Test_Button,Confirm_Button;
    public int checksql;
    public void initialize() {
        //初始化，读取数据库信息
        ServerIP_TextField.setText(Main.serverIP);
        ServerPort_TextField.setText(Main.serverPort);
        SQLTableName_TextField.setText(Main.SQLTableName);
        SQLUserName_TextField.setText(Main.SQLUserName);
        SQLPassword_PasswordField.setText(Main.SQLPassword);
        //"确认"按钮不可用，需要先通过测试才能继续
        Confirm_Button.setDisable(true);
    }
    public void test_ButtonClick(){
        //单击"测试"按钮
        SQL_Connect sql_connect = new SQL_Connect();
        checksql = sql_connect.getConnection(ServerIP_TextField.getText().trim(),ServerPort_TextField.getText().trim(),SQLTableName_TextField.getText().trim(),SQLUserName_TextField.getText().trim(),SQLPassword_PasswordField.getText().trim());
        if (checksql == 1) {
            //如果连接成功
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("数据库连接测试成功！");
            alert.initOwner(Test_Button.getScene().getWindow());
            alert.showAndWait();
            Confirm_Button.setDisable(false);
        }
        else {
            //如果连接失败
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("小区物业管理系统");
            alert.setHeaderText("数据库连接测试失败！");
            alert.initOwner(Test_Button.getScene().getWindow());
            alert.showAndWait();
        }
    }
    public void confirm_ButtonClick(){
        //单击"确认"按钮，对原有数据库服务器信息进行修改并返回登录主界面
        Main.serverIP = ServerIP_TextField.getText().trim();
        Main.serverPort = ServerPort_TextField.getText().trim();
        Main.SQLTableName = SQLTableName_TextField.getText().trim();
        Main.SQLUserName = SQLUserName_TextField.getText().trim();
        Main.SQLPassword = SQLPassword_PasswordField.getText().trim();
        back_Login();
    }
    public void back_ButtonClick(){
        //单击"返回"按钮，对原有数据库服务器信息不进行修改直接返回登录主界面
        back_Login();
    }
    void back_Login(){
        //返回登录主窗口
        try {
            Parent Login_Root = FXMLLoader.load(getClass().getResource("GUI_LoginMain.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-登录");
            Main.Login_Stage.setScene(new Scene(Login_Root, 300, 250));
            Main.Login_Stage.getIcons().setAll(new Image("/image/logo.png"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
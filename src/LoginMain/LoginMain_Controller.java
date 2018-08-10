import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.security.MessageDigest;

public class LoginMain_Controller {
    public Button Login_Button;
    public Label login_info,SQL_Info;
    public TextField User_TextField;
    public PasswordField Password_PasswordField;
    public int checksql, checkloin;
    public void initialize() {
        //初始化
        check_SQL();
        User_TextField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            //用户名输入框的回车键盘监听
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    login_ButtonClick();
                }
            }
        });
        Password_PasswordField.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            //密码框的回车键盘监听
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    login_ButtonClick();
                }
            }
        });
    }
    public void check_SQL(){
        //数据库测试
        SQL_Connect sql_connect = new SQL_Connect();
        checksql = sql_connect.getConnection(Main.serverIP,Main.serverPort,Main.SQLTableName,Main.SQLUserName,Main.SQLPassword);
        if (checksql == 0) {
            SQL_Info.setText("数据库连接失败");
            SQL_Info.setTextFill(Color.web("#ff1a00"));
            Login_Button.setDisable(true);
        }
        else {
            SQL_Info.setText("数据库连接成功");
            SQL_Info.setTextFill(Color.web("#2E8B57"));
        }
    }
    public void edit_LabelClick(){
        //单击配置数据库服务器
        try {
            Parent SQLEdit_Root = FXMLLoader.load(getClass().getResource("/LoginSQLConfig/LoginSQLConfig_GUI.fxml"));
            Main.Login_Stage.setTitle("数据库配置");
            Main.Login_Stage.setScene(new Scene(SQLEdit_Root, 300, 250));
            Main.Login_Stage.getIcons().setAll(new Image("/image/edit_logo.png"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void login_ButtonClick() {
        //单击"登录"按钮
        SQL_Connect sql_connect = new SQL_Connect();
        if (User_TextField.getText() == null || User_TextField.getText().length() == 0) {
            login_info.setText("请输入用户名");
            login_info.setTextFill(Color.web("#ff1a00"));
        } else if (Password_PasswordField.getText() == null || Password_PasswordField.getText().length() == 0) {
            login_info.setText("请输入密码");
            login_info.setTextFill(Color.web("#ff1a00"));
        } else {
            checkloin = sql_connect.login(User_TextField.getText().toLowerCase(), stringToMD5(Password_PasswordField.getText()));
            switch (checkloin) {
                case 1:
                    login_info.setText("恭喜，登录成功。");
                    login_info.setTextFill(Color.web("#2E8B57"));
                    Main.loginUser = User_TextField.getText().toLowerCase();
                    Load_IndexGUI();
                    break;
                case 2:
                    login_info.setText("抱歉，密码错误。");
                    login_info.setTextFill(Color.web("#ff1a00"));
                    break;
                case 3:
                    login_info.setText("未查询到该用户名");
                    login_info.setTextFill(Color.web("#ff1a00"));
                    break;
                case 0:
                    login_info.setText("数据库出现异常。");
                    login_info.setTextFill(Color.web("#ff1a00"));
                    break;
            }
        }
    }
    void Load_IndexGUI() {
        //加载主界面
        try {
            Parent Index_Root = FXMLLoader.load(getClass().getResource("/IndexMain/IndexMain_GUI.fxml"));
            Main.Login_Stage.setTitle("小区物业管理系统-主界面");
            Main.Login_Stage.setScene(new Scene(Index_Root, 1000, 615));
            Main.Login_Stage.setX((Main.width-1000)/2);
            Main.Login_Stage.setY((Main.height-615)/2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String stringToMD5(String inStr){
        //密码MD5加密
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
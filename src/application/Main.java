package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;

public class Main extends Application {
    public static Stage Login_Stage;
    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static String serverIP = "sh1.hualuoo.com";
    public static String serverPort = "1433";
    public static String SQLTableName = "Sql_Curriculum_Design";
    public static String SQLUserName = "sa";
    public static String SQLPassword = "Qq111111";
    public static String loginUser;
    public void start(Stage primaryStage) throws Exception{
        Parent Login_Root = FXMLLoader.load(getClass().getResource("/GUI/GUI_LoginMain.fxml"));
        Login_Stage = new Stage();
        Login_Stage.setTitle("小区物业管理系统-登录");
        Login_Stage.getIcons().add(new Image("/image/logo.png"));
        Login_Stage.setScene(new Scene(Login_Root, 290, 240));
        Login_Stage.show();
        Login_Stage.setResizable(false);
        Login_Stage.setX((width-290)/2);
        Login_Stage.setY((height-240)/2);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
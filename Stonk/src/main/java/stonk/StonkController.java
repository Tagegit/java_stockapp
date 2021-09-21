package stonk;

import java.io.IOException;
// import javafx.fxml.FXML; uvisst om vi bruker denne 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage; 
import javafx.scene.Node;



public class StonkController {

    private Stage stage; 
    private Scene scene; 
    private Parent parent;

    public void fromLoginToRegister(ActionEvent event) throws IOException{
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("newUser.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();


    }
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    public void isLoginValid(ActionEvent event) throws IOException {
        DataHandler dataHandler = new DataHandler();
        try {
            User user = dataHandler.isLoginValid(username.getText(), password.getText());
            if(user.equals(null)){
                throw new IllegalArgumentException("Password is incorrect");
            }
            else {
                fromLoginToMain(event);
            }
        }
        catch(IllegalArgumentException e){
            System.out.println(e);
        } 
    }


    public void fromLoginToMain(ActionEvent event) throws IOException{
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();


    }

    public void fromRegisterToMain(ActionEvent event) throws IOException{
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();
    }




}

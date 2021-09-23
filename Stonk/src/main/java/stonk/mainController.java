package stonk;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class mainController implements Initializable{
    DataHandler handler = new DataHandler();
    private User user;
    
    @FXML
    private Label cashMoneyFlow; 
    
    @FXML
    private Label fullName; 

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField age;
    @FXML
    private TextField cash;
    @FXML
    private TextField searchbar;
    @FXML
    private Text balanceString; 


    @FXML
    public void displayCash(){
        cashMoneyFlow.setText(Float.toString(user.getCash()));
    }

    public void setUser(User user){
        this.user = user; 
    }
    
    public void updateMain(){
        DataHandler dataHandler = new DataHandler();
        user = dataHandler.isLoginValid(username.getText(), password.getText());
        System.out.println("hei");
        System.out.println(user.getFirstName());
        System.out.println(user.getCash());

        fullName.setText(" " + user.getFirstName());
        cash.setText(Float.toString(user.getCash()));

    }


    public static void main(String[] args) {
        mainController controller = new mainController();
        controller.displayCash();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Parent s = balanceString.getScene().getRoot();
        user = handler.generateUser(s.getId());
        System.out.println(user.getUserName()); 
        
    }




    

}

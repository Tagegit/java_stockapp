package ui;

import core.Stonk;
import core.User;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Controller for main.
 */
public class MainController {
  private User user;
  // Stonk stock = new Stonk(); Bruker ikke ifølge spotbugs
  
  @FXML
  private GridPane gridpane;
  @FXML
  private Label cashMoneyFlow; // Added private (wasnt anything before)
  @FXML
  private Label equity;
  @FXML
  private Label fullName;
  @FXML
  private Label growth;
  @FXML
  private Label growthPercent;
  @FXML
  private Text balanceString;
  @FXML
  private TextField age;
  @FXML
  private TextField cash;
  @FXML
  private TextField firstname;
  @FXML
  private TextField lastname;
  @FXML
  private TextField password;
  @FXML
  private TextField searchBar;
  @FXML
  private TextField username;
  @FXML
  private VBox scrollPane;
  @FXML
  private Button watchList;

  private Float ecuityChange = (float) 0;
  private Float stockPriceChanged = (float) 0.0;
  private Float growthPerStock = (float) 0.0;
  private String stockOnWeb;
  // public Float difference = (float) 0; - spotbugs - unused

  /**
   * Gets decimalform.
   * @param number of the given number.
   * @return in decimal format.
   */
  public String decimalform(Float number) {
    DecimalFormat df = new DecimalFormat();
    df.setMaximumFractionDigits(2);
    return df.format(number);
  }

  /**
   * Displays your potifolio on main im fxml.
   */
  public void displayOnMain() {
    user.removeMoney(ecuityChange); // to show correct current balance
    displayPortfolio();
    // Float difference = (ecuityChange - user.getCash());
    cashMoneyFlow.setText(Float.toString(user.getCash()) + "$");
    cashMoneyFlow.setStyle("-fx-text-fill: white;");
    fullName.setText((user.getFirstName()) + " " + (user.getLastName()));
    equity.setText((decimalform(user.getCash() + stockPriceChanged + ecuityChange)) + "$");
    System.out.println(stockPriceChanged);
    growthPercent();
  }

  /**
   * Growth-precent to show.
   */
  public void growthPercent() {
    growth.setText(decimalform(stockPriceChanged) + "$");
    if (cashEarnedPercent() > 0) {
      growthPercent.setText("+" + decimalform(cashEarnedPercent()) + "%");
    } else {
      growthPercent.setText(decimalform(cashEarnedPercent()) + "%");
    }
    if (stockPriceChanged > 1000 || stockPriceChanged < -1000) {
      growthPercent.setLayoutX(190);
    }
  }

  /**
   * Percentage-calculator with colorchange for negative og positive.
   *
   * @return how much percent one has earned.
   */
  public float cashEarnedPercent() {
    Float totalEq = ecuityChange + user.getCash();
    if (stockPriceChanged == 0) {
      growthPercent.setStyle("-fx-text-fill: black;");
      return 0.0f;
    } else if (stockPriceChanged < 0) {
      growthPercent.setStyle("-fx-text-fill: #cc021d;");
      return (stockPriceChanged / totalEq) * 100;
    }
    return ((stockPriceChanged / totalEq) * 100);
  }

  /**
   * Navigates to stockpage.
   */
  public void toStockPage() {
    StonkApp app = new StonkApp();
    try {
      StockPageController.stock.getStockInfo(searchBar.getText().toLowerCase());
      app.changeScene("stockPage.fxml");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Cannot find stock.");
    }
  }

  /**
   * Opens the marketwatch.com website with more information about the stock clicked on.
   */
  public void openBrowser() {
    Desktop d = Desktop.getDesktop();
    try {
      d.browse(new URI("https://www.marketwatch.com/investing/stock/" + stockOnWeb));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Displays portfoilo on the main page.
   */
  public void displayPortfolio() {
    scrollPane.getChildren().clear();
    watchList.setStyle("-fx-graphic: url('https://img.icons8.com/ios/25/000000/star--v2.png')");

    ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
    JSONArray json = user.getPortfolio();
    System.out.println(json);

    for (Object i : json) {
      JSONObject tempObj = (JSONObject) i;
      ArrayList<String> tempArr = new ArrayList<>();
      tempArr.add(String.valueOf(tempObj.get("ticker")));
      tempArr.add(String.valueOf(tempObj.get("price")));
      tempArr.add(String.valueOf(tempObj.get("count")));
      // LEGG TIL CURRENTS PRICE OG NÅVERENDE VERDI
      arr.add(tempArr);
    }
    System.out.println(arr);

    if (arr.size() == 0) {
      // "NO STOCKS IN PORTFOLIO"
    } else {
      for (ArrayList<String> row : arr) {

        Stonk s = new Stonk();
        s.getStockInfo(row.get(0));
        System.out.println((row.get(0)));
        // to get how much you have eanred from Stocks
        ecuityChange += (s.getPrice()) * Float.parseFloat(row.get(2));

        growthPerStock = ((s.getPrice()) * Float.parseFloat(row.get(2))
        - Float.parseFloat(row.get(1)) * Float.parseFloat(row.get(2)));
        stockPriceChanged += growthPerStock;

        // Adds info
        Label l = new Label("____________________________\n" 
            + row.get(0).toUpperCase() + "\nAmount: " + row.get(2)
            + "\nAverage: " + String.format("%.2f", Float.parseFloat(row.get(1)))+ " $" + "\nCurrent: " + s.getPrice() +" $" + "\nGrowth: " + String.format("%.2f", growthPerStock) + " $");
        Button b = new Button("Sell");
        Button more = new Button("more info");

        
        // Style of elements


        l.setStyle("-fx-font-size: 15;");
        b.maxHeight(l.getHeight());
        more.maxHeight(l.getHeight());
        more.setStyle("-fx-background-color: #090a0c;" 
            + "-fx-text-fill: linear-gradient(white, #d0d0d0); -fx-padding:10px;");
        b.setStyle("-fx-background-color: #090a0c;"
            + " -fx-text-fill: linear-gradient(white, #d0d0d0);  -fx-padding:10px;");
        VBox h = new VBox(l);
        h.setPadding(new Insets(10, 10, 10, 10));
        h.setStyle("-fx-background-color: #dbdbdb");
        HBox hbox = new HBox(b, more);
        hbox.setSpacing(15);
        hbox.setMargin(b, new Insets(0, 0, 0, 70));
        hbox.setStyle("-fx-background-color: #dbdbdb; -fx-margin: auto");

        b.setOnMouseClicked(event -> {
          searchBar.setText(row.get(0));
          toStockPage();
        });

        more.setOnMouseClicked(event -> {
          stockOnWeb = row.get(0);
          openBrowser();
        });

        scrollPane.getChildren().addAll(h);
        scrollPane.getChildren().addAll(hbox);
      }
    }
  }
  
  /**
   * Displays watchList on the main page when clickking on the star.
   */
  public void showWatchList(){
    scrollPane.getChildren().clear(); // remove the portifolio.
    watchList.setStyle("-fx-graphic: url('https://img.icons8.com/fluency/25/000000/star.png')"); // change star color in the button
      ArrayList<ArrayList<String>> arrWatch = new ArrayList<ArrayList<String>>();
      JSONArray json = user.getWatchList();
      for (Object i : json) {
        JSONObject tempObj = (JSONObject) i;
        ArrayList<String> tempArrWatch = new ArrayList<>();
        tempArrWatch.add(String.valueOf(tempObj.get("ticker")));
        tempArrWatch.add(String.valueOf(tempObj.get("price")));
        tempArrWatch.add(String.valueOf(tempObj.get("count")));
        // LEGG TIL CURRENTS PRICE OG NÅVERENDE VERDI
        arrWatch.add(tempArrWatch);
      }
  
      if (arrWatch.size() == 0) {
        // "NO STOCKS IN watchlist"
      } else {
        for (ArrayList<String> rowWatch : arrWatch) {
          Stonk s = new Stonk();
          s.getStockInfo(rowWatch.get(0));
          float percentChange =100 -(Float.parseFloat(rowWatch.get(1))/((s.getPrice())))*100;   // show difference in stock price from now and when it was added to watchList.
          
// Adds info
        Label l = new Label("____________________________\n" 
        + rowWatch.get(0).toUpperCase()+ "\nPrice when added: " + rowWatch.get(1)+ "\nPrice now: " + s.getPrice() +"\nPriceChange: " + String.format("%.2f", percentChange) + " %");
        Button b = new Button("Buy");
        Button more = new Button("more info");
    
    // Style of elements
    l.setStyle("-fx-font-size: 15;");
    b.maxHeight(l.getHeight());
    more.maxHeight(l.getHeight());  // styling for buy and more info buttons
    more.setStyle("-fx-background-color: #090a0c;" 
        + "-fx-text-fill: linear-gradient(white, #d0d0d0); -fx-padding:10px;");
    b.setStyle("-fx-background-color: #090a0c;"
        + " -fx-text-fill: linear-gradient(white, #d0d0d0);  -fx-padding:10px;");
    VBox h = new VBox(l);
    h.setPadding(new Insets(10, 10, 10, 10));
    h.setStyle("-fx-background-color: #dbdbdb");
    HBox hbox = new HBox(b, more);
    hbox.setSpacing(15);
    hbox.setMargin(b, new Insets(0, 0, 0, 70));
    hbox.setStyle("-fx-background-color: #dbdbdb; -fx-margin: auto");

    b.setOnMouseClicked(event -> {
      searchBar.setText(rowWatch.get(0));
      toStockPage();
    });

    more.setOnMouseClicked(event -> {
      stockOnWeb = rowWatch.get(0);
      openBrowser();
    });

    scrollPane.getChildren().addAll(h);
    scrollPane.getChildren().addAll(hbox);
      }

    }
  }

  /**
   * Navigates to profile.
   */
  public void toProfile() {
    StonkApp app = new StonkApp();
    app.changeScene("profile.fxml");

  }

  /**
   * Initializes on start.
   */
  @FXML
  private void initialize() {
    this.user = StonkApp.getStaticUser();
    displayOnMain();
  }

}

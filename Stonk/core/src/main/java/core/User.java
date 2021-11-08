package core;

import data.DataHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class user.
 */
public class User {

  public float cash;
  private int age;
  private String firstName;
  private String lastName;
  private String password;
  private String username;
  // private JSONArray portfolio; Unused

  public DataHandler handler = new DataHandler();

  /**
   * Constructor.
   *
   * @param firstName string
   * @param lastName string
   * @param username string
   * @param password string
   * @param cash float
   * @param age int
   * @param portfolio JSONArray
   * @param isNewUser boolean
   */
  public User(String firstName, String lastName, String username, String password,
      float cash, int age, JSONArray portfolio, JSONArray watchList, boolean isNewUser) {
    if (isNewUser) {
      setFirstName(firstName);
      setLastName(lastName);
      setUserName(username);
      setPassword(password);
      setCash(cash);
      setAge(age);
      // this.portfolio = getPortfolio(); bruker vi den?
      handler.newUser(username, password, firstName, lastName, age, cash, new JSONArray(), new JSONArray());
    } else {
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.password = password;
      this.cash = cash;
      this.age = age;
    }
  }

  public User() {
    firstName = "This is a dummy, used as temp";
  }

  /**
   * Adds stock to portfolio.
   *
   * @param ticker for finding stock.
   * @param count how many.
   */
  public void addToPortfoilio(String ticker, int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("Amount of stocks cant be negative or 0");
    }
    Stonk stock = new Stonk();
    stock.getStockInfo(ticker);
    setCash(cash - (stock.getPrice() * count));
    handler.addToPortfoilio(username, ticker, stock.getPrice(), count);
  }
  
  public void addToWatchList(String ticker, int count) {
    Stonk stock = new Stonk();
    stock.getStockInfo(ticker);
    handler.addToWatchList(username, ticker, stock.getPrice(), 1);
  }

  /**
   * Removes from portfolio.
   *
   * @param ticker for finding stock.
   * @param count how many.
   */
  public void removeFromPortfolio(String ticker, int count) {
    if (count <= 0) {
      throw new IllegalArgumentException("Amount of stocks cant be negative or 0");
    }
    Stonk stock = new Stonk();
    stock.getStockInfo(ticker);
    handler.removeFromPortfolio(username, ticker, stock.getPrice(), count);
    setCash(cash + (stock.getPrice() * count));
  }

  public JSONArray getPortfolio() {
    return handler.getPortfolio(handler.findUser(username));
  }
  public JSONArray getWatchList() {
    return handler.getWatchList(handler.findUser(username));
  }

  /**
   * Sets first name.
   *
   * @param firstName new firstName.
   */
  public void setFirstName(String firstName) {
    if (firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be blank");
    }
    this.firstName = firstName;
  }

  /**
   * Sets last name.
   *
   * @param lastName new lastName.
   */
  public void setLastName(String lastName) {
    if (lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be blank");
    }
    this.lastName = lastName;
  }

  /**
   * Sets username.
   *
   * @param name new username.
   */
  public void setUserName(String name) {
    if (name.isBlank()) {
      throw new IllegalArgumentException("Username cannot be blank");
    }
    if (handler.findUser(name) != null) {
      throw new IllegalArgumentException("Username is already registered");
    }
    this.username = name;
  }

  /**
   * Sets password.
   *
   * @param password new password.
   */
  private void setPassword(String password) {
    if (password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be blank");
    }
    this.password = password;
  }

  /**
   * Sets the cash.
   *
   * @param cash how much.
   */
  private void setCash(float cash) {
    if (cash < 0) {
      throw new IllegalArgumentException("Cant set a negative balance");
    }
    this.cash = cash;
  }

  /**
   * Adds money.
   *
   * @param cash how much.
   */
  public void addMoney(float cash) {
    if (cash < 0) {
      throw new IllegalArgumentException("Cant set a negative balance");
    }
    this.cash += cash;
    handler.addOrRemoveCash(getUserName(), cash);
  }

  /**
   * Removes money.
   *
   * @param cash how much.
   */
  public void removeMoney(float cash) {
    if (cash < 0) {
      throw new IllegalArgumentException("Cant set a positive number");
    }
    this.cash -= cash;
    handler.addOrRemoveCash(getUserName(), cash);
  }

  /**
   * Sets age. 
   *
   * @param age int.
   */
  public void setAge(int age) {
    if (age < 18) {
      throw new IllegalArgumentException("You need to be over 18 years");
    }
    if (String.valueOf(age).isEmpty()) {
      throw new IllegalArgumentException("Age cannot be empty");
    }
    this.age = age;
  }

  /**
   * Checks if login is valid.
   *
   * @param username string.
   * @param password string.
   * @return the gives user if velud.
   */
  public User isLoginValid(String username, String password) {
    JSONObject temp = handler.isLoginValid(username, password);
    return new User(temp.get("firstname").toString(),
        temp.get("lastname").toString(), temp.get("username").toString(),
        temp.get("password").toString(), Float.parseFloat(temp.get("cash").toString()),
        Integer.parseInt(temp.get("age").toString()), (JSONArray) temp.get("portfolio"), (JSONArray) temp.get("watchList"), false);
  }

  public String getUserName() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public float getCash() {
    return cash;
  }

  public int getAge() {
    return age;
  }



  /**
   * Main.
   *
   * @param args .
   */


}

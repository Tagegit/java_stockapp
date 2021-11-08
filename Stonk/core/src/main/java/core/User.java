package core;

import java.nio.charset.Charset;
import java.security.MessageDigest;
//encryption imports
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;  

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
  private ArrayList<Stonk> portfolio = new ArrayList<Stonk>(); 

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
      float cash, int age, ArrayList<Stonk> portfolio, boolean isNewUser) {
    if (isNewUser) {
      setFirstName(firstName);
      setLastName(lastName);
      setUserName(username);
      setPassword(encryptPassword(password));
      setCash(cash);
      setAge(age);
      setPortfolio(portfolio);
    } else {
      this.firstName = firstName;
      this.lastName = lastName;
      this.username = username;
      this.password = password;
      this.cash = cash;
      this.age = age;
      setPortfolio(portfolio);
    }
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
    Stonk stock = new Stonk(ticker, count);
    boolean isOwned = false;
    for(Stonk i : portfolio){
      if (i.getTicker().equals("ticker")){
        i.setNewAverage(stock);
        isOwned = true;
      }
    }
    if(!isOwned){
      portfolio.add(stock);
    }
    setCash(cash - (stock.getPrice() * count));
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
    Stonk stock = new Stonk(ticker, count);
    boolean isOwned = false;
    for(Stonk i : getPortfolio()){
      if(i.getTicker().equals(ticker)){
        isOwned = true;
        if(count > i.getCount()){
          throw new IllegalArgumentException("Not enough stocks to sell");
        }
        else if (count == i.getCount()){
          portfolio.remove(i);
        }
        else {
          i.setNewCount(stock);
        }
      }
    }
    if (!isOwned){
      throw new IllegalArgumentException("Stock is not in portfolio");
    }
    setCash(cash + (stock.getPrice() * count));
  }

  public ArrayList<Stonk> getPortfolio() {
    return new ArrayList<Stonk>(portfolio);
  }

  private void setPortfolio(ArrayList<Stonk> portfolio){
    this.portfolio = new ArrayList<Stonk>(portfolio);
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
    this.password = encryptPassword(password);
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

  public String encryptPassword(String password) {
    String tempPassword = password;
    String encryptedPassword = null;
    try{
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.update(tempPassword.getBytes(Charset.forName("UTF-8")));
      byte[] bytes = m.digest(); 
      StringBuilder s = new StringBuilder();  
      for(int i=0; i< bytes.length ;i++)  {  
            s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));  
        }
      encryptedPassword = s.toString(); 
    }
    catch (NoSuchAlgorithmException e){
      e.printStackTrace();
    }
    return encryptedPassword;
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
  public static void main(String[] args) {
  }

}

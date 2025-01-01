public class Account {
  public String account_number;
  public String pin;
  public double balance;
  public String role;

  public Account(String account_number, String pin, double balance,
                 String role) {
    this.account_number = account_number;
    this.pin = pin;
    this.balance = balance;
    this.role = role;
  }
}
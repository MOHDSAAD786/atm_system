import java.sql.*;
import java.util.Scanner;

public class Main {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_system?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Mohdsaad@786";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean authenticateUser(String userId, String pin) {
        String query = "SELECT * FROM users WHERE user_id = ? AND pin = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void transactionHistory(String userId) {
        String query = "SELECT * FROM transactions WHERE user_id = ? ORDER BY timestamp DESC";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Transaction History:");
            while (rs.next()) {
                System.out.println(rs.getString("type") + ": " + rs.getDouble("amount") + " on " + rs.getTimestamp("timestamp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdraw(String userId, double amount) {
        String balanceQuery = "SELECT balance FROM users WHERE user_id = ?";
        String updateQuery = "UPDATE users SET balance = balance - ? WHERE user_id = ? AND balance >= ?";
        String insertTransaction = "INSERT INTO transactions (user_id, type, amount, timestamp) VALUES (?, 'Withdraw', ?, NOW())";
        try (Connection conn = connect();
             PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement transactionStmt = conn.prepareStatement(insertTransaction)) {

            balanceStmt.setString(1, userId);
            ResultSet rs = balanceStmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    updateStmt.setDouble(1, amount);
                    updateStmt.setString(2, userId);
                    updateStmt.setDouble(3, amount);
                    updateStmt.executeUpdate();

                    transactionStmt.setString(1, userId);
                    transactionStmt.setDouble(2, amount);
                    transactionStmt.executeUpdate();

                    System.out.println("Withdrawal successful!");
                } else {
                    System.out.println("Insufficient balance!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(String userId, double amount) {
        String updateQuery = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        String insertTransaction = "INSERT INTO transactions (user_id, type, amount, timestamp) VALUES (?, 'Deposit', ?, NOW())";
        try (Connection conn = connect();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement transactionStmt = conn.prepareStatement(insertTransaction)) {

            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, userId);
            updateStmt.executeUpdate();

            transactionStmt.setString(1, userId);
            transactionStmt.setDouble(2, amount);
            transactionStmt.executeUpdate();

            System.out.println("Deposit successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transfer(String userId, String receiverId, double amount) {
        String balanceQuery = "SELECT balance FROM users WHERE user_id = ?";
        String updateSender = "UPDATE users SET balance = balance - ? WHERE user_id = ? AND balance >= ?";
        String updateReceiver = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        String insertTransaction = "INSERT INTO transactions (user_id, type, amount, timestamp) VALUES (?, 'Transfer', ?, NOW())";

        try (Connection conn = connect();
             PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery);
             PreparedStatement senderStmt = conn.prepareStatement(updateSender);
             PreparedStatement receiverStmt = conn.prepareStatement(updateReceiver);
             PreparedStatement transactionStmt = conn.prepareStatement(insertTransaction)) {

            balanceStmt.setString(1, userId);
            ResultSet rs = balanceStmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    senderStmt.setDouble(1, amount);
                    senderStmt.setString(2, userId);
                    senderStmt.setDouble(3, amount);
                    senderStmt.executeUpdate();

                    receiverStmt.setDouble(1, amount);
                    receiverStmt.setString(2, receiverId);
                    receiverStmt.executeUpdate();

                    transactionStmt.setString(1, userId);
                    transactionStmt.setDouble(2, amount);
                    transactionStmt.executeUpdate();

                    System.out.println("Transfer successful!");
                } else {
                    System.out.println("Insufficient balance!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main atm = new Main();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter User ID:");
        String userId = scanner.nextLine();
        System.out.println("Enter PIN:");
        String pin = scanner.nextLine();

        if (atm.authenticateUser(userId, pin)) {
            int choice;
            do {
                System.out.println("\n1. Transaction History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Quit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        atm.transactionHistory(userId);
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        atm.withdraw(userId, withdrawAmount);
                        break;
                    case 3:
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        atm.deposit(userId, depositAmount);
                        break;
                    case 4:
                        System.out.print("Enter receiver ID: ");
                        scanner.nextLine(); // Consume newline
                        String receiverId = scanner.nextLine();
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        atm.transfer(userId, receiverId, transferAmount);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } while (choice != 5);
        } else {
            System.out.println("Invalid User ID or PIN.");
        }
    }
}

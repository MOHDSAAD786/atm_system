# ATM System Project

## Project Description
This project is a simple ATM system implemented in Java and MySQL. It allows users to:
- Authenticate with a user ID and PIN.
- View transaction history.
- Withdraw funds.
- Deposit funds.
- Transfer funds to another user.

The project uses JDBC to interact with a MySQL database for managing user accounts and transactions.

## Project Structure
- **Main.java**: The Java source code for the ATM system.
- **atm_system.sql**: SQL file to set up the database and tables with sample data.
- **README.md**: This file, containing project details and instructions.

## Database Setup Instructions
1. Install MySQL on your system if not already installed.
2. Open your MySQL client (e.g., MySQL Workbench, Command Line).
3. Execute the provided `atm_system.sql` file:
   ```
   SOURCE path/to/atm_system.sql;
   ```
   Replace `path/to/atm_system.sql` with the full path to the SQL file.

4. Verify that the database and tables have been created:
   ```sql
   SHOW DATABASES;
   USE atm_system;
   SHOW TABLES;
   ```

## Login Credentials for Testing
Sample user data has been preloaded into the database:
- **User 1**:
  - User ID: `user1`
  - PIN: `1234`
  - Balance: `1000`
- **User 2**:
  - User ID: `user2`
  - PIN: `5678`
  - Balance: `2000`
- **User 3**:
  - User ID: `user3`
  - PIN: `0000`
  - Balance: `500`

## How to Run the Java Program
1. Ensure you have Java JDK installed on your system.
2. Compile the Java program:
   ```
   javac Main.java
   ```
3. Run the program:
   ```
   java Main
   ```
4. Follow the prompts in the console to interact with the ATM system.

## Features
1. **Authentication**: Secure login using user ID and PIN.
2. **Transaction History**: View recent transactions (sorted by time).
3. **Withdraw**: Deduct funds from the user's account (with balance check).
4. **Deposit**: Add funds to the user's account.
5. **Transfer**: Transfer funds to another user's account (with balance check).

## Dependencies
- Java JDK (version 8 or above)
- MySQL Server
- MySQL JDBC Driver (ensure the `mysql-connector-java` is available in your classpath)

## Notes
- Ensure the database credentials in the Java code (`Main.java`) match your MySQL setup.
- Test the program with the provided sample data or add your own data using SQL.

---

### For Any Issues
If you encounter issues, ensure:
1. The database is running.
2. The `atm_system.sql` file has been executed correctly.
3. The database credentials in `Main.java` are correct.

package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DatabaseConnector {

    private SQLiteDataSource dataSource;

    public DatabaseConnector(String fileName) throws SQLException {
        String url = "jdbc:sqlite:" + fileName;
        this.dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try(Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid");
            }
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER," +
                        "number TEXT," +
                        "pin TEXT," +
                        "balance INTEGER DEFAULT 0" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewCard(String number, String pin) {

        try(Connection con = dataSource.getConnection()) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid");
            }
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("INSERT INTO card (number, pin)" +
                        "VALUES (" +
                        "'" + number + "'," +
                        "'" + pin + "'" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Account getAccount (String number, String pin) throws SQLException {

        String query = "SELECT id, number, pin, balance FROM card WHERE number = '" + number + "' LIMIT 1";

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String cardNumber = resultSet.getString(2);
                    String cardPin = resultSet.getString(3);
                    if (!(cardPin.equals(pin))) {
                        return null;
                    } else {
                        int balance = resultSet.getInt(4);
                        return new Account(id, cardNumber, cardPin, balance);
                    }
                }
            } catch( SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateBalance(Account account) throws SQLException {

        String update = "UPDATE card SET balance = ? WHERE number = ?";

        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = con.prepareStatement(update)) {
                preparedStatement.setInt(1, account.getBalance());
                preparedStatement.setString(2, account.getCardNumber());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String number) {
        String query = "SELECT id, number, pin, balance FROM card WHERE number = '" + number + "'";
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String cardNumber = resultSet.getString(2);
                    String cardPin = resultSet.getString(3);
                    int balance = resultSet.getInt(4);
                    return new Account(id, cardNumber, cardPin, balance);
                }
            } catch( SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAccount(String number) {

        String delete = "DELETE FROM card WHERE number = '" + number + "'";
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                boolean rowsAffected =  statement.execute(delete);
                if (rowsAffected) {
                    System.out.println("The account has been closed!");
                }
            }
            catch( SQLException e) {
                e.printStackTrace();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

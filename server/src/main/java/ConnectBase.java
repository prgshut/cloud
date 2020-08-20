import java.sql.*;

public class ConnectBase {
    private static final String nameUser= "root";
    private static final String passUser = "root";
    private static final String urlBase = "jdbc:mysql://localhost:3306/cloud?serverTimezone=Europe/Moscow&useSSL=false";
    private static Connection con;
    private static Statement stmt;
    public  void conect() throws SQLException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            con= DriverManager.getConnection(urlBase,nameUser,passUser);
            System.out.println("conect base");
            stmt=con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public  void disconect(){
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public  int update(String request){
        int rez;
        try {
            return rez= stmt.executeUpdate(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public ResultSet select(String request){
        ResultSet rez;
        try {
            return rez = stmt.executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

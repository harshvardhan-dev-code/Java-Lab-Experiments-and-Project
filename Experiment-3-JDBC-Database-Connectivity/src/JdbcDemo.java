import java.sql.*;

public class JdbcDemo {

    public static void main(String args[]) throws Exception {

        // Step 1: Load the MySQL JDBC Driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Step 2: Make the connection
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college",
                "root",
                "root"
        );

        // Step 3: Create a PreparedStatement with placeholders
        PreparedStatement ps = con.prepareStatement(
                "insert into student values(?,?,?)"
        );

        // Step 4: Set values for each placeholder
        ps.setInt(1, 101);
        ps.setString(2, "Rahul");
        ps.setString(3, "CSE");

        // Step 5: Execute the INSERT
        ps.executeUpdate();
        System.out.println("Record Inserted");

        // Step 6: Create a Statement and run a SELECT
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(
                "select * from student"
        );

        // Step 7: Read the ResultSet row by row
        while (rs.next()) {
            System.out.println(rs.getInt(1) + " "
                    + rs.getString(2) + " "
                    + rs.getString(3));
        }

        // Step 8: Close the connection
        con.close();
    }
}

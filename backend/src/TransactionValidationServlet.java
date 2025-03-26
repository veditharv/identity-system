import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/validate")
public class TransactionValidationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            Gson gson = new Gson();
            UserRequest userRequest = gson.fromJson(request.getReader(), UserRequest.class);
            
            String sql = "SELECT * FROM users WHERE full_name = ? AND dob = ? AND issue_date = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userRequest.fullName);
            stmt.setString(2, userRequest.dob);
            stmt.setString(3, userRequest.issueDate);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                out.print("{\"message\": \"Validation Successful!\"}");
            } else {
                out.print("{\"message\": \"Validation Failed!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"message\": \"Error occurred!\"}");
        }
    }

    private static class UserRequest {
        String fullName;
        String dob;
        String issueDate;
    }
}

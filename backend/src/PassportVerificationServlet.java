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

@WebServlet("/verify-passport")
public class PassportVerificationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try (Connection conn = DatabaseConnection.getConnection()) {
            Gson gson = new Gson();
            PassportRequest passportRequest = gson.fromJson(request.getReader(), PassportRequest.class);

            String sql = "SELECT * FROM users WHERE RIGHT(passport_number, 4) = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, passportRequest.passportDigits);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                out.print("{\"message\": \"✅ Passport Verified!\"}");
            } else {
                out.print("{\"message\": \"❌ Passport Verification Failed!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"message\": \"Error occurred!\"}");
        }
    }

    private static class PassportRequest {
        String passportDigits;
    }
}

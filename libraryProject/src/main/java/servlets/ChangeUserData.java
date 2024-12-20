package servlets;

import repositories.UserRepositoryJdbcImpl;
import repositories.interfaces.UserRepository;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/changeUserData")
public class ChangeUserData extends HttpServlet {
    private UserRepository userRepository = null;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        userRepository = (UserRepositoryJdbcImpl) servletContext.getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/changeUserData.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(false);

        try {
            userRepository.updateUserData((int) session.getAttribute("userId"), username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}

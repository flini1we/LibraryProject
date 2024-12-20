package servlets;

import models.User;
import repositories.UserRepositoryJdbcImpl;
import repositories.interfaces.UserRepository;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteAccount")
public class DeleteAccountServlet extends HttpServlet {
    private UserRepository userRepository;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        userRepository = (UserRepositoryJdbcImpl) servletContext.getAttribute("userRepository");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        HttpSession session = req.getSession();
        User admin = (User) session.getAttribute("admin");

        if (userId < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Необходим идентификатор пользователя.");
            return;
        }

        boolean result;
        try {
            result = userRepository.delete(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (result) {
            if (admin == null) {
                resp.sendRedirect(getServletContext().getContextPath() + "/login");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/adminMain");

        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при удалении аккаунта.");
        }
    }
}

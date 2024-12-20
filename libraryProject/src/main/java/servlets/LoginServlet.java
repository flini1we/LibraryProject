package servlets;

import models.User;
import repositories.UserRepositoryJdbcImpl;
import utils.PasswordUtil;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserRepositoryJdbcImpl userRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        userRepository = (UserRepositoryJdbcImpl) servletContext.getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user;
        try {
            user = userRepository.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            HttpSession session = request.getSession();
            if (user.getRole().equals("ADMIN")) {
                session.setAttribute("admin", user);
                session.setAttribute("adminId", user.getId());
                response.sendRedirect(getServletContext().getContextPath() + "/adminMain");
            } else {
                session.setAttribute("user", user);
                session.setAttribute("userId", userRepository.getIdByUsername(user.getUsername()));
                response.sendRedirect(getServletContext().getContextPath() + "/main");
            }
        } else {
            request.removeAttribute("errorMessage");
            request.setAttribute("errorMessage", "Неверное имя пользователя или пароль");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}

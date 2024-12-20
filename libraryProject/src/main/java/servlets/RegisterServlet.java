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
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserRepositoryJdbcImpl userRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        userRepository = (UserRepositoryJdbcImpl) servletContext.getAttribute("userRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String hashedPassword = PasswordUtil.hashPassword(password);

        try {
            if (userRepository.userExists(username, email)) {
                request.removeAttribute("errorMessage");
                request.setAttribute("errorMessage", "Пользователь с таким именем уже зарегистрирован.");
                request.getRequestDispatcher("/login").forward(request, response);
                return;
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setEmail(email);
            user.setRole("READER");
            user.setRegistrationDate(LocalDateTime.now());

            userRepository.create(user);
            response.sendRedirect(getServletContext().getContextPath() + "/login");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
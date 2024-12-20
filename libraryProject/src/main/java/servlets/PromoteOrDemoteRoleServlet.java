package servlets;

import listener.LibraryContextListener;
import models.User;
import repositories.UserRepositoryJdbcImpl;
import repositories.interfaces.AdminRepository;
import repositories.interfaces.UserRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/promoteOrDemote")
public class PromoteOrDemoteRoleServlet extends HttpServlet {
    private AdminRepository adminRepository;
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        userRepository = (UserRepositoryJdbcImpl) servletContext.getAttribute("userRepository");
        adminRepository = (AdminRepository) servletContext.getAttribute("adminRepository");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        User user;
        try {
            user = userRepository.getById(userId);
            adminRepository.promoteOrDemoteUser(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

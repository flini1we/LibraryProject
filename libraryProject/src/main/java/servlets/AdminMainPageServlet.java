package servlets;

import models.User;
import repositories.interfaces.AdminRepository;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/adminMain")
public class AdminMainPageServlet extends HttpServlet {
    private AdminRepository adminRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        adminRepository = (AdminRepository) servletContext.getAttribute("adminRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User admin = (User) req.getSession().getAttribute("admin");
        try {
            List<User> users = adminRepository.getAllUsersExceptSelf(admin.getId());
            req.setAttribute("users", users);
            req.getRequestDispatcher("/jsp/adminMain.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

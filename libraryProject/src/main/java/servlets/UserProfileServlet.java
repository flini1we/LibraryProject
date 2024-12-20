package servlets;

import lombok.SneakyThrows;
import models.User;
import repositories.ReserveRepositoryJdbcImpl;
import repositories.interfaces.ReserveRepository;
import service.UserServiceImpl;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {
    private UserServiceImpl userService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        userService = (UserServiceImpl) servletContext.getAttribute("userService");
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("user", user);

        String formattedDate = user.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        request.setAttribute("formattedDate", formattedDate);

        String userId = String.valueOf(user.getId());

        boolean doUserHaveAnyReservedBooks = userService.doUserHaveAnyReservedBooks(Integer.parseInt(userId));
        request.setAttribute("doUserHaveAnyReservedBooks", doUserHaveAnyReservedBooks);

        request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
    }
}

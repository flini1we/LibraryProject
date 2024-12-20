package servlets;

import models.User;
import models.dto.BookDTO;
import repositories.ReserveRepositoryJdbcImpl;
import repositories.interfaces.ReserveRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/userBooked")
public class UserBookedServlet extends HttpServlet {
    private ReserveRepositoryJdbcImpl reserveRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        reserveRepository = (ReserveRepositoryJdbcImpl) servletContext.getAttribute("reserveRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        try {
            List<BookDTO> reservedBooks = reserveRepository.getReservedBooksByUser(user.getId());
            req.setAttribute("reservedBooks", reservedBooks);
            req.getRequestDispatcher("/jsp/userBooked.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

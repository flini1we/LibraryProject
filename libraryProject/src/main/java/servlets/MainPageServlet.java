package servlets;

import models.dto.BookDTO;
import repositories.ReserveRepositoryJdbcImpl;
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

@WebServlet("/main")
public class MainPageServlet extends HttpServlet {
    private ReserveRepositoryJdbcImpl reserveRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        reserveRepository = (ReserveRepositoryJdbcImpl) servletContext.getAttribute("reserveRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");

        try {
            List<BookDTO> books = reserveRepository.getAllBooksWithReserveInfo(userId);
            request.setAttribute("books", books);

            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при получении списка книг");
        }
    }
}

package servlets;

import repositories.interfaces.BookReservationRepository;
import repositories.interfaces.ReserveRepository;
import repositories.interfaces.serviceInterfaces.ReserveService;
import service.ReserveServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bookDetails")
public class BookDetailsServlet extends HttpServlet {
    private ReserveService reserveService;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        reserveService = (ReserveServiceImpl) servletContext.getAttribute("reserveService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int userId = (int) request.getSession().getAttribute("userId");

        boolean isReservedByUser = reserveService.isReservedByUser(bookId, userId);
        boolean isAvailable = reserveService.areCopiesAvailable(bookId);

        request.setAttribute("isReservedByUser", isReservedByUser);
        request.setAttribute("isAvailable", isAvailable);
        request.getRequestDispatcher("/bookDetails.jsp").forward(request, response);
    }
}

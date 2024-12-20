package servlets;

import com.google.gson.JsonObject;
import repositories.interfaces.BookReservationRepository;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

@WebServlet("/reserveBook")
public class ReserveBookServlet extends HttpServlet {
    private BookReservationRepository bookReservationRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        bookReservationRepository = (BookReservationRepository) servletContext.getAttribute("bookReservationRepository");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int userId = (int) request.getSession().getAttribute("userId");
        Timestamp reservationDate = new Timestamp(System.currentTimeMillis());

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (bookReservationRepository.areCopiesAvailable(bookId)) {
                bookReservationRepository.reserveBook(bookId, userId, reservationDate);
                bookReservationRepository.updateAvailableCopies(bookId, -1);
                int newAvailableCopies = bookReservationRepository.getAvailableCopiesById(bookId);

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("newButtonText", "Сдать");
                jsonResponse.addProperty("newButtonColor", "red");
                jsonResponse.addProperty("availableCopies", newAvailableCopies);
                request.setAttribute("newAvailableCopies", newAvailableCopies);

                out.print(jsonResponse.toString());
                out.flush();
            } else {
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Нет доступных копий");

                out.print(jsonResponse.toString());
                out.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

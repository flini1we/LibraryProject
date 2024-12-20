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

@WebServlet("/returnBook")
public class ReturnBookServlet extends HttpServlet {
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

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            bookReservationRepository.returnBook(bookId, userId);
            bookReservationRepository.updateAvailableCopies(bookId, 1);
            int newAvailableCopies = bookReservationRepository.getAvailableCopiesById(bookId);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("newButtonText", "Забронировать");
            jsonResponse.addProperty("newButtonColor", "green");
            jsonResponse.addProperty("availableCopies", newAvailableCopies);
            request.setAttribute("newAvailableCopies", newAvailableCopies);

            out.print(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            try (PrintWriter out = response.getWriter()) {
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Ошибка сервера");

                out.print(jsonResponse.toString());
                out.flush();
            }
        }
    }
}
package servlets;

import com.google.gson.JsonObject;
import models.dto.BookDTO;
import repositories.BookRepositoryJdbcImpl;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/addBooks")
public class AddBooksServlet extends HttpServlet {
    private BookRepositoryJdbcImpl bookRepositoryJdbc;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        bookRepositoryJdbc = (BookRepositoryJdbcImpl) servletContext.getAttribute("bookRepository");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<BookDTO> books = bookRepositoryJdbc.getAllBooks();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/jsp/addBooks.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            String action = req.getParameter("action");

            if (action.equals("increase")) {
                bookRepositoryJdbc.increaseBookCopies(bookId);
            } else {
                bookRepositoryJdbc.decreaseBookCopies(bookId);
            }
            int newAvailableCopies = bookRepositoryJdbc.getBookById(bookId).getAvailableCopies();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("availableCopies", newAvailableCopies);
        } catch (SQLException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Invalid book ID format.");
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Unexpected error: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
    }
}

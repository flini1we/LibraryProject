package servlets;

import lombok.SneakyThrows;
import models.dto.BookDTO;
import repositories.interfaces.BookRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bookDescription")
public class BookDescriptionServlet extends HttpServlet {
    private BookRepository bookRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        bookRepository = (BookRepository) servletContext.getAttribute("bookRepository");
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        BookDTO book = bookRepository.findBookDTOById(bookId);
        if (book == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("bookDTO", book);
        req.getRequestDispatcher("/jsp/bookDescription.jsp").forward(req, resp);
    }
}

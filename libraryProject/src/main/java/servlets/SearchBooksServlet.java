package servlets;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import models.dto.BookDTO;
import repositories.interfaces.BookRepository;
import repositories.interfaces.serviceInterfaces.BookService;
import service.BookServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/searchBooks")
public class SearchBooksServlet extends HttpServlet {
    private BookRepository bookRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        bookRepository = (BookRepository) servletContext.getAttribute("bookRepository");
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        BookService bookService = new BookServiceImpl(bookRepository);
        List<BookDTO> books = null;
        try {
            books = bookService.getAllBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (query != null && !query.trim().isEmpty()) {
            String searchQuery = query.toLowerCase();
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchQuery) ||
                            book.getAuthorName().toLowerCase().contains(searchQuery))
                    .peek(book -> {
                        book.setAvailableCopiesPresent(book.getAvailableCopies() > 0);
                        book.setReservedByUser(book.isReservedByUser());
                    })
                    .collect(Collectors.toList());
        }

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(books);
            response.getWriter().write(json);
        } else {
            request.setAttribute("books", books);
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
        }
    }
}

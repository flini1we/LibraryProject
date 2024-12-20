package servlets;

import lombok.SneakyThrows;
import models.Book;
import models.Review;
import repositories.interfaces.BookRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/bookDetail")
public class BookDetail extends HttpServlet {
    private BookRepository bookRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        bookRepository = (BookRepository) servletContext.getAttribute("bookRepository");
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        HttpSession session = req.getSession();
        session.setAttribute("bookId", bookId);

        Book currentBook = bookRepository.getBookById(bookId);
        List<Review> reviews = bookRepository.getReviewsForBook(bookId);

        for (Review review : reviews) {
            review.setBookTitle(currentBook.getTitle());
        }

        req.setAttribute("reviews", reviews);
        req.getRequestDispatcher("/jsp/bookDetail.jsp").forward(req, resp);
    }
}
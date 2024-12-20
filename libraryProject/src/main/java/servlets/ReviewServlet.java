package servlets;

import models.Review;
import repositories.interfaces.serviceInterfaces.ReviewService;
import service.ReviewServiceImpl;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        reviewService = (ReviewServiceImpl) servletContext.getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        try {
            List<Review> reviews = reviewService.getReviewsByBookId(bookId);
            request.setAttribute("reviews", reviews);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error fetching reviews");
        }
        try {
            request.getRequestDispatcher("").forward(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

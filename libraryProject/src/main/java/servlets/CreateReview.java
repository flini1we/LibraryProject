package servlets;

import models.Review;
import repositories.ReviewRepositoryJdbcImpl;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/createReview")
public class CreateReview extends HttpServlet {
    private ReviewRepositoryJdbcImpl reviewRepository;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        reviewRepository = (ReviewRepositoryJdbcImpl) servletContext.getAttribute("reviewRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/createReview.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        try {
            HttpSession session = req.getSession();

            int bookId = session.getAttribute("bookId") == null ? -1 : Integer.parseInt(session.getAttribute("bookId").toString());
            int userId = session.getAttribute("userId") == null ? -1 : Integer.parseInt(session.getAttribute("userId").toString());
            int rating = req.getParameter("rating") == null ? -1 : Integer.parseInt(req.getParameter("rating"));
            String reviewText = req.getParameter("reviewText") == null ? "" : req.getParameter("reviewText");
            Timestamp reviewDate = new Timestamp(System.currentTimeMillis());

            Review review = new Review(bookId, userId, rating, reviewText, reviewDate);
            reviewRepository.createReview(review);
            resp.sendRedirect(req.getContextPath() + "/bookDetail?bookId=" + bookId);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при создании отзыва");
        }
    }

}

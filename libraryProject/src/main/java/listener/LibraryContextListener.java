package listener;

import repositories.*;
import repositories.interfaces.serviceInterfaces.ReviewService;
import service.ReserveServiceImpl;
import service.ReviewServiceImpl;
import service.UserServiceImpl;
import utils.DatabaseConnection;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class LibraryContextListener implements ServletContextListener {
    private static Connection connection;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            connection = DatabaseConnection.getConnection();
            AdminRepositoryJdbcImpl adminRepository = new AdminRepositoryJdbcImpl(connection);
            AuthorRepositoryJdbcImpl authorRepository = new AuthorRepositoryJdbcImpl(connection);
            BookRepositoryJdbcImpl bookRepository = new BookRepositoryJdbcImpl(connection);
            BookReservationRepositoryJdbcImpl bookReservationRepository = new BookReservationRepositoryJdbcImpl(connection);
            ReserveRepositoryJdbcImpl reserveRepository = new ReserveRepositoryJdbcImpl(connection);
            ReviewRepositoryJdbcImpl reviewRepository = new ReviewRepositoryJdbcImpl(connection);
            UserRepositoryJdbcImpl userRepository = new UserRepositoryJdbcImpl(connection);
            ReserveServiceImpl reserveService = new ReserveServiceImpl(reserveRepository, bookReservationRepository);
            ReviewService reviewService = new ReviewServiceImpl(reviewRepository);
            UserServiceImpl userService = new UserServiceImpl(reserveRepository);

            ServletContext servletContext = sce.getServletContext();
            servletContext.setAttribute("adminRepository", adminRepository);
            servletContext.setAttribute("authorRepository", authorRepository);
            servletContext.setAttribute("bookRepository", bookRepository);
            servletContext.setAttribute("bookReservationRepository", bookReservationRepository);
            servletContext.setAttribute("reserveRepository", reserveRepository);
            servletContext.setAttribute("reviewRepository", reviewRepository);
            servletContext.setAttribute("userRepository", userRepository);
            servletContext.setAttribute("reserveService", reserveService);
            servletContext.setAttribute("reviewService", reviewService);
            servletContext.setAttribute("userService", userService);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

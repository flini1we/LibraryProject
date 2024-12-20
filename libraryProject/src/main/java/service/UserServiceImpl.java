package service;

import repositories.interfaces.ReserveRepository;

import java.sql.SQLException;

public class UserServiceImpl implements repositories.interfaces.serviceInterfaces.UserService {
    private ReserveRepository reserveRepository;

    public UserServiceImpl(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    @Override
    public boolean doUserHaveAnyReservedBooks(int userId) throws SQLException {
        return reserveRepository.doUserHaveReservedBooks(userId);
    }
}
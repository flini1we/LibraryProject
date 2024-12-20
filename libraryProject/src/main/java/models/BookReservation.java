package models;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BookReservation {
    private int id;
    private Book book;
    private User user;
    private LocalDateTime reservationDate;
    private LocalDateTime returnDate;
    private String status;
}

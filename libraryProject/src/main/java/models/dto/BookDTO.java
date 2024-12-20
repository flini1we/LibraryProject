package models.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private int id;
    private String title;
    private String authorName;
    private int publicationYear;
    private String description;
    private String isbn;
    private int availableCopies;
    private int totalCopies;
    private String imagePath;
    private boolean reservedByUser;
    private boolean availableCopiesPresent;
}

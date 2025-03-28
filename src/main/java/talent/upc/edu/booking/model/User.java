package talent.upc.edu.booking.model;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class User {

  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private LocalDate registrationDate;
  private String avatar;
}

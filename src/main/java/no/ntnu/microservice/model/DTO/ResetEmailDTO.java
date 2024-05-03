package no.ntnu.microservice.model.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetEmailDTO {

    private String password;
    private String token;

}

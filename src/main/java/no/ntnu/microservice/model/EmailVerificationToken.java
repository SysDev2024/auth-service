package no.ntnu.microservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import no.ntnu.microservice.model.sharedmodels.user.User;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerificationToken {

    @Id
    @GeneratedValue
    private Long id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime expiryDate;

    public Object orElseThrow(Object object) {
        // TODO(Magnus) Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}

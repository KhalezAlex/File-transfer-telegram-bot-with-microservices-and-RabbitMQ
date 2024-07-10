package org.klozevitz.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.klozevitz.entity.enums.ApplicationUserState;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @CreationTimstamp - this annotation generates first connection timestamp automatically
 * EnumType.ORDINAL - returns enumeration number instead of getName() method result
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Builder
@Entity
@Table(name = "app_user")
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private ApplicationUserState state;
}

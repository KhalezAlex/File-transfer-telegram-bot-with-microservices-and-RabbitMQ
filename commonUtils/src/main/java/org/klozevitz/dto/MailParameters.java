package org.klozevitz.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailParameters {
    private String id;
    private String emailTo;
}

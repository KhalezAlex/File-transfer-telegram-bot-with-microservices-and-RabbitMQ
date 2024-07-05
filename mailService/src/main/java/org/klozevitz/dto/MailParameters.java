package org.klozevitz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailParameters {
    private String id;
    private String emailTo;
}

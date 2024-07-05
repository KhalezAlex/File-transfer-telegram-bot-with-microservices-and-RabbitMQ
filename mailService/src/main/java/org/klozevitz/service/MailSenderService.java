package org.klozevitz.service;

import org.klozevitz.dto.MailParameters;

public interface MailSenderService {
    void send(MailParameters mailParameters);
}

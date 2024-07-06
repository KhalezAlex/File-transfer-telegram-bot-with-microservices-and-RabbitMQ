package org.klozevitz.service.interfaces;

import org.klozevitz.dto.MailParameters;

public interface MailSenderService {
    void send(MailParameters mailParameters);
}

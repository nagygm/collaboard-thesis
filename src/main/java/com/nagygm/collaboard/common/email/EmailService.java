package com.nagygm.collaboard.common.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    @Async
    void sendEmail(SimpleMailMessage email);
}

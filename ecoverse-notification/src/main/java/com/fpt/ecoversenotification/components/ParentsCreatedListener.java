package com.fpt.ecoversenotification.components;

import com.fpt.ecoversenotification.dtos.ParentsCreatedEvent;
import com.fpt.ecoversenotification.services.MailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ParentsCreatedListener {

    private final MailService mailService;

    public ParentsCreatedListener(MailService mailService) {
        this.mailService = mailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ParentsCreatedEvent event) {
        mailService.sendParentWelcomeBatch(event.getMails());
    }
}

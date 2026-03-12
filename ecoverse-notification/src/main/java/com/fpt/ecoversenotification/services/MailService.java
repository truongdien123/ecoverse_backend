package com.fpt.ecoversenotification.services;


import com.fpt.ecoversenotification.dtos.ParentCredentialMail;

import java.util.List;

public interface MailService {
    void sendParentWelcomeBatch(List<ParentCredentialMail> mails);
}

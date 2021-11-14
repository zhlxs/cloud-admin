package com.it.service;


import com.it.entity.Mail;

import java.util.List;

public interface MailService
{

	void save(Mail mail, List<String> toUser);
}

package br.com.alura.forum.service.infra;

import br.com.alura.forum.model.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ForumEmailService {

	private static final Logger logger = LoggerFactory.getLogger(ForumEmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmail(Answer answer) {
		SimpleMailMessage smm = new SimpleMailMessage();

		smm.setTo(answer.getTopic().getOwner().getEmail());
		smm.setSubject("Novo comentário em: " + answer.getTopic().getShortDescription());
		smm.setText("Olé " + answer.getTopic().getOwnerName() + " \n\n " +
				"Há uma nova mensagem no fórum! " + answer.getOwnerName() +
				" comentou o tópico: " + answer.getTopic().getShortDescription());

		try {
			mailSender.send(smm);
		} catch (MailServiceException me) {
			logger.info("nao foi possivel enviar email para: " + answer.getTopic().getOwner().getEmail());
			throw new MailServiceException("Não foi possível enviar email.", me);
		}


	}

}

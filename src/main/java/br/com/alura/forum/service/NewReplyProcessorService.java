package br.com.alura.forum.service;

import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.AnswerRepository;
import br.com.alura.forum.service.infra.ForumEmailService;
import br.com.alura.forum.service.infra.MailServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewReplyProcessorService {

	private static final Logger logger = LoggerFactory.getLogger(NewReplyProcessorService.class);

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private ForumEmailService forumEmailService;

	public void execute(Answer answer) {
		answerRepository.save(answer);

		try {
			this.forumEmailService.sendEmail(answer);
		} catch (MailServiceException e) {
			Topic answeredTopic = answer.getTopic();
			logger.error("Não foi possível notificar o usuário {} enviando email para {}",
					answeredTopic.getOwnerName(), answeredTopic.getOwnerEmail());
		}
	}
}

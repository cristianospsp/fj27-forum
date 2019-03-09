package br.com.alura.forum.service.infra;

import br.com.alura.forum.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class NewMailFactory {

	@Autowired
	private TemplateEngine templateEngine;

	public String generateNewReplyMailContent(Answer answer) {
		Context context = new Context();
		context.setVariable("topicOwnerName", answer.getOwnerName());
		context.setVariable("topicShortDescription", answer.getTopic().getShortDescription());
		context.setVariable("answerAuthor", answer.getOwnerName());
		context.setVariable("answerCreationInstant", getFormattedCreationTime(answer));
		context.setVariable("answerContent", answer.getContent());

		return this.templateEngine.process("email-template.html", context);
	}

	private String getFormattedCreationTime(Answer answer) {
		return DateTimeFormatter.ofPattern("kk:mm")
				.withZone(ZoneId.of("America/Sao_Paulo"))
				.format(answer.getCreationTime());
	}

}

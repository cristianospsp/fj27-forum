package br.com.alura.forum.repository;


import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.Instant;
import java.util.List;

public interface AnswerRepository extends Repository<Answer, Long>, JpaSpecificationExecutor {

	void save(Answer answer);

}

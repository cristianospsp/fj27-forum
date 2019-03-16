package br.com.alura.forum.repository;

import br.com.alura.forum.model.OpenTopicsByCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface OpenTopicsByCategoryRepository extends Repository<OpenTopicsByCategory, Long> {

	void saveAll(Iterable<OpenTopicsByCategory> topics);

	@Query("select t from OpenTopicsByCategory t " +
			"where year(t.date) = year(current_date) " +
			"and month(t.date) = month(current_date)")
	List<OpenTopicsByCategory> findAllByCurrentMonth();

}

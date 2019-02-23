package br.com.alura.forum.repository;


import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends Repository<Topic, Long>, JpaSpecificationExecutor {

	@Query("select t from Topic t")
	List<Topic> list();

	@Query("select t from Topic t where t.course.subcategory = :category")
	List<Topic> findAllByCategory(Category category);
}

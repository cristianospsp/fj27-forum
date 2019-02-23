package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DashBoardItemDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TopicController {

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/*@GetMapping(value = "/api/topics", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch) {
		Specification<Topic> build = topicSearch.build();
		List<Topic> topics = topicRepository.findAll(build);
		return TopicBriefOutputDto.listFromTopics(topics);
	}*/

	@GetMapping(value = "/api/topics", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch, Pageable pageRequest) {
		Specification<Topic> build = topicSearch.build();
		Page<Topic> topics = topicRepository.findAll(build, pageRequest);
		return TopicBriefOutputDto.listFromTopics(topics);
	}


	@GetMapping(value = "/api/topics/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DashBoardItemDto> dasborad() {

		Instant lastWeek = Instant.now().minus(7, ChronoUnit.DAYS);

		List<DashBoardItemDto> collect = categoryRepository
				.findAllByCategoryIsNull().stream()
				.map(category -> {
					List<Topic> topic = topicRepository.findAllByCategory(category);
					return DashBoardItemDto.createNew(category, topic, lastWeek);
				})
				.collect(Collectors.toList());

		return collect;
	}

}

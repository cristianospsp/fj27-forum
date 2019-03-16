package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.DashBoardItemDto;
import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.validator.NewTopicCustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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

	@Autowired
	private CourseRepository courseRepository;


	/*@GetMapping(value = "/api/topics", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch) {
		Specification<Topic> build = topicSearch.build();
		List<Topic> topics = topicRepository.findAll(build);
		return TopicBriefOutputDto.listFromTopics(topics);
	}*/

	@GetMapping(value = "/api/topics", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch,
												@PageableDefault(sort = "creationInstant", direction = Sort.Direction.DESC) Pageable pageRequest) {
		Specification<Topic> build = topicSearch.build();
		Page<Topic> topics = topicRepository.findAll(build, pageRequest);
		return TopicBriefOutputDto.listFromTopics(topics);
	}


	@Cacheable("dashboardData")
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

	@PostMapping(value = "/api/topics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TopicOutputDto> createTopic(@Valid @RequestBody NewTopicInputDto newTopicDto,
											 @AuthenticationPrincipal User loggedUser,
											 UriComponentsBuilder uriBuilder) {

		Topic topic = newTopicDto.build(loggedUser, this.courseRepository);

		this.topicRepository.save(topic);

		URI path = uriBuilder.path("/api/topics/{id}").buildAndExpand(topic.getId()).toUri();

		return ResponseEntity.created(path).body(new TopicOutputDto(topic));
	}

	@InitBinder("newTopicInputDto")
	public void initBinder(WebDataBinder binder, @AuthenticationPrincipal User loggedUser) {
		binder.addValidators(new NewTopicCustomValidator(this.topicRepository, loggedUser));
	}

	@Cacheable(value = "topicDetails", key = "#id")
	@GetMapping(value = "/api/topics/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TopicOutputDto getTopicDetails(@PathVariable Long id) {
		Topic byId = this.topicRepository.findById(id);
		return new TopicOutputDto(byId);
	}

}

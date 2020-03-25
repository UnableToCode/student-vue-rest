/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.hateoas.examples;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Web {@link RestController} used to generate a REST API.
 *
 * @author Greg Turnquist
 */
@RestController
@CrossOrigin
class StudentController {

	private final StudentRepository repository;

	StudentController(StudentRepository repository) {
		this.repository = repository;
	}

	/**
	 * Look up all students, and transform them into a REST collection resource.
	 * Then return them through Spring Web's {@link ResponseEntity} fluent API.
	 */
	@GetMapping("/api/students")
	ResponseEntity<CollectionModel<EntityModel<Student>>> findAll() {

		List<EntityModel<Student>> students = StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(student -> new EntityModel<>(student, //
						linkTo(methodOn(StudentController.class).findOne(student.getId())).withSelfRel(), //
						linkTo(methodOn(StudentController.class).findAll()).withRel("students"))) //
				.collect(Collectors.toList());

		return ResponseEntity.ok( //
				new CollectionModel<>(students, linkTo(methodOn(StudentController.class).findAll()).withSelfRel()));
	}

	@PostMapping("/api/students")
	ResponseEntity<?> newStudent(@RequestBody Student student) {

		try {

			Student savedStudent = repository.save(student);

			EntityModel<Student> studentResource = new EntityModel<>(savedStudent,
					linkTo(methodOn(StudentController.class).findOne(savedStudent.getId())).withSelfRel());

			return ResponseEntity.created(new URI(studentResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
					.body(studentResource);
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to create " + student);
		}
	}

	/**
	 * Look up a single {@link Student} and transform it into a REST resource. Then
	 * return it through Spring Web's {@link ResponseEntity} fluent API.
	 *
	 * @param id
	 */
	@GetMapping("/api/students/{id}")
	ResponseEntity<EntityModel<Student>> findOne(@PathVariable Long id) {

		return repository.findById(id).map(student -> new EntityModel<>(student, //
				linkTo(methodOn(StudentController.class).findOne(student.getId())).withSelfRel(), //
				linkTo(methodOn(StudentController.class).findAll()).withRel("students"))) //
				.map(ResponseEntity::ok) //
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Update existing student then return a Location header.
	 * 
	 * @param student
	 * @param id
	 * @return
	 */
	@PutMapping("/api/students/{id}")
	ResponseEntity<?> updateStudent(@RequestBody Student student, @PathVariable long id) {

		Student studentToUpdate = student;
		studentToUpdate.setId(id);
		repository.save(studentToUpdate);

		Link newlyCreatedLink = linkTo(methodOn(StudentController.class).findOne(id)).withSelfRel();

		try {
			return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
		} catch (URISyntaxException e) {
			return ResponseEntity.badRequest().body("Unable to update " + studentToUpdate);
		}
	}

	/**
	 * Delete existing student then return a success signal.
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/api/students/{id}")
	ResponseEntity<?> deleteStudent(@PathVariable Long id) {
		try {
			if (id == null || repository.findById(id) == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}

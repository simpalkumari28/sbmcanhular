package com.microservices.faculty.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.faculty.jpa.Course;
import com.microservices.faculty.repositories.CourseRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "Course CRUD operations" })
@RestController
@RequestMapping("course")
public class CourseRestController {

	@Autowired
	private CourseRepository courseRepository;

	@ApiOperation("Returns all courses from database")
	@GetMapping("{filter}")
	public Page<Course> getCourses(@PathVariable ("filter") String filter, @PageableDefault(sort = {"id"}) Pageable p, HttpServletRequest request){
		System.out.println("GET Course "+
				" ip: "+request.getRemoteAddr()+
				" datum i vreme pristupa: "+new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss").format(Calendar.getInstance().getTime()));
		if(filter.equals("*"))
			return courseRepository.findAll(p);
		return courseRepository.findByNameContainingIgnoreCaseOrLabelContainingIgnoreCaseOrStartDateContainingIgnoreCase(filter, filter, filter, p);
	}
	
	@ApiOperation("Returns course with an id that is passed as a path variable")
	@GetMapping("one/{id}")
	public Course getOneCourse(@PathVariable("id") int id) {
		return courseRepository.getOne(id);
	}

	// insert
	@ApiOperation("Insert course in database")
	@PostMapping
	public ResponseEntity<Course> insertCourse(@RequestBody Course course) {
		courseRepository.save(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// update
	@ApiOperation("Update course in database")
	@PutMapping
	public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
		if (!courseRepository.existsById(course.getId()))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		courseRepository.save(course);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation("Delete course from database")
	@DeleteMapping("{id}")
	public ResponseEntity<Course> deleteCourse(@PathVariable("id") Integer id) {
		if (!courseRepository.existsById(id))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		courseRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

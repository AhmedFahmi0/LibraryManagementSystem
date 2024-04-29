package com.libraryManagement.libraryManagement.patron.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libraryManagement.libraryManagement.patron.models.request.PatronReqModel;
import com.libraryManagement.libraryManagement.patron.models.response.PatronResModel;
import com.libraryManagement.libraryManagement.patron.services.PatronService;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

@RequestMapping("/api/patrons")

@RestController
public class PatronController {
	@Autowired 
	private PatronService patronService;
	
	
	@ApiOperation("create patron")
	@PostMapping
	public ResponseEntity<PatronResModel> createPatron(@Valid @RequestBody PatronReqModel patronReqModel) {
		return new ResponseEntity<>(patronService.createPatron(patronReqModel), 
      HttpStatus.OK);
		
	}
	

	@ApiOperation("get patron by id")
	@GetMapping("/{id}")
	public ResponseEntity<PatronResModel> getPatronById(
			@PathVariable("id") Long patronId) {
		return new ResponseEntity<>(patronService.getPatronById(patronId), 
				HttpStatus.OK);
	}
	
	@ApiOperation("get patron by id")
	@PutMapping("/{id}")
	public ResponseEntity<PatronResModel> updatePatronById(
			@Valid @RequestBody PatronReqModel patronReqModel,
			@PathVariable("id") Long patronId) {
		return new ResponseEntity<>(patronService.updatePatronById(patronReqModel,patronId), 
				HttpStatus.OK);
	}
	
	
	@ApiOperation("get all patrons")
	@GetMapping
	public ResponseEntity<List<PatronResModel>> getAllPatrons(
			@RequestParam(defaultValue = "10", required = false)  Integer pageSize,
			@RequestParam(defaultValue = "0", required = false)  Integer pageIndex,
			@RequestParam(required = false) String sortField, 
			@RequestParam(required = false) String sortOrder) {
		return new ResponseEntity<>(patronService.getAllPatrons(pageSize,pageIndex,sortField,sortOrder), 
				HttpStatus.OK);
		
	}
	
	
	@ApiOperation("delete patron by id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatronById(
			@PathVariable("id") Long patronId) {
		patronService.deletePatronById(patronId);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
}

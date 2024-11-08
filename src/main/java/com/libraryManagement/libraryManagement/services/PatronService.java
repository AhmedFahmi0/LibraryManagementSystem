package com.libraryManagement.libraryManagement.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.libraryManagement.libraryManagement.model.request.PatronReqModel;
import com.libraryManagement.libraryManagement.projections.PatronProjection;

@Service
public interface PatronService {

	PatronProjection createPatron(PatronReqModel patronReqModel);

	PatronProjection getPatronById(Long patronId);

	PatronProjection updatePatronById(PatronReqModel patronReqModel, Long patronId);

	List<PatronProjection> getAllPatrons(Integer pageSize, Integer pageIndex,String sort, String sortOrder);

	void deletePatronById(Long patronId);

}

package com.libraryManagement.libraryManagement.patron.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.libraryManagement.libraryManagement.patron.models.request.PatronReqModel;
import com.libraryManagement.libraryManagement.patron.models.response.PatronResModel;

@Service
public interface PatronService {

	PatronResModel createPatron(PatronReqModel patronReqModel);

	PatronResModel getPatronById(Long patronId);

	PatronResModel updatePatronById(PatronReqModel patronReqModel, Long patronId);

	List<PatronResModel> getAllPatrons(Integer pageSize, Integer pageIndex,String sort, String sortOrder);

	void deletePatronById(Long patronId);

}

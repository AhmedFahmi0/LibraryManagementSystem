package com.libraryManagement.libraryManagement.patron.models.mapinterface;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.libraryManagement.libraryManagement.patron.entities.Patron;
import com.libraryManagement.libraryManagement.patron.models.request.PatronReqModel;
import com.libraryManagement.libraryManagement.patron.models.response.PatronResModel;



@Mapper(componentModel = "spring")
public interface PatronMapper {
	
	Patron mapToPatron(PatronReqModel patronReqModel);
	
	Patron mapToPatron(@MappingTarget Patron Patron, PatronReqModel patronReqModel);
	
	PatronResModel mapToPatronResModel(Patron Patron);

}

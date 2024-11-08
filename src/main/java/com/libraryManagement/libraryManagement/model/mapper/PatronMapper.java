package com.libraryManagement.libraryManagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.libraryManagement.libraryManagement.entities.Patron;
import com.libraryManagement.libraryManagement.model.request.PatronReqModel;



@Mapper(componentModel = "spring")
public interface PatronMapper {
	
	Patron mapToPatron(PatronReqModel patronReqModel);
	
	Patron mapToPatron(@MappingTarget Patron Patron, PatronReqModel patronReqModel);

}

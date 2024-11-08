package com.libraryManagement.libraryManagement.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.libraryManagement.libraryManagement.entities.BorrowingRecord;
import com.libraryManagement.libraryManagement.model.request.BorrowingRecordReqModel;

@Mapper(componentModel = "spring")
public interface BorrowingRecordMapper {
	
//	@Mapping(source = "book.id", target = "bookId")
//	@Mapping(source = "patron.id", target = "patronId")
//	BorrowingRecordResModel mapToBorrowingRecordResModel(BorrowingRecord borrowingRecord);
	
	BorrowingRecord mapToBorrowingRecord(BorrowingRecordReqModel borrowingRecordReqModel);
	BorrowingRecord mapToBorrowingRecord(@MappingTarget BorrowingRecord borrowingRecord, BorrowingRecordReqModel borrowingRecordReqModel);
}

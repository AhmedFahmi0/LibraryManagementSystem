package com.libraryManagement.libraryManagement.borrowingRecord.models.mapinterface;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.libraryManagement.libraryManagement.borrowingRecord.entites.BorrowingRecord;
import com.libraryManagement.libraryManagement.borrowingRecord.models.request.BorrowingRecordReqModel;
import com.libraryManagement.libraryManagement.borrowingRecord.models.response.BorrowingRecordResModel;

@Mapper(componentModel = "spring")
public interface BorrowingRecordMapper {
	
	@Mapping(source = "book.id", target = "bookId")
	@Mapping(source = "patron.id", target = "patronId")
	BorrowingRecordResModel mapToBorrowingRecordResModel(BorrowingRecord borrowingRecord);
	
	BorrowingRecord mapToBorrowingRecord(BorrowingRecordReqModel borrowingRecordReqModel);
	BorrowingRecord mapToBorrowingRecord(@MappingTarget BorrowingRecord borrowingRecord, BorrowingRecordReqModel borrowingRecordReqModel);
}

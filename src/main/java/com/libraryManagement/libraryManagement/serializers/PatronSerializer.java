package com.libraryManagement.libraryManagement.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.libraryManagement.libraryManagement.entities.Patron;

public class PatronSerializer extends JsonSerializer<Patron>{
	

	@Override
	public void serialize(Patron value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if(value == null) {
			gen.writeNull();
			return;
		}
		gen.writeStartObject();
		gen.writeStringField("name", value.getName());
		gen.writeStringField("mobile", value.getMobile());
		gen.writeStringField("email", value.getEmail());
		gen.writeEndObject();

	}

}

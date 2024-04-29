package com.libraryManagement.libraryManagement.exceptions;

import org.springframework.http.HttpStatus.Series;

public enum CustomHttpStatus {

	WARNING_CODE(220, Series.SUCCESSFUL, "Business Warning Code");

	private final int value;
	private final Series series;
	private final String reasonPhrase;

	CustomHttpStatus(int value, Series series, String reasonPhrase) {
		this.value = value;
		this.series = series;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}

	public Series series() {
		return this.series;
	}

	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	@Override
	public String toString() {
		return this.value + " " + this.name();
	}
}

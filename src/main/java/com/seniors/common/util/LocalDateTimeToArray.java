package com.seniors.common.util;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@NoArgsConstructor
public class LocalDateTimeToArray {

	public static Integer[] convert(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return new Integer[3];
		}
		String dateString = localDateTime.format(DateTimeFormatter.ISO_DATE);

		return Arrays.stream(dateString.split("-"))
				.map(Integer::valueOf)
				.toArray(Integer[]::new);
	}
}
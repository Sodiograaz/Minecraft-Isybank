package dev.sodiograaz.storage.utils;

import lombok.Getter;
import lombok.ToString;

/* @author Sodiograaz
 @since 17/10/2024
*/
@ToString
public enum ResponseType {
	SUCCESS("SUCCESS"), ERROR("NOT FOUND"), NOTHING_HAPPENED("NOTHING HAPPENED");
	private @Getter String name;
	
	ResponseType(String name) {
		this.name = name;
	}
}
package dev.sodiograaz.storage.utils.exceptions;

import java.sql.SQLException;

public class BankNotRegisteredException extends PlayerNotRegisteredException {
	public BankNotRegisteredException(String message) {
		super(message);
	}
}
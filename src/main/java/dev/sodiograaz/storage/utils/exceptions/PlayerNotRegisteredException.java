package dev.sodiograaz.storage.utils.exceptions;

import java.sql.SQLException;

public class PlayerNotRegisteredException extends SQLException
{
	public PlayerNotRegisteredException(String message) {
		super(message);
	}
}
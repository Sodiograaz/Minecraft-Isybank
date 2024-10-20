package dev.sodiograaz.internal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* @author Sodiograaz
 @since 18/10/2024
*/
public class RegexUtils
{
	
	private static final Pattern ONLY_LETTERS_REGEX_NO_SPECIALS = Pattern.compile("[^0-9]", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);
	private static final Pattern ONLY_NUMBERS_REGEX_NO_SPECIALS = Pattern.compile("[^a-zA-Z.]", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);
	private static final Pattern ONLY_NUMBERS_WITH_DECIMALS_REGEX_NO_SPECIALS = Pattern.compile("[^a-zA-Z]", Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);
	private static final Pattern PLAYER_USERNAME_REGEX = Pattern.compile("[^a-zA-Z0-9_]", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.UNICODE_CASE | Pattern.COMMENTS);
	
	public static boolean onlyLettersString(String toCheck)
	{
		Matcher matcher = ONLY_LETTERS_REGEX_NO_SPECIALS.matcher(toCheck);
		return matcher.find();
	}
	
	public static boolean onlyNumbersString(String toCheck)
	{
		Matcher matcher = ONLY_NUMBERS_REGEX_NO_SPECIALS.matcher(toCheck);
		return matcher.find();
	}
	
	public static boolean onlyNumbersWithDecimalsString(String toCheck)
	{
		Matcher matcher = ONLY_NUMBERS_WITH_DECIMALS_REGEX_NO_SPECIALS.matcher(toCheck);
		return matcher.find();
	}
	
	public static boolean checkForPlayerUsernameValidity(String username)
	{
		Matcher matcher = PLAYER_USERNAME_REGEX.matcher(username);
		return matcher.find();
	}
	
}
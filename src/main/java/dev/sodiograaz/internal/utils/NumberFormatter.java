package dev.sodiograaz.internal.utils;

import java.text.DecimalFormat;

/* @author Sodiograaz
 @since 19/10/2024
*/
public class NumberFormatter
{
	
	public static String formatValue(double value)
	{
		String arr[] = {"", "K", "M", "B", "T", "P", "E"};
		int index = 0;
		while ((value / 1000) >= 1)
		{
			value = value / 1000;
			index++;
		}
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return String.format("%s %s", decimalFormat.format(value), arr[index]);
	}

}
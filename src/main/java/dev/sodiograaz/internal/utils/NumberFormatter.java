package dev.sodiograaz.internal.utils;

import java.text.DecimalFormat;

/* @author Sodiograaz
 @since 19/10/2024
*/
public class NumberFormatter
{
	
	public static String formatValue(double value) {
		if (value >= 1_000D) {
			return String.format("%s%s", new DecimalFormat("#.##").format(value), "K");
		}
		else if (value >= 1_000_000D) {
			return String.format("%s%s", new DecimalFormat("#.##").format(value), "M");
		}
		else if(value >= 1_000_000_000D) {
			return String.format("%s%s", new DecimalFormat("#.##").format(value), "B");
		}
		else if(value >= 1_000_000_000_000D)
		{
			return String.format("%s%s", new DecimalFormat("#.##").format(value), "T");
		}
		return String.format("%s", new DecimalFormat("#.##").format(value));
	}

}
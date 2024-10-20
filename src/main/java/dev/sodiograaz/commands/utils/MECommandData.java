package dev.sodiograaz.commands.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* @author Sodiograaz
 @since 18/10/2024
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MECommandData
{
	
	String name();
	String permission() default "isybank.user";
	String[] aliases() default {};
	
}
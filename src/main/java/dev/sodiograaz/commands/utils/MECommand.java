package dev.sodiograaz.commands.utils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.Nullable;

/* @author Sodiograaz
 @since 18/10/2024
*/
public interface MECommand extends CommandExecutor, TabCompleter
{
	
	@Nullable
	default MECommandData getMeCommandData()
	{
		return this.getClass()
				.getAnnotation(MECommandData.class);
	}
	
}
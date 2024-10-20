package dev.sodiograaz.commands;

import dev.sodiograaz.commands.utils.MECommand;
import dev.sodiograaz.commands.utils.MECommandData;
import dev.sodiograaz.internal.IsybankEconomy;
import lombok.SneakyThrows;
import org.reflections.Reflections;

/* @author Sodiograaz
 @since 18/10/2024
*/
public class CommandManager
{
	
	@SneakyThrows
	public static void reflect()
	{
		for(var clazz : new Reflections("dev.sodiograaz.commands.commands").getSubTypesOf(MECommand.class))
		{
			if(clazz.getAnnotation(MECommandData.class) == null) continue;
			MECommand instance = clazz.getConstructor().newInstance();
			MECommandData meCommandData = instance.getMeCommandData();
			IsybankEconomy.getInstance()
					.getCommand(meCommandData.name())
					.setExecutor(instance);
			IsybankEconomy.getInstance()
					.getCommand(meCommandData.name())
					.setTabCompleter(instance);
			
			for(var alias : meCommandData.aliases())
			{
				IsybankEconomy.getInstance()
						.getCommand(alias)
						.setExecutor(instance);
				IsybankEconomy.getInstance()
						.getCommand(alias)
						.setTabCompleter(instance);
			}
		}
	}
	
}
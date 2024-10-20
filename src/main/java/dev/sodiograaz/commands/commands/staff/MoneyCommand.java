package dev.sodiograaz.commands.commands.staff;

import dev.sodiograaz.commands.utils.MECommand;
import dev.sodiograaz.commands.utils.MECommandData;
import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.internal.utils.PlayerServerUtils;
import dev.sodiograaz.internal.utils.RegexUtils;
import dev.sodiograaz.storage.data.BankAccountStaffTransaction;
import dev.sodiograaz.storage.table.managers.BankCashManager;
import dev.sodiograaz.storage.utils.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/* @author Sodiograaz
 @since 18/10/2024
*/
@MECommandData(name = "money", aliases = "eco", permission = "isybank.admin")
public class MoneyCommand implements MECommand
{
	
	private final BankCashManager bankManager = IsybankEconomy.getStorageManager()
			.getBankCashManager();
	
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if(!(commandSender instanceof Player player)) return true;
		if(!player.hasPermission(this.getMeCommandData().permission()))
		{
			player.sendMessage(ConfigurationUtils.NoPermissionComponent(this));
			return true;
		}
		
		if(args.length == 0)
		{
			player.sendMessage(ConfigurationUtils.InvalidCommandComponent());
			return true;
		}
		
		/*
		args:
		 args[0]: size 1 = player
		 args[1]: size 2 = operation
		 args[2]: size 3 = amount
		 */
		
		if(Bukkit.getOnlinePlayers()
				.stream()
				.noneMatch(x -> x.getName().equals(args[0])))
		{
			player.sendMessage(ConfigurationUtils.InvalidCommandComponent());
			return true;
		}
		
		Object searchedPlayer = PlayerServerUtils.searchPlayer(args[0]);
		
		if(!(searchedPlayer instanceof Player target))
		{
			player.sendMessage(ConfigurationUtils.PlayerNotFoundComponent());
			return true;
		}
		
		if(!target.isOnline())
		{
			player.sendMessage(ConfigurationUtils.PlayerNotFoundComponent());
			return true;
		}
		
		String operation = args[1].toLowerCase();
		
		boolean operationHasValidKeywords = operation.equals("give") || operation.equals("set") || operation.equals("remove");
		boolean regexOperationIsValid = RegexUtils.onlyLettersString(operation);
		
		String amount = args[2];
		
		boolean amountHasOnlyNumbersWithDecimals = RegexUtils.onlyNumbersWithDecimalsString(amount);
		
		if(!operationHasValidKeywords || !regexOperationIsValid || !amountHasOnlyNumbersWithDecimals)
		{
			player.sendMessage(ConfigurationUtils.PlayerNotFoundComponent());
			return true;
		}
		
		double amountParsed = Double.parseDouble(amount);
		
		// check if amountParsed is exceding usable numbers
		if(amountParsed >= 9000000000000000000D || (amountParsed < 0))
		{
			player.sendMessage(ConfigurationUtils.ExceedingNumberValue());
			return true;
		}
		
		String targetUsername = target.getName();
		
		switch(operation)
		{
			case "give" ->
			{
				BankAccountStaffTransaction giveResponse = bankManager.give(player.getName(), targetUsername, amountParsed);
				if(giveResponse.getResponseType().equals(ResponseType.ERROR))
				{
					player.sendMessage(ConfigurationUtils.CommandError());
					return true;
				}
			}
			case "remove" -> {
				BankAccountStaffTransaction withdrawResponse = bankManager.remove(player.getName(), targetUsername, amountParsed);
				if (withdrawResponse.getResponseType().equals(ResponseType.ERROR))
				{
					player.sendMessage(ConfigurationUtils.CommandError());
					return true;
				}
			}
				case "set" -> {
					BankAccountStaffTransaction setResponse = bankManager.set(player.getName(), targetUsername, amountParsed);
					if (setResponse.getResponseType().equals(ResponseType.ERROR))
					{
						player.sendMessage(ConfigurationUtils.CommandError());
						return true;
					}
				}
			}
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if(args.length == 1)
			return Bukkit.getOnlinePlayers()
					.stream()
					.map(Player::getName)
					.toList();
		if(args.length == 2)
			return List.of("give", "set", "remove");
		if(args.length == 3)
			return List.of("1000", "2000", "3000");
		return List.of();
	}
}
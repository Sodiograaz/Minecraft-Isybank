package dev.sodiograaz.commands.commands.player;

import dev.sodiograaz.commands.utils.MECommand;
import dev.sodiograaz.commands.utils.MECommandData;
import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.internal.utils.NumberUtils;
import dev.sodiograaz.internal.utils.RegexUtils;
import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.data.BankAccountTransaction;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.table.managers.BankCashManager;
import dev.sodiograaz.storage.table.managers.BankManager;
import dev.sodiograaz.storage.table.managers.PlayerManager;
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
@MECommandData(name = "pay")
public class PayCommand implements MECommand
{
	private final StorageManager storageManager = IsybankEconomy
			.getStorageManager();
	
	private final PlayerManager playerManager = storageManager.getPlayerManager();
	private final BankManager bankManager = storageManager.getBankManager();
	private final BankCashManager bankCashManager = storageManager.getBankCashManager();
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if (!(sender instanceof Player player)) return true;
		if (!player.hasPermission(this.getMeCommandData().permission()))
		{
			player.sendMessage(ConfigurationUtils.NoPermissionComponent(this));
			return true;
		}
		
		if (args.length == 0)
		{
			player.sendMessage(ConfigurationUtils.InvalidCommandComponent());
			return true;
		}
		
		String userId = player.getUniqueId().toString();
		
		if (RegexUtils.checkForPlayerUsernameValidity(args[0]))
		{
			player.sendMessage("Player username validity does not fit.");
			player.sendMessage(ConfigurationUtils.PlayerNotFoundComponent());
			return true;
		}
		
		User target = playerManager.lookupPlayer(args[0]);
		
		String targetUserId = target.getUserId();
		String targetUsername = target.getUsername();
		
		if (userId.equals(targetUserId))
		{
			player.sendMessage(ConfigurationUtils.PayToItself());
			return true;
		}
		
		if (!RegexUtils.onlyNumbersWithDecimalsString(args[1]))
		{
			player.sendMessage(ConfigurationUtils.InvalidCommandComponent());
			return true;
		}
		
		double parsedAmount = Double.parseDouble(args[1]);
		
		// Parsed Amount Checks
		if (parsedAmount < 0 || parsedAmount > NumberUtils.MAX_VALUE_SAFETY)
		{
			player.sendMessage(ConfigurationUtils.ExceedingNumberValue());
			return true;
		}
		
		// Sender bank check
		BankAccount senderBankAccount = bankManager.lookupBankByUserId(userId);
		
		double bankAvailability = senderBankAccount.getBankAvailability();
		
		if (parsedAmount > bankAvailability)
		{
			player.sendMessage(ConfigurationUtils.NotEnoughMoney());
			return true;
		}
		
		BankAccount targetBankAccount = bankManager.lookupBankByUserId(targetUserId);
		
		double targetBankAccountBankAvailability = targetBankAccount.getBankAvailability();
		
		if ((targetBankAccountBankAvailability + parsedAmount) > NumberUtils.MAX_VALUE_SAFETY)
		{
			player.sendMessage(ConfigurationUtils.ExceedingNumberValue());
			return true;
		}
		
		BankAccountTransaction response = bankCashManager.pay(player.getName(), targetUsername, parsedAmount, "");
		if (response.getResponseType().equals(ResponseType.ERROR))
		{
			player.sendMessage(ConfigurationUtils.InvalidCommandComponent());
			return true;
		}
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
	{
		if(!(sender instanceof Player player)) return List.of();
		if(args.length == 1)
			return Bukkit.getOnlinePlayers()
					.stream()
					.map(Player::getName)
					.toList();
		if(args.length == 2)
		{
			BankAccount bank = bankManager.lookupBankByUserId(player.getUniqueId().toString());
			return List.of(String.valueOf(bank.getBankAvailability()));
		}
		return List.of();
	}
}
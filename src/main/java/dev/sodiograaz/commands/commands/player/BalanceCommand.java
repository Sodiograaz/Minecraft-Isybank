package dev.sodiograaz.commands.commands.player;

import dev.sodiograaz.commands.utils.MECommand;
import dev.sodiograaz.commands.utils.MECommandData;
import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.table.managers.BankManager;
import dev.sodiograaz.storage.table.managers.PlayerManager;
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
@MECommandData(name = "balance", aliases = {
		"bal"
})
public class BalanceCommand implements MECommand
{
	
	private final PlayerManager playerManager = IsybankEconomy
			.getStorageManager()
			.getPlayerManager();
	private final BankManager bankManager = IsybankEconomy.getStorageManager()
			.getBankManager();
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player player)) return true;
		if(!player.hasPermission(this.getMeCommandData().permission()))
		{
			player.sendMessage(ConfigurationUtils.NoPermissionComponent(this));
			return true;
		}
		
		// Check if is checking someone's account
		if(args.length == 1)
		{
			if(!player.hasPermission("isybank.user.others"))
			{
				player.sendMessage(ConfigurationUtils.NoPermissionComponent(this));
				return true;
			}
			
			User user = playerManager.lookupPlayer(args[0]);
			String userId = user.getUserId();
			if(userId.isEmpty() || userId.equals("NOT_FOUND"))
			{
				player.sendMessage(ConfigurationUtils.PlayerNotFoundComponent());
				return true;
			}
			
			BankAccount bank = bankManager.lookupBankByUserId(userId);
			player.sendMessage(ConfigurationUtils.BalanceOthers(args[0], bank.getBankAvailability()));
			return true;
		}
		
		BankAccount bank = bankManager.lookupBankByUserId(player.getUniqueId().toString());
		player.sendMessage(ConfigurationUtils.BalanceSelf(bank.getBankAvailability()));
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(args.length == 1)
			return Bukkit.getOnlinePlayers()
					.stream()
					.map(Player::getName)
					.toList();
		return List.of();
	}
}
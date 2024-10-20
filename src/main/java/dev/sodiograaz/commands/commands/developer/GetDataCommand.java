package dev.sodiograaz.commands.commands.developer;

import dev.sodiograaz.commands.utils.MECommand;
import dev.sodiograaz.commands.utils.MECommandData;
import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.data.BankAccountTransaction;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.table.managers.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/* @author Sodiograaz
 @since 20/10/2024
*/
@MECommandData(name = "data", permission = "isybank.admin")
public class GetDataCommand implements MECommand {
	
	private final StorageManager storageManager = IsybankEconomy.getStorageManager();
	private final PlayerManager playerManager = storageManager.getPlayerManager();
	private final BankManager bankManager = storageManager.getBankManager();
	private final PaymentsManager paymentsManager = storageManager.getPaymentsManager();
	private final PaymentsStaffManager paymentsStaffManager = storageManager.getPaymentsStaffManager();
	private final BankCashManager bankCashManager = storageManager.getBankCashManager();
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player player)) return true;
		if(!player.hasPermission(this.getMeCommandData().permission()))
		{
			player.sendMessage(ConfigurationUtils.NoPermissionComponent(this));
			return true;
		}
		
		if(args.length == 0)
		{
			player.sendMessage(ConfigurationUtils.CommandError());
			return true;
		}
		
		if(!args[0].equals("get"))
		{
			player.sendMessage(ConfigurationUtils.CommandError());
			return true;
		}
		
		// args[1] = player returned from the database
		
		switch(args[2])
		{
			case "player" ->
			{
				User user = this.playerManager.lookupPlayer(args[1]);
				player.sendMessage(user.toString());
			}
			case "bank" ->
			{
				User user = this.playerManager.lookupPlayer(args[1]);
				BankAccount bankAccount = this.bankManager.lookupBankByUserId(user.getUserId());
				player.sendMessage(bankAccount.toString());
			}
		}
		
		return true;
	}
	
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(args.length == 1) // 0
			return List.of("get");
		if(args.length == 2) // 1
			return this.playerManager.getAllPlayers()
					.stream()
					.map(User::getUsername)
					.toList();
		if(args.length == 3) // 2
			return List.of("player", "bank", "transactions", "transaction");
		if(args.length == 4 && args[2].equals("transaction")) // 3
			return this.paymentsManager.lookupTransactionsByHolderUsername(args[1])
					.stream()
					.map(BankAccountTransaction::getPaymentId)
					.toList();
		return List.of();
	}
}
package dev.sodiograaz.listeners.listeners;

import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.table.managers.BankManager;
import dev.sodiograaz.storage.table.managers.PlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/* @author Sodiograaz
 @since 18/10/2024
*/
public class PlayerListeners implements Listener
{
	
	private final StorageManager storageManager = IsybankEconomy.getStorageManager();
	private final PlayerManager playerManager = storageManager.getPlayerManager();
	private final BankManager bankManager = storageManager.getBankManager();
	
	private final double bankAvailabilityInitial = ConfigurationUtils.INITIAL_BALANCE;
	
	// On join create bank
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		String name = player.getName();
		String userId = player.getUniqueId().toString();
		if (playerManager.playerAlreadyRegistered(userId) && bankManager.bankAlreadyRegistered(userId)) {
			return;
		}
		User user = this.playerManager.createPlayer(name, userId);
		String bankId = UUID.randomUUID().toString();
		this.bankManager.createBank(user.getUserId(), bankId, bankAvailabilityInitial);
	}
	
	// Check if player has duplicate
	@EventHandler
	public void onPlayerJoinCheckDuplicate(AsyncPlayerPreLoginEvent event)
	{
		UUID userId = event.getUniqueId();
		String userIdS = userId.toString();
		String profileName = event.getPlayerProfile().getName();
		if (!this.playerManager.playerAlreadyRegistered(userIdS))
			return;
		
		// Check the duplicate (NEW USERID) == (OLD USERID)
		User user = this.playerManager.lookupPlayer(profileName);
		String userIdResult = user.getUserId();
		if (!userIdResult.equals(userIdS))
		{
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text("<red>Kicked for duplicate id's in database, please contact the administrator</red>"));
		}
	}
	
}
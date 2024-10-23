package dev.sodiograaz.listeners.listeners;

import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.table.managers.BankManager;
import dev.sodiograaz.storage.table.managers.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
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
		
		/**
		 * Check the duplicate
		 * If userId from database and userId from player are equals but username from database and username from player are not then disallow
		 */
		User user = this.playerManager.lookupPlayer(profileName);
		
		if(user.getUserId().equals(userIdS) && !profileName.equals(user.getUsername()))
		{
			TextComponent textComponent = Component.text("Detected duplicate ids in database. Please contact the administrator")
					.hoverEvent(HoverEvent.showText(Component.text(String.format("Found UUID [%s] for %s\nYour UUID [%s] for %s\nBoth UUID are equals? %s\nBoth usernames are equals? %s",
							user.getUserId(), user.getUsername(), userIdS, profileName, userIdS.equals(user.getUserId()), profileName.equals(user.getUsername())))));
			
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, textComponent);
		}
		
	}
	
}
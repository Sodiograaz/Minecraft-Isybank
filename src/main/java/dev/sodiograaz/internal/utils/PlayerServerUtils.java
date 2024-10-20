package dev.sodiograaz.internal.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

/* @author Sodiograaz
 @since 18/10/2024
*/
public class PlayerServerUtils
{
	
	private static OfflinePlayer searchOfflinePlayerIfCached(String username)
	{
		List<OfflinePlayer> convert = List.of(Bukkit.getOfflinePlayers());
		List<OfflinePlayer> filtered = convert.stream()
				.filter(x -> x.getName().equals(username))
				.toList();
		return filtered.stream().findFirst()
				.orElseThrow(() ->
						new RuntimeException(String.format("Player %s Not Found", username)));
	}
	
	public static Object searchPlayer(String username)
	{
		OfflinePlayer player = searchOfflinePlayerIfCached(username);
		return player.isOnline() ? player.getPlayer() : player;
	}
	
}
package dev.sodiograaz.internal.utils;

import dev.sodiograaz.commands.utils.MECommand;
import dev.sodiograaz.internal.IsybankEconomy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

/* @author Sodiograaz
 @since 18/10/2024
*/
public class ConfigurationUtils
{
	// Storage Section
	private static final ConfigurationSection storageSection = IsybankEconomy
			.getInstance()
			.getConfig()
			.getConfigurationSection("Storage");
	
	public static final String Host = storageSection.getString("Host");
	public static final String Port = storageSection.getString("Host");
	public static final String Username = storageSection.getString("Host");
	public static final String Password = storageSection.getString("Host");
	public static final String Database = storageSection.getString("Host");
	public static final Map<String,Object> UrlParameters = storageSection.getObject("UrlParameters", Map.class);
	
	// Messages Section
	private static final ConfigurationSection messagesSection = IsybankEconomy
			.getInstance()
			.getConfig()
			.getConfigurationSection("Messages");
	
	// Plugin Prefix
	private static final String PLUGIN_PREFIX = messagesSection.getString("Prefix");
	
	// Economy Prefixes
	private static final ConfigurationSection ECONOMY_PREFIX = messagesSection.getConfigurationSection("Economy-Prefix");
	public static final String ECONOMY_PREFIX_SINGULAR = ECONOMY_PREFIX.getString("Letters-Singular");
	public static final String ECONOMY_PREFIX_PLURAL = ECONOMY_PREFIX.getString("Letters-Plural");
	public static final String ECONOMY_PREFIX_ACRONYM = ECONOMY_PREFIX.getString("Letters-Acronym");
	public static final String ECONOMY_PREFIX_UNICODE = ECONOMY_PREFIX.getString("Unicode");
	
	// General Messages
	private static String NO_PERMISSION(String permission)
	{
		return messagesSection.getString("NoPermission")
				.replaceAll("%permission%", permission);
	}
	
	public static Component NoPermissionComponent(String permission)
	{
		return deserialize(NO_PERMISSION(permission));
	}
	
	public static Component NoPermissionComponent(MECommand command)
	{
		return deserialize(NO_PERMISSION(command.getMeCommandData().permission()));
	}
	
	private static String PLAYER_NOT_FOUND()
	{
		return messagesSection.getString("PlayerNotFound");
	}
	
	public static Component PlayerNotFoundComponent()
	{
		return deserialize(PLAYER_NOT_FOUND());
	}
	
	private static String INVALID_COMMAND()
	{
		return messagesSection.getString("InvalidCommand");
	}
	
	public static Component InvalidCommandComponent()
	{
		return deserialize(INVALID_COMMAND());
	}
	
	private static String COMMAND_ERROR()
	{
		return messagesSection.getString("CommandError");
	}
	
	public static Component CommandError()
	{
		return deserialize(COMMAND_ERROR());
	}
	
	private static String EXCEEDING_NUMBER_VALUE()
	{
		return messagesSection.getString("ExceedingLimitNumbers");
	}
	
	public static Component ExceedingNumberValue()
	{
		return deserialize(EXCEEDING_NUMBER_VALUE());
	}
	
	private static String NEGATIVE_NUMBER_VALUE()
	{
		return messagesSection.getString("NegativeNumber");
	}
	
	public static Component NegativeNumberValue()
	{
		return deserialize(NEGATIVE_NUMBER_VALUE());
	}
	
	// Player-To-Player Operations
	private static final ConfigurationSection p2p = messagesSection.getConfigurationSection("P2P-Operations");
	
	private static String PAY_SOMEONE(String target, double amount)
	{
		return p2p.getString("Pay-Someone")
				.replaceAll("%target%", target)
				.replace("%amount%", NumberFormatter.formatValue(amount));
	}
	
	public static Component PaySomeone(String target, double amount)
	{
		return deserializeEconomicOperations(PAY_SOMEONE(target, amount));
	}
	
	private static String SOMEONE_PAID(String sender, double amount)
	{
		return p2p.getString("Someone-Paid")
				.replaceAll("%sender%", sender)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	public static Component SomeonePaid(String sender, double amount)
	{
		return deserializeEconomicOperations(SOMEONE_PAID(sender, amount));
	}
	
	private static String PAY_TO_ITSELF()
	{
		return p2p.getString("Pay-To-ItSelf");
	}
	
	public static Component PayToItself()
	{
		return deserializeEconomicOperations(PAY_TO_ITSELF());
	}
	
	// Bank Operations
	private static final ConfigurationSection bankOperations = messagesSection.getConfigurationSection("Bank-Operations");
	
	private static String GIVE_OPERATION(String target, double amount)
	{
		return bankOperations.getString("Give")
				.replaceAll("%player%", target)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	private static String GIVE_OPERATION(Player target, double amount)
	{
		return GIVE_OPERATION(target.getName(), amount);
	}
	
	public static Component GiveOperation(Player target, double amount)
	{
		return deserializeEconomicOperations(GIVE_OPERATION(target, amount));
	}
	
	public static Component GiveOperation(String target, double amount)
	{
		return deserializeEconomicOperations(GIVE_OPERATION(target, amount));
	}
	
	private static String GIVE_OTHERS_OPERATION(String sender, double amount)
	{
		return bankOperations.getString("Give-Others")
				.replaceAll("%player%", sender)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	private static String GIVE_OTHERS_OPERATION(Player sender, double amount)
	{
		return GIVE_OTHERS_OPERATION(sender.getName(), amount);
	}
	
	public static Component GiveOthersOperation(Player sender, double amount)
	{
		return deserializeEconomicOperations(GIVE_OTHERS_OPERATION(sender, amount));
	}
	
	public static Component GiveOthersOperation(String sender, double amount)
	{
		return deserializeEconomicOperations(GIVE_OTHERS_OPERATION(sender, amount));
	}
	
	private static String REMOVE_OTHERS_OPERATION(String sender, double amount)
	{
		return bankOperations.getString("Give-Others")
				.replaceAll("%player%", sender)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	private static String REMOVE_OTHERS_OPERATION(Player sender, double amount)
	{
		return REMOVE_OTHERS_OPERATION(sender.getName(), amount);
	}
	
	public static Component RemoveOthersOperation(Player sender, double amount)
	{
		return deserializeEconomicOperations(REMOVE_OTHERS_OPERATION(sender, amount));
	}
	
	public static Component RemoveOthersOperation(String sender, double amount)
	{
		return deserializeEconomicOperations(REMOVE_OTHERS_OPERATION(sender, amount));
	}
	
	private static String SET_OTHERS_OPERATION(Player sender, double amount)
	{
		return bankOperations.getString("Give-Others")
				.replaceAll("%player%", sender.getName())
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	public static Component SetOthersOperation(Player sender, double amount)
	{
		return deserializeEconomicOperations(SET_OTHERS_OPERATION(sender, amount));
	}
	
	
	private static String REMOVE_OPERATION(String target, double amount)
	{
		return bankOperations.getString("Remove")
				.replaceAll("%player%", target)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	private static String REMOVE_OPERATION(Player target, double amount)
	{
		return REMOVE_OPERATION(target.getName(), amount);
	}
	
	public static Component RemoveOperation(Player target, double amount)
	{
		return deserializeEconomicOperations(REMOVE_OPERATION(target.getName(), amount));
	}
	
	public static Component RemoveOperation(String target, double amount)
	{
		return deserializeEconomicOperations(REMOVE_OPERATION(target, amount));
	}
	
	
	private static String SET_OPERATION(String target, double amount)
	{
		return bankOperations.getString("Set")
				.replaceAll("%player%", target)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	private static String SET_OPERATION(Player target, double amount)
	{
		return bankOperations.getString("Set")
				.replaceAll("%player%", target.getName())
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	public static Component SetOperation(Player target, double amount)
	{
		return deserializeEconomicOperations(SET_OPERATION(target.getName(), amount));
	}
	
	public static Component SetOperation(String target, double amount)
	{
		return deserializeEconomicOperations(SET_OPERATION(target, amount));
	}
	
	private static ConfigurationSection bankMessages = messagesSection.getConfigurationSection("Bank-Messages");
	
	private static String BALANCE_SELF(double amount)
	{
		return bankMessages.getString("Balance-Self")
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	public static Component BalanceSelf(double amount)
	{
		return deserializeEconomicOperations(BALANCE_SELF(amount));
	}
	
	private static String BALANCE_OTHERS(String player, double amount)
	{
		return bankMessages.getString("Balance-Others")
				.replaceAll("%player%", player)
				.replaceAll("%amount%", NumberFormatter.formatValue(amount));
	}
	
	public static Component BalanceOthers(String player, double amount)
	{
		return deserializeEconomicOperations(BALANCE_OTHERS(player, amount));
	}
	
	// Bank Errors Section
	private static final ConfigurationSection bankErrorsSection = bankMessages.getConfigurationSection("Errors");
	
	private static String NOT_ENOUGH_MONEY()
	{
		return bankErrorsSection.getString("NotEnoughMoney");
	}
	
	public static Component NotEnoughMoney()
	{
		return deserialize(NOT_ENOUGH_MONEY());
	}
	
	private static String BANK_IS_DISABLED_FOR_SAFETY_MEASURES()
	{
		return bankErrorsSection.getString("BankIsDisabledForSafetyMeasures");
	}
	
	public static Component BankIsDisabledForSafetyMeasures()
	{
		return deserialize(BANK_IS_DISABLED_FOR_SAFETY_MEASURES());
	}
	
	private static String BANK_PAYMENTS_ARE_DISABLED(String player)
	{
		return bankErrorsSection.getString("BANK_PAYMENTS_ARE_DISABLED")
				.replaceAll("%player%", player);
	}
	
	public static Component BankPaymentsAreDisabled(String player)
	{
		return deserialize(BANK_PAYMENTS_ARE_DISABLED(player));
	}
	
	// Economy Section
	private static final ConfigurationSection economySection = IsybankEconomy
			.getInstance()
			.getConfig()
			.getConfigurationSection("Economy");
	
	public static double INITIAL_BALANCE = economySection.getDouble("Initial-Balance");
	
	// Utils
	private static Component deserializeEconomicOperations(String input)
	{
		return deserialize(input
				.replaceAll("%economy_prefix_singular%", ECONOMY_PREFIX_SINGULAR)
				.replaceAll("%economy_prefix_plural%", ECONOMY_PREFIX_PLURAL)
				.replaceAll("%economy_prefix_acronym%", ECONOMY_PREFIX_ACRONYM)
				.replaceAll("%economy_prefix_unicode%", ECONOMY_PREFIX_UNICODE));
	}
	
	
	private static Component deserialize(String input)
	{
		return MiniMessage.miniMessage().deserialize(input
				.replaceAll("%prefix%", PLUGIN_PREFIX));
	}
	
}
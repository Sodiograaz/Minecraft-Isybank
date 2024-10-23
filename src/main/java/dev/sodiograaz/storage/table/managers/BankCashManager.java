package dev.sodiograaz.storage.table.managers;

import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.data.BankAccountStaffTransaction;
import dev.sodiograaz.storage.data.BankAccountTransaction;
import dev.sodiograaz.storage.data.User;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/* @author Sodiograaz
 @since 20/10/2024
*/
public class BankCashManager
{
	private final Connection connection;
	private final PlayerManager playerManager;
	private final BankManager bankManager;
	private final PaymentsManager paymentsManager;
	private final PaymentsStaffManager paymentsStaffManager;
	
	@SneakyThrows
	public BankCashManager(StorageManager storageManager)
	{
		this.connection = storageManager.getHikariDataSource().getConnection();;
		this.playerManager = storageManager.getPlayerManager();
		this.bankManager = storageManager.getBankManager();
		this.paymentsManager = storageManager.getPaymentsManager();
		this.paymentsStaffManager = storageManager.getPaymentsStaffManager();
	}
	
	/*
	OPERATIONS OF THE BANK:
		STAFF:
	    - give (String, Double)
	    - remove (String, Double)
	    - set (String, Double)
	  USER:
	    - pay (User, User, Double, String)
	 */
	
	/**
	 * Gives money from input to target
	 * @param moderator The moderator
	 * @param username The target
	 * @param money Money to give
	 * @return {@link BankAccountStaffTransaction}
	 */
	public BankAccountStaffTransaction give(String moderator, String username, double money)
	{
		User user = this.playerManager.lookupPlayer(username);
		String userId = user.getUserId();
		BankAccount bankAccount = this.bankManager.lookupBankByUserId(userId);
		
		double bankAvailability = bankAccount.getBankAvailability();
		double bankAfterGive = bankAvailability + money;
		
		try (PreparedStatement statement = this.connection.prepareStatement("UPDATE users_bank_data SET bank_availability = ? WHERE bankId = ?;")) {
			statement.setDouble(1, bankAfterGive);
			statement.setString(2, bankAccount.getBankId());
			statement.executeUpdate();
		}
		catch (SQLException exception) {}
		
		return this.paymentsStaffManager.createStaffTransaction(moderator, username, money);
	}
	
	/**
	 * Sets money from input to target
	 * @param moderator The moderator username
	 * @param username The target username
	 * @param money The money to set
	 * @return {@link BankAccountStaffTransaction}
	 */
	public BankAccountStaffTransaction set(String moderator, String username, double money)
	{
		// Target
		User user = this.playerManager.lookupPlayer(username);
		String userId = user.getUserId();
		BankAccount bankAccount = this.bankManager.lookupBankByUserId(userId);
		
		// update the account
		try (PreparedStatement statement = this.connection.prepareStatement("UPDATE users_bank_data SET bank_availability = ? WHERE bankId = ?;"))
		{
			statement.setDouble(1, money);
			statement.setString(2, bankAccount.getBankId());
			statement.executeUpdate();
		}
		catch (SQLException exception) {}
		return this.paymentsStaffManager.createStaffTransaction(moderator, username, money);
	}
	
	/**
	 * Removes money from target
	 * @param moderator The moderator username
	 * @param username The target username
	 * @param money Money to give
	 * @return {@link BankAccountStaffTransaction}
	 */
	public BankAccountStaffTransaction remove(String moderator, String username, double money)
	{
		User user = this.playerManager.lookupPlayer(username);
		String userId = user.getUserId();
		BankAccount bankAccount = this.bankManager.lookupBankByUserId(userId);
		
		double bankAvailability = bankAccount.getBankAvailability();
		double bankAfterGive = bankAvailability - money;
		
		// update the account
		try (PreparedStatement statement = this.connection.prepareStatement("UPDATE users_bank_data SET bank_availability = ? WHERE bankId = ?;"))
		{
			statement.setDouble(1, bankAfterGive);
			statement.setString(2, bankAccount.getBankId());
			statement.executeUpdate();
		}
		catch (SQLException exception) {}
		
		return this.paymentsStaffManager.createStaffTransaction(moderator, username, money);
	}
	
	/**
	 * Removes money from holder
	 *
	 * @param holderUsername Who pays
	 * @param money          The money to remove
	 */
	public void removeFromUser(String holderUsername, double money)
	{
		User holder = this.playerManager.lookupPlayer(holderUsername);
		String holderUserId = holder.getUserId();
		BankAccount holderBank = this.bankManager.lookupBankByUserId(holderUserId);
		String holderBankId = holderBank.getBankId();
		
		double moneyToSet = holderBank.getBankAvailability() - money;
		
		try (PreparedStatement statement = this.connection.prepareStatement("UPDATE users_bank_data SET bank_availability = ? WHERE bankId = ?;"))
		{
			statement.setDouble(1, moneyToSet);
			statement.setString(2, holderBankId);
			statement.executeUpdate();
		} catch (SQLException exception) {}
	}
	
	/**
	 * Adds money to holder
	 * @param payeeUsername The holder
	 * @param money The money to add
	 * @return The holder availability after adding the amount {@link BankAccountTransaction}
	 */
	public void addToUser(String payeeUsername, double money)
	{
		User holder = this.playerManager.lookupPlayer(payeeUsername);
		String payeeUserId = holder.getUserId();
		BankAccount payeeBank = this.bankManager.lookupBankByUserId(payeeUserId);
		String payeeBankId = payeeBank.getBankId();
		
		double moneyToSet = payeeBank.getBankAvailability() + money;
		
		try (PreparedStatement statement = this.connection.prepareStatement("UPDATE users_bank_data SET bank_availability = ? WHERE bankId = ?;"))
		{
			statement.setDouble(1, moneyToSet);
			statement.setString(2, payeeBankId);
			
			statement.executeUpdate();
		} catch (SQLException exception) {}

	}
	
	/**
	 * Pays someone
	 * @param holder Who pays
	 * @param payee Who gets paid
	 * @param money The money paid
	 * @return The holder transaction {@link BankAccountTransaction}
	 */
	public BankAccountTransaction pay(String holder, String payee, double money, String description)
	{
		// Check holder then remove money from holder
		OfflinePlayer holderOfflinePlayer = Bukkit.getOfflinePlayer(holder);
		if(holderOfflinePlayer != null && holderOfflinePlayer.isOnline())
		{
			Player player = holderOfflinePlayer.getPlayer();
			player.sendMessage(ConfigurationUtils.PaySomeone(payee, money));
		}
		this.removeFromUser(holder, money);
		
		// Add money to payee
		this.addToUser(payee, money);
		if(Bukkit.getOfflinePlayer(payee) != null && Bukkit.getOfflinePlayer(payee).isOnline())
		{
			Player player = Bukkit.getOfflinePlayer(payee).getPlayer();
			player.sendMessage(ConfigurationUtils.SomeonePaid(holder, money));
		}
		
		return this.paymentsManager.createTransaction(holder, payee, money, description);
	}
	
}
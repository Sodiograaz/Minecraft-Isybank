package dev.sodiograaz.storage.table.managers;

import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccountStaffTransaction;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.utils.ResponseType;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/* @author Sodiograaz
 @since 20/10/2024
*/
public class PaymentsStaffManager
{
	
	private final Connection connection;
	private final PlayerManager playerManager;
	
	@SneakyThrows
	public PaymentsStaffManager(StorageManager storageManager)
	{
		this.connection = storageManager.getHikariDataSource()
				.getConnection();
		this.playerManager = storageManager.getPlayerManager();
	}
	
	public BankAccountStaffTransaction createStaffTransaction(String moderatorUsername, String modifiedUserUsername, double bill)
	{
		User moderatorUser = this.playerManager.lookupPlayer(moderatorUsername);
		User modifiedUser = this.playerManager.lookupPlayer(modifiedUserUsername);
		
		String moderatorId = moderatorUser.getUserId();
		String modifiedUserId = modifiedUser.getUserId();
		
		String paymentId = UUID.randomUUID().toString();
		
		try(PreparedStatement statement = this.connection.prepareStatement("INSERT INTO users_bank_payments_staff (moderator, modifiedUser, paymentId, bill) VALUES (?,?,?,?);"))
		{
			statement.setString(1, moderatorId);
			statement.setString(2, modifiedUserId);
			statement.setString(3, paymentId);
			statement.setDouble(4, bill);
			statement.execute();
		}
		catch (SQLException exception) {
			Bukkit.getLogger().severe(exception.getLocalizedMessage());
		}
		return lookupTransactionByModeratorUsernameAndPaymentId(moderatorUsername, paymentId);
	}
	
	public BankAccountStaffTransaction lookupTransactionByModeratorUsernameAndPaymentId(String moderatorUsername, String paymentId)
	{
		User moderatorUser = this.playerManager.lookupPlayer(moderatorUsername);
		
		try (PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users_bank_payments_staff WHERE moderator = ? AND paymentId = ?;"))
		{
			statement.setString(1, moderatorUser.getUserId());
			statement.setString(2, paymentId);
			ResultSet query = statement.executeQuery();
			if (query.next())
				return BankAccountStaffTransaction.builder()
						.moderator(moderatorUser)
						.modifiedUser(this.playerManager.lookupPlayerByUserId(query.getString("modifiedUser")))
						.bill(query.getDouble("bill"))
						.responseType(ResponseType.SUCCESS)
						.build();
		}
		catch (SQLException exception) {
			Bukkit.getLogger().severe(exception.getLocalizedMessage());
		}
		
		return BankAccountStaffTransaction.emptyBankAccountStaffTransactionWithError();
	}
	
	public List<BankAccountStaffTransaction> lookupTransactionsByModeratorUsernameAndPaymentId(String moderatorUsername)
	{
		User moderatorUser = this.playerManager.lookupPlayer(moderatorUsername);
		
		List<BankAccountStaffTransaction> result = new LinkedList<>();
		
		try (PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users_bank_payments_staff WHERE moderator = ?;"))
		{
			statement.setString(1, moderatorUser.getUserId());
			ResultSet query = statement.executeQuery();
			while(query.next())
				result.add(BankAccountStaffTransaction.builder()
						.moderator(moderatorUser)
						.modifiedUser(this.playerManager.lookupPlayerByUserId(query.getString("modifiedUser")))
						.bill(query.getDouble("bill"))
						.responseType(ResponseType.SUCCESS)
						.build());
		}
		catch (SQLException ignored) {}
		return result;
	}
	
	public boolean transactionAlreadyExists(String moderatorId, String paymentId)
	{
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) FROM users_bank_payments_staff WHERE paymentId = ?;"))
		{
			statement.setString(1, moderatorId);
			statement.setString(2, paymentId);
			ResultSet query = statement.executeQuery();
			if(query.next())
				return query.getInt("COUNT(*)") > 0;
		}
		catch (SQLException ignored) {}
		return false;
	}
	
}
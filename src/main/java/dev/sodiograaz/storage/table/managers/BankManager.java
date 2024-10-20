package dev.sodiograaz.storage.table.managers;

import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.data.User;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/* @author Sodiograaz
 @since 17/10/2024
*/
public class BankManager 
{
	private final Connection connection;
	private final PlayerManager playerManager;
	
	@SneakyThrows
	public BankManager(StorageManager storageManager)
	{
		this.connection = storageManager.getHikariDataSource()
				.getConnection();
		this.playerManager = storageManager.getPlayerManager();
	}
	
	public BankAccount createBank(String userId, String bankId, double bankAvailability)
	{
		if(bankAlreadyRegistered(userId)) return BankAccount.emptyBankAccountWithError();
		User user = this.playerManager.lookupPlayerByUserId(userId);
		synchronized (this.connection)
		{
			try(PreparedStatement statement = this.connection.prepareStatement("INSERT INTO users_bank_data (userId, bank_availability, bankId) VALUES (?,?,?);"))
			{
				statement.setString(1, user.getUserId());
				statement.setDouble(2, bankAvailability);
				statement.setString(3, bankId);
				statement.execute();
			}
			catch (SQLException ignored) {}
		}
		return lookupBankByUserId(userId);
	}
	
	public BankAccount lookupBankByUserId(String userId) {
		if(!bankAlreadyRegistered(userId)) return BankAccount.emptyBankAccountWithError();
		User user = this.playerManager.lookupPlayerByUserId(userId);
		synchronized (this.connection)
		{
			try (PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users_bank_data WHERE userId = ?;"))
			{
				statement.setString(1, userId);
				ResultSet query = statement.executeQuery();
				if(query.next())
					return BankAccount.builder()
							.holder(user)
							.bankId(query.getString("bankId"))
							.bankAvailability(query.getLong("bank_availability"))
							.build();
			}
			catch (SQLException ignored) {}
		}
		
		return BankAccount.emptyBankAccountWithError();
	}
	
	
	public boolean bankAlreadyRegistered(String userId)
	{
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) FROM users_bank_data WHERE userId = ?;"))
		{
			statement.setString(1, userId);
			ResultSet query = statement.executeQuery();
			if(query.next())
				return query.getInt("COUNT(*)") > 0;
		}
		catch (SQLException ignored) {}
		return false;
	}
	
	
}
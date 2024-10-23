package dev.sodiograaz.storage.table.managers;

import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.data.BankAccountTransaction;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.utils.ResponseType;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/* @author Sodiograaz
 @since 19/10/2024
*/
public class PaymentsManager
{
	
	private final Connection connection;
	private final PlayerManager playerManager;
	private final BankManager bankManager;
	
	@SneakyThrows
	public PaymentsManager(StorageManager storageManager)
	{
		this.connection = storageManager.getHikariDataSource().getConnection();
		this.playerManager = storageManager.getPlayerManager();
		this.bankManager = storageManager.getBankManager();
	}
	
	
	/**
	 * Creates a transaction upon someone pays someone
	 * @param holderUsername The who pays username
	 * @param payeeUsername The who gets paid username
	 * @param gross_bill The amount paid (gross amount)
	 * @param description The description for the transaction
	 */
	public BankAccountTransaction createTransaction(String holderUsername, String payeeUsername, double gross_bill, String description)
	{
		// PaymentId
		String paymentId = UUID.randomUUID().toString();
		
		if(transactionAlreadyExists(paymentId)) return BankAccountTransaction.emptyBankAccountTransactionWithError();
		
		// Prepare the statement
		try(PreparedStatement statement = this.connection.prepareStatement(
				"INSERT INTO users_bank_payments (holder, payee, paymentId, transaction, transaction_description) VALUES (?,?,?,?,?);"))
		{
			// HolderUserID Lookup
			User holder = this.playerManager.lookupPlayer(holderUsername);
			BankAccount holderBank = this.bankManager.lookupBankByUserId(holder.getUserId());
			String holderBankId = holderBank.getBankId();
			
			// PayeeUserID Lookup
			User payee = this.playerManager.lookupPlayer(payeeUsername);
			BankAccount payeeBank = this.bankManager.lookupBankByUserId(payee.getUserId());
			String payeeBankId = payeeBank.getBankId();
			
			statement.setString(1, holderBankId);
			statement.setString(2, payeeBankId);
			statement.setString(3, paymentId);
			statement.setDouble(4, gross_bill);
			statement.setString(5, description);
			statement.executeUpdate();
		}
		catch (SQLException ignored) {}
		return lookupTransactionByHolderUsernameAndTransactionId(holderUsername, paymentId);
	}
	
	public BankAccountTransaction lookupTransactionByHolderUsernameAndTransactionId(String holderUsername, String paymentId)
	{
		if(!transactionAlreadyExists(paymentId)) return BankAccountTransaction.emptyBankAccountTransactionWithError();
		User holder = this.playerManager.lookupPlayer(holderUsername);
		String holderUserId = holder.getUserId();
		BankAccount holderBank = this.bankManager.lookupBankByUserId(holderUserId);
		String holderBankAccountId = holderBank.getBankId();
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users_bank_payments WHERE holder = ? AND paymentId = ?;"))
		{
			statement.setString(1, holderBankAccountId);
			statement.setString(2, paymentId);
			ResultSet query = statement.executeQuery();
			if(query.next())
				return BankAccountTransaction.builder()
						.holder(holder)
						.payee(this.playerManager.lookupPlayerByUserId(query.getString("payee")))
						.paymentId(query.getString("paymentId"))
						.description(query.getString("transaction_description"))
						.transaction(query.getDouble("transaction"))
						.responseType(ResponseType.SUCCESS)
						.build();
		}
		catch (SQLException ignored) {}
		return BankAccountTransaction.emptyBankAccountTransactionWithError();
	}
	
	/**
	 * Looks up for all transactions by holder's username
	 * @param holderUsername Who made the transaction
	 * @return {@link BankAccountTransaction}
	 */
	public List<BankAccountTransaction> lookupTransactionsByHolderUsername(String holderUsername)
	{
		List<BankAccountTransaction> result = new LinkedList<>();
		User holder = this.playerManager.lookupPlayer(holderUsername);
		String holderUserId = holder.getUserId();
		BankAccount holderBank = this.bankManager.lookupBankByUserId(holderUserId);
		String holderBankAccountId = holderBank.getBankId();
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users_bank_payments WHERE holder = ?;"))
		{
			statement.setString(1, holderBankAccountId);
			ResultSet query = statement.executeQuery();
			while(query.next())
				result.add(BankAccountTransaction.builder()
						.holder(holder)
						.payee(this.playerManager.lookupPlayerByUserId(query.getString("payee")))
						.paymentId(query.getString("paymentId"))
						.description(query.getString("transaction_description"))
						.transaction(query.getDouble("transaction"))
						.responseType(ResponseType.SUCCESS)
						.build());
		}
		catch (SQLException ignored) {}
		return List.of();
	}
	
	/**
	 * This method strictly looksUp for paymentId
	 * @return true if it exists false if it doesn't
	 */
	public boolean transactionAlreadyExists(String paymentId)
	{
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) FROM users_bank_payments WHERE paymentId = ?;"))
		{
			statement.setString(1, paymentId);
			ResultSet query = statement.executeQuery();
			if(query.next())
				return query.getInt("COUNT(*)") > 0;
		}
		catch (SQLException ignored) {}
		return false;
	}
	
}
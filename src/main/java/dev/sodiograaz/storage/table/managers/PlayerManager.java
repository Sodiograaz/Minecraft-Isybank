package dev.sodiograaz.storage.table.managers;

import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.User;
import dev.sodiograaz.storage.utils.ResponseType;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* @author Sodiograaz
 @since 17/10/2024
*/
public class PlayerManager
{
	
	private final Connection connection;
	
	@SneakyThrows
	public PlayerManager(StorageManager storageManager)
	{
		this.connection = storageManager.getHikariDataSource()
				.getConnection();
	}
	
	/**
	 * Creates a player in the database
	 * @param username The username
	 * @param userId The userId
	 * @return the userId
	 */
	public User createPlayer(String username, String userId)
	{
		if(playerAlreadyRegistered(userId)) return User.emptyUserWithError();
		try(PreparedStatement statement = this.connection.prepareStatement("INSERT INTO users (username, userId) VALUES (?,?);"))
		{
			statement.setString(1, username);
			statement.setString(2, userId);
			statement.executeUpdate();
		} catch (SQLException ignored) {}
		return lookupPlayer(username);
	}
	
	/**
	 * Looks up for the username
	 * @param userId The userId to lookup
	 * @return Player's account username
	 */
	public User lookupPlayerByUserId(String userId)
	{
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users WHERE userId = ?;"))
		{
			statement.setString(1, userId);
			ResultSet query = statement.executeQuery();
			if(query.next())
				return User.builder()
						.responseType(ResponseType.SUCCESS)
						.userId(query.getString("userId"))
						.username(query.getString("username"))
						.isAcceptingPayments(false)
						.build();
		}
		catch (SQLException ignored) {}
		return User.emptyUserWithError();
	}
	
	/**
	 * Looks up for the User
	 * @param username {@link String} the username
	 * @return {@link String} the userId
	 */
	public User lookupPlayer(String username)
	{
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users WHERE username = ?;"))
		{
			statement.setString(1, username);
			ResultSet query = statement.executeQuery();
			if(query.next())
				return User.builder()
						.responseType(ResponseType.SUCCESS)
						.userId(query.getString("userId"))
						.username(query.getString("username"))
						.isAcceptingPayments(false)
						.build();
		}
		catch (SQLException ignored) {}
		return User.emptyUserWithError();
	}
	
	public List<User> getAllPlayers()
	{
		List<User> result = new ArrayList<>();
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users;"))
		{
			ResultSet query = statement.executeQuery();
			while(query.next())
				result.add(User.builder()
						.userId(query.getString("userId"))
						.username(query.getString("username"))
						.isAcceptingPayments(false)
						.responseType(ResponseType.SUCCESS)
						.build());
		}
		catch (SQLException ignored) {}
		return result;
	}
	
	/**
	 * Deletes a player from database
	 * @param userId {@link String} the userId
	 * @return the rows deleted, 0 if none is deleted
	 */
	public int deletePlayer(String userId)
	{
		int result = 0; // 0 = nothing happened; 1 = success
		try(PreparedStatement statement = this.connection.prepareStatement("DELETE FROM users WHERE userId = ?;"))
		{
			statement.setString(1, userId);
			result = statement.executeUpdate();
		}
		catch (SQLException ignored) {}
		return result;
	}
	
	/**
	 * Updates player's username
	 * @param userId {@link String} the userId
	 * @return the rows deleted, 0 if none is deleted
	 */
	public int updatePlayerUsername(String userId, String username)
	{
		if(!playerAlreadyRegistered(userId)) return 0;
		
		int result = 0; // 0 = nothing happened; 1 = success
		try(PreparedStatement statement = this.connection.prepareStatement("UPDATE * FROM users SET username = ? WHERE userId = ?;"))
		{
			statement.setString(1, username);
			statement.setString(2, userId);
			result = statement.executeUpdate();
		}
		catch (SQLException ignored) {}
		return result;
	}
	
	public boolean playerAlreadyRegistered(String userId)
	{
		try(PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) FROM users WHERE userId = ?;"))
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
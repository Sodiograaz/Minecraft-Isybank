package dev.sodiograaz.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.sodiograaz.storage.table.managers.*;
import lombok.Getter;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Map;

/* @author Sodiograaz
 @since 17/10/2024
*/
public class StorageManager
{
	
	@Getter
	private HikariDataSource hikariDataSource;
	
	private String host, port, database, username, password;
	private Map<String,Object> parameters;
	
	private final ConfigurationSection storageSection;
	
	public StorageManager(Plugin plugin)
	{
		this.storageSection = plugin.getConfig().getConfigurationSection("Storage");
		this.host = this.storageSection.getString("Host");
		this.port = this.storageSection.getString("Port");
		this.username = this.storageSection.getString("Username");
		this.password = this.storageSection.getString("Password");
		this.database = this.storageSection.getString("Database");
		ConfigurationSection urlParameters = this.storageSection.getConfigurationSection("UrlParameters");
		parameters = urlParameters.getValues(false);
		parameters.forEach((x,y) -> Bukkit.getLogger().info(String.format("%s -> %s", x, y)));
	}
	
	@Getter
	private PlayerManager playerManager;
	@Getter
	private BankManager bankManager;
	@Getter
	private PaymentsManager paymentsManager;
	@Getter
	private PaymentsStaffManager paymentsStaffManager;
	@Getter
	private BankCashManager bankCashManager;
	
	public StorageManager createConnection()
	{
		if(!checkConnection()) return this;
		
		this.hikariDataSource = new HikariDataSource(getHikariConfig());
		
		Bukkit.getLogger().info(String.format("2: Connection check: isClosed: %s, isRunning: %s", this.hikariDataSource.isClosed(), this.hikariDataSource.isRunning()));
		try
		{
			ScriptRunner scriptRunner = new ScriptRunner(hikariDataSource.getConnection());
			InputStream inputStream = this.getClass()
					.getClassLoader()
					.getResourceAsStream("schema.sql");
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			scriptRunner.runScript(inputStreamReader);
		}
		catch (SQLException exception)
		{
			Bukkit.getLogger().severe(exception.getLocalizedMessage());
		}
		
		this.playerManager = new PlayerManager(this);
		this.bankManager = new BankManager(this);
		this.paymentsManager = new PaymentsManager(this);
		this.paymentsStaffManager = new PaymentsStaffManager(this);
		this.bankCashManager = new BankCashManager(this);
		
		return this;
	}
	
	private HikariConfig getHikariConfig()
	{
		HikariConfig config = new HikariConfig();
		config.setUsername(this.username);
		config.setPassword(this.password);
		config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",this.host,this.port,this.database));
		this.parameters.forEach(config::addDataSourceProperty);
		config.setAutoCommit(true);
		return config;
	}
	
	private boolean checkConnection()
	{
		boolean result = new HikariDataSource(getHikariConfig()).isClosed();
		Bukkit.getLogger().info(String.format("1: Check connection is valid: %s", result));
		return !result;
	}
	
}
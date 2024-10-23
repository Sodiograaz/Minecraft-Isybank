package dev.sodiograaz.internal.hooks;

import dev.sodiograaz.internal.IsybankEconomy;
import dev.sodiograaz.internal.utils.ConfigurationUtils;
import dev.sodiograaz.internal.utils.NumberFormatter;
import dev.sodiograaz.storage.StorageManager;
import dev.sodiograaz.storage.data.BankAccount;
import dev.sodiograaz.storage.table.managers.BankCashManager;
import dev.sodiograaz.storage.table.managers.BankManager;
import dev.sodiograaz.storage.table.managers.PlayerManager;
import dev.sodiograaz.storage.utils.ResponseType;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

/* @author Sodiograaz
 @since 20/10/2024
*/
public class VaultHook implements Economy
{
	private final StorageManager storageManager = IsybankEconomy.getStorageManager();
	private final PlayerManager playerManager = storageManager.getPlayerManager();
	private final BankManager bankManager = storageManager.getBankManager();
	private final BankCashManager bankCashManager = storageManager.getBankCashManager();
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String getName() {
		return "Isybank";
	}
	
	@Override
	public boolean hasBankSupport() {
		return true;
	}
	
	@Override
	public int fractionalDigits() {
		return 2;
	}
	
	@Override
	public String format(double v) {
		return NumberFormatter.formatValue(v);
	}
	
	@Override
	public String currencyNamePlural() {
		return ConfigurationUtils.ECONOMY_PREFIX_PLURAL;
	}
	
	@Override
	public String currencyNameSingular() {
		return ConfigurationUtils.ECONOMY_PREFIX_SINGULAR;
	}
	
	@Override
	public boolean hasAccount(String s) {
		return bankManager.bankAlreadyRegistered(this.playerManager.lookupPlayer(s)
				.getUserId());
	}
	
	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		return bankManager.bankAlreadyRegistered(this.playerManager.lookupPlayer(offlinePlayer.getName())
				.getUserId());
	}
	
	@Override
	public boolean hasAccount(String s, String s1) {
		return false;
	}
	
	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
		return false;
	}
	
	@Override
	public double getBalance(String s) {
		return this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(s).getUserId()).getBankAvailability();
	}
	
	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId()).getBankAvailability();
	}
	
	@Override
	public double getBalance(String s, String s1) {
		return 0;
	}
	
	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String s) {
		return 0;
	}
	
	@Override
	public boolean has(String s, double v) {
		return this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(s).getUserId()).getBankAvailability() >= v;
	}
	
	@Override
	public boolean has(OfflinePlayer offlinePlayer, double v) {
		return this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId()).getBankAvailability() >= v;
	}
	
	@Override
	public boolean has(String s, String s1, double v) {
		return false;
	}
	
	@Override
	public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
		return false;
	}
	
	@Override
	public EconomyResponse withdrawPlayer(String s, double v) {
		if(v < 0)
			return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "Negative Numbers");
		BankAccount bankAccountBefore = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(s).getUserId());
		this.bankCashManager.remove("ADMIN", s, v);
		BankAccount bankAccountAfter = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(s).getUserId());
		return new EconomyResponse(bankAccountBefore.getBankAvailability(), bankAccountAfter.getBankAvailability(), EconomyResponse.ResponseType.SUCCESS, "Success");
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
		if(v < 0)
			return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "Negative Numbers");
		BankAccount bankAccountBefore = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId());
		this.bankCashManager.remove("ADMIN", offlinePlayer.getName(), v);
		BankAccount bankAccountAfter = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId());
		return new EconomyResponse(bankAccountBefore.getBankAvailability(), bankAccountAfter.getBankAvailability(), EconomyResponse.ResponseType.SUCCESS, "Success");
	}
	
	@Override
	public EconomyResponse withdrawPlayer(String s, String s1, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse depositPlayer(String s, double v) {
		if(v < 0)
			return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "Negative Numbers");
		BankAccount bankAccountBefore = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(s).getUserId());
		this.bankCashManager.give("ADMIN", s, v);
		BankAccount bankAccountAfter = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(s).getUserId());
		return new EconomyResponse(bankAccountBefore.getBankAvailability(), bankAccountAfter.getBankAvailability(), EconomyResponse.ResponseType.SUCCESS, "Success");
	}
	
	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
		if(v < 0)
			return new EconomyResponse(0,0, EconomyResponse.ResponseType.FAILURE, "Negative Numbers");
		BankAccount bankAccountBefore = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId());
		this.bankCashManager.give("ADMIN", offlinePlayer.getName(), v);
		BankAccount bankAccountAfter = this.bankManager.lookupBankByUserId(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId());
		return new EconomyResponse(bankAccountBefore.getBankAvailability(), bankAccountAfter.getBankAvailability(), EconomyResponse.ResponseType.SUCCESS, "Success");
	}
	
	@Override
	public EconomyResponse depositPlayer(String s, String s1, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse createBank(String s, String s1) {
		BankAccount bank = this.bankManager.createBank(this.playerManager.lookupPlayer(s1).getUserId(), UUID.randomUUID().toString(), ConfigurationUtils.INITIAL_BALANCE);
		if(bank.getResponseType().equals(ResponseType.ERROR))
			return new EconomyResponse(ConfigurationUtils.INITIAL_BALANCE, ConfigurationUtils.INITIAL_BALANCE, EconomyResponse.ResponseType.FAILURE, "Error");
		return new EconomyResponse(ConfigurationUtils.INITIAL_BALANCE, ConfigurationUtils.INITIAL_BALANCE, EconomyResponse.ResponseType.SUCCESS, "Success");
	}
	
	@Override
	public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
		BankAccount bank = this.bankManager.createBank(this.playerManager.lookupPlayer(offlinePlayer.getName()).getUserId(), UUID.randomUUID().toString(), ConfigurationUtils.INITIAL_BALANCE);
		if(bank.getResponseType().equals(ResponseType.ERROR))
			return new EconomyResponse(ConfigurationUtils.INITIAL_BALANCE, ConfigurationUtils.INITIAL_BALANCE, EconomyResponse.ResponseType.FAILURE, "Error");
		return new EconomyResponse(ConfigurationUtils.INITIAL_BALANCE, ConfigurationUtils.INITIAL_BALANCE, EconomyResponse.ResponseType.SUCCESS, "Success");
	}
	
	@Override
	public EconomyResponse deleteBank(String s) {
		return null;
	}
	
	@Override
	public EconomyResponse bankBalance(String s) {
		return null;
	}
	@Override
	public EconomyResponse bankHas(String s, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse bankWithdraw(String s, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse bankDeposit(String s, double v) {
		return null;
	}
	
	@Override
	public EconomyResponse isBankOwner(String s, String s1) {
		return null;
	}
	
	@Override
	public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
		return null;
	}
	
	@Override
	public EconomyResponse isBankMember(String s, String s1) {
		return null;
	}
	
	@Override
	public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
		return null;
	}
	
	@Override
	public List<String> getBanks() {
		return List.of();
	}
	
	@Override
	public boolean createPlayerAccount(String s) {
		return false;
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return false;
	}
	
	@Override
	public boolean createPlayerAccount(String s, String s1) {
		return false;
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
		return false;
	}
}
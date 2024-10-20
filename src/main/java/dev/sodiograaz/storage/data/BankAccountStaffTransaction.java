package dev.sodiograaz.storage.data;

import dev.sodiograaz.storage.utils.ResponseType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/* @author Sodiograaz
 @since 20/10/2024
*/
@Builder(toBuilder = true)
@Getter
@ToString
public class BankAccountStaffTransaction
{
	
	private User moderator;
	private User modifiedUser;
	private double bill;
	private ResponseType responseType;
	
	
	public static BankAccountStaffTransaction emptyBankAccountStaffTransaction()
	{
		return BankAccountStaffTransaction.builder()
				.moderator(User.emptyUser())
				.modifiedUser(User.emptyUser())
				.bill(0D)
				.responseType(ResponseType.NOTHING_HAPPENED)
				.build();
	}
	
	public static BankAccountStaffTransaction emptyBankAccountStaffTransactionWithError()
	{
		return emptyBankAccountStaffTransaction()
				.toBuilder()
				.moderator(User.emptyUserWithError())
				.modifiedUser(User.emptyUserWithError())
				.responseType(ResponseType.ERROR)
				.build();
	}
	
}
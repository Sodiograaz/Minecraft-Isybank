package dev.sodiograaz.storage.data;

import dev.sodiograaz.storage.utils.ResponseType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/* @author Sodiograaz
 @since 17/10/2024
*/
@Getter
@Builder(toBuilder = true)
@ToString
public class BankAccount
{
	private User holder;
	private String bankId;
	private double bankAvailability;
	private boolean acceptsPayments;
	private ResponseType responseType;
	
	public static BankAccount emptyBankAccount()
	{
		return BankAccount.builder()
				.holder(User.emptyUser())
				.bankId("")
				.bankAvailability(0D)
				.responseType(ResponseType.SUCCESS)
				.build();
	}
	
	public static BankAccount emptyBankAccountWithError()
	{
		return emptyBankAccount()
				.toBuilder()
				.holder(User.emptyUserWithError())
				.responseType(ResponseType.ERROR)
				.build();
	}
	
}
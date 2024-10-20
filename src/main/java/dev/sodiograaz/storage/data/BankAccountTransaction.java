package dev.sodiograaz.storage.data;

import dev.sodiograaz.storage.utils.ResponseType;
import lombok.Builder;
import lombok.Getter;

/* @author Sodiograaz
 @since 19/10/2024
*/
@Builder(toBuilder = true)
@Getter
public class BankAccountTransaction
{
	
	private User holder;
	private User payee;
	private double gross;
	private String description;
	private String paymentId;
	private ResponseType responseType;
	private double accountNow;
	
	public static BankAccountTransaction emptyBankAccountTransaction()
	{
		return BankAccountTransaction
				.builder()
				.holder(User.emptyUser())
				.payee(User.emptyUser())
				.description("")
				.paymentId("")
				.gross(0D)
				.accountNow(0D)
				.responseType(ResponseType.NOTHING_HAPPENED)
				.build();
	}
	
	public static BankAccountTransaction emptyBankAccountTransactionWithError()
	{
		return emptyBankAccountTransaction()
				.toBuilder()
				.holder(User.emptyUserWithError())
				.payee(User.emptyUserWithError())
				.responseType(ResponseType.ERROR)
				.build();
	}
	
}
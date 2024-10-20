package dev.sodiograaz.storage.data;

import dev.sodiograaz.storage.utils.ResponseType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/* @author Sodiograaz
 @since 19/10/2024
*/
@Getter
@Builder(toBuilder = true)
@ToString
public class User
{
	private String username;
	private String userId;
	private boolean isAcceptingPayments; // NOT YET USED
	private ResponseType responseType;
	
	public static User emptyUser()
	{
		return User.builder()
				.userId("")
				.username("")
				.isAcceptingPayments(false)
				.responseType(ResponseType.NOTHING_HAPPENED)
				.build();
	}
	
	public static User emptyUserWithError()
	{
		return emptyUser()
				.toBuilder()
				.responseType(ResponseType.ERROR)
				.build();
	}
	
}
package webshopREST.database;

import types.AuthenticationToken;
import types.User;

public class TokenService {
	
	public static AuthenticationToken getToken(User user) {
		//TODO: Tokenin luominen.
		AuthenticationToken token = new AuthenticationToken();
		token.setToken("xxx.yyy.zzz");
		return token;
	}
}

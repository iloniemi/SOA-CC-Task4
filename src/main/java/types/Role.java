package types;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Tämän enum-luokan tyypin avulla voidaan testata oikeuksia
 * ilman merkkijonoja tyyliin if (user.getRoles().contains(Role.ADMIN))
 * @author Jukka
 *
 */
public enum Role {
	ADMIN("ADMIN"),
	USER("USER");
	
	private String id;
	 
	Role(String id) {
        this.id = id;
    }
 
	@JsonValue
    public String getId() {
        return id;
    }
}

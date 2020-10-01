package types;

import com.fasterxml.jackson.annotation.JsonValue;

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

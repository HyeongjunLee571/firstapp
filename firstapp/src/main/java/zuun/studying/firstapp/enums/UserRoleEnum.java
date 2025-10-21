package zuun.studying.firstapp.enums;

import lombok.Getter;

@Getter

public enum UserRoleEnum {

    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    UserRoleEnum(String role) {
        this.role = role;
    }
}

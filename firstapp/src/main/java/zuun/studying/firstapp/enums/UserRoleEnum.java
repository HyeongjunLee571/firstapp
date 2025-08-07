package zuun.studying.firstapp.enums;

import lombok.Getter;

@Getter

public enum UserRoleEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final  String role;

    UserRoleEnum(String role) {
        this.role = role;
    }
}

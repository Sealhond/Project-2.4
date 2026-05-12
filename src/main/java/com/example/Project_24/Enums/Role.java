package com.example.Project_24.Enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
        ROLE_CUSTOMER,
        ROLE_EMPLOYEE;

        //role_ is spring convention

    public String getAuthority() {
        return name();
    }
}

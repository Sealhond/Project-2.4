package com.example.Project_24.Security;

import com.example.Project_24.Models.Account;
import com.example.Project_24.Models.User;
import com.example.Project_24.Repositories.AccountRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final AccountRepository accountRepository;

    public CustomPermissionEvaluator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetId == null || targetType == null || permission == null) {
            return false;
        }

        if (!(authentication.getPrincipal() instanceof User currentUser)) {
            return false;
        }

        // employees can do everything
        if (isEmployee(authentication)) {
            return true;
        }

        if ("account".equalsIgnoreCase(targetType)) {
            String iban = targetId.toString();
            return accountRepository.findById(iban)
                    .map(Account::getUser)
                    .map(User::getId)
                    .filter(ownerId -> ownerId.equals(currentUser.getId()))
                    .isPresent();
        }

        return false;
    }

    private boolean isEmployee(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_EMPLOYEE"::equals);
    }
}

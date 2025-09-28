package org.digitalwallet.app.service;

import lombok.AllArgsConstructor;
import org.digitalwallet.app.repository.UserAuthRepository;
import org.digitalwallet.app.repository.model.UserAuth;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;

    public UserAuth findByUserName(String userName) {
        return userAuthRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
    }

    public boolean isCustomerOwn(String userName, Long customerId) {
        UserAuth userAuth = findByUserName(userName);
        return customerId.equals(userAuth.getCustomerId());
    }
}

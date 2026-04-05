package com.flab.woowahaneats.domain.user.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.user.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.user.controller.dto.UserResponse;
import com.flab.woowahaneats.domain.user.controller.dto.UserSignUpRequest;
import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.user.domain.User;
import com.flab.woowahaneats.domain.auth.repository.AccountRepository;
import com.flab.woowahaneats.domain.user.repository.UserRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public void signUpUser(UserSignUpRequest userSignUpRequest) {
        Account existingAccount = accountRepository.findByEmail(userSignUpRequest.email());

        if (existingAccount != null) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(userSignUpRequest.password());

        Account account = Account.builder()
                .id(userSignUpRequest.id())
                .password(encodedPassword)
                .email(userSignUpRequest.email())
                .build();

        accountRepository.save(account);

        User user = User.create(
                account.getId(),
                userSignUpRequest.name(),
                userSignUpRequest.phoneNumber(),
                userSignUpRequest.address(),
                userSignUpRequest.location(),
                userSignUpRequest.profileImageUrl(),
                userSignUpRequest.nickName()
        );

        userRepository.save(user);
    }

    public UserResponse getUserProfile(){

        User user = AuthContextHolder.getContext().getUser();
        Account account = accountRepository.findById(user.getAccountId());

        return UserResponse.from(user, account);
    }
}

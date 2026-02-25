package com.flab.woowahaneats.domain.member.application;

import com.flab.woowahaneats.domain.auth.UserAuthContext;
import com.flab.woowahaneats.domain.member.application.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.member.controller.dto.UserResponse;
import com.flab.woowahaneats.domain.member.controller.dto.UserSignUpRequest;
import com.flab.woowahaneats.domain.member.domain.Account;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.member.repository.AccountRepository;
import com.flab.woowahaneats.domain.member.repository.UserRepository;
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
                .name(userSignUpRequest.name())
                .password(encodedPassword)
                .email(userSignUpRequest.email())
                .phoneNumber(userSignUpRequest.phoneNumber())
                .build();

        accountRepository.save(account);

        User user = User.builder()
                .id(userSignUpRequest.id())
                .accountId(account.getId())
                .nickName(userSignUpRequest.nickName())
                .address(userSignUpRequest.address())
                .location(userSignUpRequest.location())
                .profileImageUrl(userSignUpRequest.profileImageUrl())
                .build();

        userRepository.save(user);
    }

    public UserResponse getUserProfile(){

        User user = UserAuthContext.getUser();
        Account account = accountRepository.findById(user.getAccountId());

        return UserResponse.from(user, account);
    }
}

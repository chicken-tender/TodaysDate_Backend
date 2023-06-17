package com.kh.backend_finalproject.service;

import com.kh.backend_finalproject.dto.TokenDto;
import com.kh.backend_finalproject.dto.UserRequestDto;
import com.kh.backend_finalproject.dto.UserResponseDto;
import com.kh.backend_finalproject.entitiy.UserTb;
import com.kh.backend_finalproject.jwt.TokenProvider;
import com.kh.backend_finalproject.repository.UserRepository;
import com.kh.backend_finalproject.utils.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 🔐회원가입
    public UserResponseDto join(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 사용자 압니다. 🐿️");
        }

        UserTb user = requestDto.toUserTb(passwordEncoder);
        return UserResponseDto.of(userRepository.save(user));
    }

    // 🔐로그인
    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        try {
            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            return tokenProvider.generateTokenDto(authentication);
        } catch (AuthenticationException e) {
            System.out.println("뭔가 잘못됐다....⛑️" + e.getMessage());
            throw e;
        }
    }

    // 🔑토큰 검증 및 사용자 정보 추출
    public UserTb validateTokenAndGetUser(HttpServletRequest request, UserDetails userDetails) {
        // ♻️토큰 추출
        String accessToken = request.getHeader("Authorization");
        if(accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        // 🔑토큰 유효한지 검증
        if(accessToken != null && tokenProvider.validateToken(accessToken)) {
            Long userId = Long.valueOf(userDetails.getUsername());
            UserTb user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
            return user;
        } else {
            throw new TokenExpiredException("🔴토큰이 만료됐습니다. Refresh Token을 보내주세요.");
        }
    }
}
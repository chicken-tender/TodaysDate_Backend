package com.kh.backend_finalproject.service;

import com.kh.backend_finalproject.dto.UserDto;
import com.kh.backend_finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // ✅ 마이페이지 - 회원 프로필 바 가져오기 (프로필사진, 닉네임, 멤버십 여부, 한 줄 소개)
    public List<UserDto> findUserInfo(String email) {
        List<UserDto> userDtos = userRepository.findUserInfo(email);
        return userDtos;
    }
}
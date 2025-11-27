package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.UserDto;
import zuun.studying.firstapp.Dto.UserResponseDto;
import zuun.studying.firstapp.entity.BaseEntity;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.enums.UserRoleEnum;
import zuun.studying.firstapp.exception.UserAlreadyExistsException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.UserMapper;
import zuun.studying.firstapp.passwordEncoder.CustomPasswordEncoder;
import zuun.studying.firstapp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CustomPasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserDto userDto){

        if(userMapper.countByUsername(userDto.getUsername()) > 0) {
            throw new UserAlreadyExistsException("이미 존재하는 유저입니다.");
        }

        String role = UserRoleEnum.USER.name();

        userRepository.insertUser(userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),userDto.getEmail(),role);
    }

    public UserResponseDto getUser(String username){

        UserResponseDto user = userMapper.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException("존재하지 않는 유저정보입니다.");
        }

        return user;
    }
}

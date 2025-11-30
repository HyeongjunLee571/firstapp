package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.UserDto;
import zuun.studying.firstapp.Dto.UserResponseDto;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.enums.UserRoleEnum;
import zuun.studying.firstapp.exception.UserAlreadyExistsException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.UserMapper;
import zuun.studying.firstapp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserDto userDto){

        if(userMapper.countByUsername(userDto.getUsername()) > 0) {
            throw new UserAlreadyExistsException("이미 존재하는 유저입니다.");
        }

        String role = UserRoleEnum.USER.name();

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        UserRoleEnum userRole = UserRoleEnum.valueOf(role);
        user.setUserRole(userRole);

        userMapper.insertUser(user);
    }

    public UserResponseDto getUser(String username){

        UserResponseDto user = userMapper.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException("존재하지 않는 유저정보입니다.");
        }

        return user;
    }
}

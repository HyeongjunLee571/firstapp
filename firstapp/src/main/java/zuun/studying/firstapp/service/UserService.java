package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.UserDto;
import zuun.studying.firstapp.entity.BaseEntity;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.enums.UserRoleEnum;
import zuun.studying.firstapp.exception.UserAlreadyExistsException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.passwordEncoder.CustomPasswordEncoder;
import zuun.studying.firstapp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserDto userDto){

        if(userRepository.countByUsername(userDto.getUsername()) > 0) {
            throw new UserAlreadyExistsException("이미 존재하는 유저입니다.");
        }

        String role = UserRoleEnum.USER.name();

        userRepository.insertUser(userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),userDto.getEmail(),role);
    }

    public User getUser(String username){

        return userRepository.findByUsername(username).
                orElseThrow(()->new UserNotFoundException("없는 사용자입니다."));

    }
}

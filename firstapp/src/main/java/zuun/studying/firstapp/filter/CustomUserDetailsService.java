package zuun.studying.firstapp.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.UserResponseDto;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.UserAlreadyExistsException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.UserMapper;
import zuun.studying.firstapp.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.findEntityByUsername(username);
        if(user == null){
            throw new UserNotFoundException("존재하지 않는 유저정보입니다.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(user.getUserRole().getRole()))
                .build();

    }
}

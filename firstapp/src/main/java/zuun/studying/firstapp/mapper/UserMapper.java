package zuun.studying.firstapp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zuun.studying.firstapp.Dto.UserResponseDto;
import zuun.studying.firstapp.entity.User;

@Mapper
public interface UserMapper {

    UserResponseDto findByUsername(@Param("username") String username);

    int  countByUsername(@Param("username") String username);

    void insertUser(User user);

    User findEntityByUsername(String username);


}

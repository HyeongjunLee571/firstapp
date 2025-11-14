package zuun.studying.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.enums.UserRoleEnum;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query("SELECT u FROM User u WHERE  u.username = :username")
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM users u WHERE u.username = :username", nativeQuery = true)
    int countByUsername(@Param("username") String username);


    @Modifying
    @Query(value = "INSERT INTO users(username,password,email,userRole,created_at)"+//컬럼명
            "VALUES (:username,:password,:email,:userRole,NOW())",nativeQuery = true)//파라미터 명
    void insertUser(@Param("username") String username,
                    @Param("password") String password,
                    @Param("email") String email,
                    @Param("userRole") String userRole);//값 전달(새로운 값 수정 및 추가 시 새로운 값을 컬럼과 파라미터와 값에도 추가해야함)

}

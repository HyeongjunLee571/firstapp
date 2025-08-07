package zuun.studying.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zuun.studying.firstapp.entity.User;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    User findByUserName(String userName);
}

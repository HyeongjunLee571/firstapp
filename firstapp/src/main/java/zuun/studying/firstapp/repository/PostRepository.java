package zuun.studying.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zuun.studying.firstapp.entity.Post;

@Repository
public  interface PostRepository extends JpaRepository<Post,Long> {


}

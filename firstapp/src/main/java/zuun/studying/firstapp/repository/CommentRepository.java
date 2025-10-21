package zuun.studying.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    //List<Comment> findByPostId(Long postId);

    @Modifying
    @Query(value = "INSERT INTO comments(content,writer,post_id,likes)"+
            "VALUES (:content,:writer,:postId,0)",nativeQuery = true)
    void createComment(@Param("content") String content,
                    @Param("writer") String writer,
                    @Param("postId") Long postId,
                    @Param("likes") int likes);

    @Query("SELECT c FROM Comment c WHERE  c.id = :id")
    Optional<Comment> viewComment(@Param("id") Long id);

    @Query("SELECT c FROM Comment c WHERE  c.post.id = :id")
    List<Comment> findByPostId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Comment c SET c.likes = :likes WHERE c.id = :id")
        //파라미터 넘길때 조건 값과 수정 값을 같이 선언해야함
    void updateCommentLikes(@Param("id") Long id,@Param("likes")int likes);

}

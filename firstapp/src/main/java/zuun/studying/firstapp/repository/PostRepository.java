package zuun.studying.firstapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public  interface PostRepository extends JpaRepository<Post,Long> {


    //스프링 시큐리티에서 파라미터로 받은 유저 네임과 포스트와 조잉한 유저 객체 네임이 같다면 업뎃 다르면 미업뎃
    //수정
    @Modifying
    @Query(value = "UPDATE Post p SET p.title= :title ,p.content= :content WHERE p.id = :id " +
            "AND p.user.username = :username")
    void updatePost(@Param("id") Long id,
                    @Param("title") String title,
                    @Param("content") String content,
                    @Param("username") String username);
    //생성
    @Modifying
    @Query(value = "INSERT INTO posts(title,content,author,user_id,likes)"+
            "VALUES (:title,:content,:author,:userId,0)",nativeQuery = true)
    void insertPost(@Param("title") String title,
                    @Param("content") String content,
                    @Param("author") String author,
                    @Param("userId") Long userId,
                    @Param("likes") int likes);

    @Modifying
    @Query("UPDATE Post p SET p.likes = :likes WHERE p.id = :id")
    //파라미터 넘길때 조건 값과 수정 값을 같이 선언해야함
    void updatePostLikes(@Param("id") Long id,@Param("likes")int likes);

    //조회(Post 엔티티로 조회 조건은 postid 기준)
    @Query("SELECT p FROM Post p WHERE  p.id = :id")
    Optional<Post> viewPost(@Param("id") Long id);

    //전체 조회(Post 엔티티로 전체 조회 작성자 포함하여서(작성자는 즉시 조회)
//    @Query(value = "SELECT p from Post p join FETCH p.user ORDER BY p.id DESC ")
//    List<Post> findAllPosts();

    //전체 조회(Post 엔티티로 전체 조회 작성자 포함하여서(작성자는 즉시 조회)
    @Query(value = "SELECT * from posts ORDER BY created_at desc limit :limit OFFSET :offset" ,nativeQuery = true)
    List<Post> findAllPagePosts(@Param("limit") int limit,@Param("offset") int offset);

    //전체 게시글 수
    @Query(value = "SELECT count(*) FROM posts",nativeQuery = true)
    int countAllPost();

    String user(User user);

    @Modifying
    @Query(value = "DELETE FROM Post WHERE id=:id")
    void deletePostById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM Comment c WHERE c.post.id = :id")
    void commentsdeleteById(@Param("id") Long id);
}

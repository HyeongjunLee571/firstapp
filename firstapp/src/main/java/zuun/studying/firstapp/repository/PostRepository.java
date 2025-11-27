//package zuun.studying.firstapp.repository;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import zuun.studying.firstapp.entity.Post;
//import java.util.Optional;
//
//@Repository
//public  interface PostRepository extends JpaRepository<Post,Long> {
//
//
//    //스프링 시큐리티에서 파라미터로 받은 유저 네임과 포스트와 조잉한 유저 객체 네임이 같다면 업뎃 다르면 미업뎃
//    //수정
//    @Modifying
//    @Query(value = "UPDATE Post p SET p.title= :title ,p.content= :content WHERE p.id = :id " +
//            "AND p.user.username = :username")
//    void updatePost(@Param("id") Long id,
//                    @Param("title") String title,
//                    @Param("content") String content,
//                    @Param("username") String username);
//    //게시글 단일 조회(파일 포함) > 파일 없어도 조회 가능 단 유저는 반드시 존재해야함
//    @Query("SELECT p FROM Post p JOIN FETCH p.user LEFT JOIN fetch p.files WHERE  p.id = :id")
//    Optional<Post> findPostWithUserFiles(@Param("id") Long id);
//
//
//    @Modifying//좋아요 추가
//    @Query("UPDATE Post p SET p.likes = :likes WHERE p.id = :id")
//    //파라미터 넘길때 조건 값과 수정 값을 같이 선언해야함
//    void updatePostLikes(@Param("id") Long id,@Param("likes")int likes);
//
////    //게시글 존재 여부 확인용(단일 조회 파일 제외)
////    @Query("SELECT p FROM Post p WHERE  p.id = :id")
////    Optional<Post> viewPost(@Param("id") Long id);
//
//    //페이징 된 게시글 전체목록 조회
//    // (Post 엔티티로 전체 조회 작성자 포함하여서(작성자는 즉시 조회) > 게시글 일부만 나와도 괜찮 > 파일 정보까지는 불필요
//    //ORDER BY 뒤에 오는것은 기준 > DESC 뒤에 조건 기준으로 내림차순 적용 createdAt는 생성시간 기준 id면 아이디 기준
//    //둘다 최신순으로 내림 차순 예시)1,2,3
//    @Query("SELECT p FROM Post p JOIN FETCH  p.user ORDER BY p.createdAt DESC")
//    Page<Post> findAllWithUser(Pageable pageable);
//
//    @Modifying//게시글 삭제용
//    @Query(value = "DELETE FROM Post WHERE id=:id")
//    void DeletePostById(@Param("id") Long id);
//
//    @Modifying//댓글 삭제용
//    @Query(value = "DELETE FROM Comment c WHERE c.post.id = :id")
//    void CommentsDeleteById(@Param("id") Long id);
//
//    @Modifying//댓글 삭제용
//    @Query(value = "DELETE FROM FileEntity f WHERE f.post.id = :id")
//    void filesDeleteById(@Param("id") Long id);
//
//
//}

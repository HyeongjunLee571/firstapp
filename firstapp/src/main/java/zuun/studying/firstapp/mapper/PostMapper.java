package zuun.studying.firstapp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import zuun.studying.firstapp.Dto.PostDetailDto;
import zuun.studying.firstapp.Dto.PostDto;
import zuun.studying.firstapp.entity.Post;

import java.util.List;

@Mapper
public interface PostMapper {

    PostDetailDto findPostWithUserFiles(@Param("id") Long id);
    Page<PostDto> selectAllPosts(Pageable pageable);
    void insertPost(Post post);
    void updatePost(Post post);
    void deletePost(@Param("id") Long id);
    void updatePostLikes(@Param("id") Long id);

}

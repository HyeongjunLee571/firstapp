package zuun.studying.firstapp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zuun.studying.firstapp.Dto.PostCreateDto;
import zuun.studying.firstapp.Dto.PostDetailDto;
import zuun.studying.firstapp.Dto.PostDto;
import zuun.studying.firstapp.entity.Post;

@Mapper
public interface PostMapper {

    PostDetailDto findPostWithUserFiles(@Param("id") Long id);
    Page<PostDto> selectAllPosts(Pageable pageable);
    void insertPost(Post post);
    void updatePost(PostDto post);
    void deletePost(@Param("id") Long id);
    void updatePostLikes(@Param("id") Long id);


}

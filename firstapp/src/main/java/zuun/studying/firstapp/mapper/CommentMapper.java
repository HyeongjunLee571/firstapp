package zuun.studying.firstapp.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import zuun.studying.firstapp.Dto.CommentResponseDto;
import zuun.studying.firstapp.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    void insertComment(Comment comment);

    CommentResponseDto viewComment(@Param("id") Long id);

    List<CommentResponseDto> findByPostId(@Param("id") Long id);

    void updateCommentLikes(@Param("id") Long id);

    void deleteCommentByPostId(@Param("id") Long id);

    void deleteCommentById(@Param("id") Long id);

}

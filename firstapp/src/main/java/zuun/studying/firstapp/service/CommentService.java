package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.CommentRequestDto;
import zuun.studying.firstapp.Dto.CommentResponseDto;
import zuun.studying.firstapp.Dto.PostDetailDto;
import zuun.studying.firstapp.Dto.UserResponseDto;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.FileEntity;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.CommentNotFoundException;
import zuun.studying.firstapp.exception.PostNotFoundException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.CommentMapper;
import zuun.studying.firstapp.mapper.PostMapper;
import zuun.studying.firstapp.mapper.UserMapper;
import zuun.studying.firstapp.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final UserRepository userRepository;
    //private final CommentRepository commentRepository;
    //private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    @Transactional
    public Comment addComment(Long postId, CommentRequestDto commentDto, String username) {

        UserResponseDto user = userMapper.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException("존재하지 않는 유저정보입니다.");
        }

        PostDetailDto postDetailDto =  postMapper.findPostWithUserFiles(postId);
        if(postDetailDto == null){
            throw new PostNotFoundException("존재하지 않는 파일정보입니다.");
        }


        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUserId(user.getId());
        comment.setPostId(postDetailDto.getId());
        comment.setLikes(0);

        return commentMapper.insertComment(comment);
    }

    public List<CommentResponseDto> getComments(Long postId) {

        return commentMapper.findByPostId(postId);

    }

    @Transactional
    public void addLike(Long postId,Long commentId) {

        CommentResponseDto comment = commentMapper.viewComment(commentId);

        if(comment == null){
            throw new CommentNotFoundException("존재하지 않는 댓글입니다.",postId);
        }

        commentMapper.updateCommentLikes(comment.getId());
    }


    @Transactional
    public void deleteComment(Long id){
        commentMapper.deleteCommentByPostId(id);
    }

}

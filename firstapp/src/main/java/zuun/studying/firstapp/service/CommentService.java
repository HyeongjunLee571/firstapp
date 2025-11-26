package zuun.studying.firstapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.Dto.CommentRequestDto;
import zuun.studying.firstapp.Dto.CommentResponseDto;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.entity.User;
import zuun.studying.firstapp.exception.CommentNotFoundException;
import zuun.studying.firstapp.exception.PostNotFoundException;
import zuun.studying.firstapp.exception.UserNotFoundException;
import zuun.studying.firstapp.mapper.CommentMapper;
import zuun.studying.firstapp.repository.PostRepository;
import zuun.studying.firstapp.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment addComment(Long postId, CommentRequestDto commentDto, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UserNotFoundException("존재하지 않는 유저입니다."));

        Post post = postRepository.findPostWithUserFiles(postId).orElseThrow(()->
                new PostNotFoundException("존재하지 않는 게시글입니다."));; //(PostExcption::new) > new PostExcption()); 같음

        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setUserId(user.getId());
        comment.setPostId(post.getId());
        comment.setLikes(0);

        commentMapper.insertComment(comment);

        return comment;

    }

    public List<CommentResponseDto> getComments(Long postId) {

        return commentMapper.findByPostId(postId);

    }

    @Transactional
    public void addLike(Long postId,Long commentId) {

        CommentResponseDto comment = commentMapper.viewComment(commentId);

        if(comment == null){
            throw  new CommentNotFoundException("존재하지 않는 댓글입니다.",postId);
        }

        commentMapper.updateCommentLikes(comment.getId());
    }


    @Transactional
    public void deleteComment(Long id){
        commentMapper.deleteCommentByPostId(id);
    }

}

package zuun.studying.firstapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zuun.studying.firstapp.entity.Comment;
import zuun.studying.firstapp.entity.Post;
import zuun.studying.firstapp.exception.CommentException;
import zuun.studying.firstapp.exception.PostException;
import zuun.studying.firstapp.repository.CommentRepository;
import zuun.studying.firstapp.repository.PostRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void addComment(Long postId, String content, String username) {

        Post post = postRepository.findById(postId).orElseThrow(PostException::new); //(PostExcption::new) > new PostExcption()); 같음

        Comment comment = Comment.builder()
                .content(content)
                .writer(username)
                .post(post)
                .build();


        commentRepository.save(comment);
    }

    public List<Comment> getComments(Long postId) {

        return commentRepository.findByPostId(postId);

    }

    public void addLike(Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);

        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
    }
}

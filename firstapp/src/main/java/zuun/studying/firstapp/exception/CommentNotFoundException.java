package zuun.studying.firstapp.exception;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {
    private final Long postId;

    public CommentNotFoundException(String message,Long postId) {
        super(message);
        this.postId = postId;
    }
}

package zuun.studying.firstapp.exception;

public class CommentException extends RuntimeException {
    public CommentException() {
        super("댓글이 존재하지 않습니다.");
    }
}

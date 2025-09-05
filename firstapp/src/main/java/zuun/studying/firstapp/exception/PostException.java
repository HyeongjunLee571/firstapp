package zuun.studying.firstapp.exception;

public class PostException extends RuntimeException {
    public PostException() {
        super("게시글이 존재하지 않습니다.");
    }
}

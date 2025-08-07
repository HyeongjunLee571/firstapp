package zuun.studying.firstapp.exception;

public class authorityException extends RuntimeException {
    public authorityException(){
        super("권한이 없습니다.");
    }
}

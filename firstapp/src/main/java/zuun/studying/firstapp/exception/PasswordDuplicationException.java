package zuun.studying.firstapp.exception;

public class PasswordDuplicationException extends RuntimeException {
    public PasswordDuplicationException(){
        super("비밀번호가 기존과 동일합니다 다른비밀번호를 입력해주세요.");
    }
}

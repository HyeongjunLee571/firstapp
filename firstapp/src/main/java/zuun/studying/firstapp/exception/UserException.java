package zuun.studying.firstapp.exception;

public class UserException extends RuntimeException {

  public UserException(){
    super("이메일 및 비밀번호가 일치하지 않습니다.");
  }
}

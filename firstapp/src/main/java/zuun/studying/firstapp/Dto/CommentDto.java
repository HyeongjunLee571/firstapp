package zuun.studying.firstapp.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private String content;
    private String writer;
    private int likes = 0;

}

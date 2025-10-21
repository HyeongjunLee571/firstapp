package zuun.studying.firstapp.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    private String title;
    private String content;
    private String author;


    public PostDto(){}
}

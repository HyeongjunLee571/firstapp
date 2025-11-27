package zuun.studying.firstapp.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {

    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(min =2,max = 100, message = "제목은 2~100자여야 합니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Size(min=5,max=1000, message = "내용은 5~1000자여야 합니다.")
    private String content;

}

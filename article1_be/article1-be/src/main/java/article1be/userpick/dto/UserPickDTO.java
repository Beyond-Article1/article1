package article1be.userpick.dto;

import article1be.userpick.entity.Condition;
import article1be.userpick.entity.Style;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPickDTO {

    @NotNull
    private Long userSeq;

    @NotNull(message = "스타일은 필수 입력값입니다.")
    private Style styleSeq;

    @NotNull(message = "체질은 필수 입력값입니다.")
    private Condition conditionSeq;

}

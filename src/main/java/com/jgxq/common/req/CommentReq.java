package com.jgxq.common.req;

import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LuCong
 * @since 2020-12-13
 **/
@Data
public class CommentReq {
    private String target;
    @NotNull
    private Byte type;
    @NotNull
    private Integer objectId;
    private Integer parentId;
    private Integer replyId;
    @NotBlank
    private String content;
}

package com.jgxq.common.req;

import com.jgxq.common.dto.TagInfo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author LuCong
 * @since 2020-12-12
 **/
@Data
public class NewsReq {
    /**
     * 标题-64个字
     */
    @NotBlank
    private String title;

    /**
     * 封面图片
     */
    @NotBlank
    private String cover;

    /**
     * 文章内容
     */
    @NotBlank
    private String text;

    /**
     * 存储tag
     */
    private Set<TagInfo> tags;

    private Date createAt;

}

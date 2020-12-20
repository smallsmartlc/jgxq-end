package com.jgxq.common.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
    @Length(max = 64,message = "标题最多为64个字")
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
    @Length(min = 20,message = "内容不能少于20个字")
    private String text;

    /**
     * 存储tag
     */
    private Set<TagReq> tags;

    private Date createAt;

}

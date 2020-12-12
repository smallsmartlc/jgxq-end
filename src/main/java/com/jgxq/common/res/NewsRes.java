package com.jgxq.common.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Data
public class NewsRes{

    private Integer id;
    private String title;

    private String cover;

    /**
     * 作者userkey
     */
    private AuthorRes author;

    /**
     * 文章内容
     */
    private String text;

    /**
     * 存储tag
     */
    private TagRes tag;

    private Date createAt;

}

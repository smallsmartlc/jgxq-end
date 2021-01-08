package com.jgxq.common.res;

import com.jgxq.common.dto.NewsHit;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private List<TagSearchRes> tags;

    private Date createAt;

    private NewsHit hit;

}

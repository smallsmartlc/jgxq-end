package com.jgxq.common.dto;

import lombok.Data;

/**
 * @author LuCong
 * @since 2020-12-12
 **/
//点赞,评论,收藏

@Data
public class NewsHit {

    private Integer thumbs;

    private Integer Comments;

    private Integer collects;

}

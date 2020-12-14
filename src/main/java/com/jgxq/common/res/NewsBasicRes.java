package com.jgxq.common.res;

import com.jgxq.common.dto.NewsHit;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Data
public class NewsBasicRes {

    private Integer id;
    private String title;

    private String cover;

    private Integer comments;
}

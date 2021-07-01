package com.jgxq.common.res;

import com.jgxq.common.dto.TalkHit;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-14
 */
@Data
public class TalkRes {

    private Integer id;

    /**
     * 作者userkey
     */
    private UserRes author;

    private String text;

    private Date createAt;

    private TalkHit hit;

}

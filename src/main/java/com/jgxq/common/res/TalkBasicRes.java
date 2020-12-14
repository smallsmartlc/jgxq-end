package com.jgxq.common.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jgxq.common.dto.TalkHit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
public class TalkBasicRes {

    private Integer id;

    private UserBasicRes author;

    private String text;

    private TalkHit hit;

}

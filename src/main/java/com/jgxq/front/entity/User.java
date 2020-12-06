package com.jgxq.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户对外的唯一标识
     */
    private String userkey;

    /**
     * 昵称
     */
    private String nickName;

    private String password;

    private String email;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 城市
     */
    private String city;

    /**
     * 个人简介
     */
    private String description;

    /**
     * 有vip信息
     */
    private String vip;

    /**
     * 默认0,作者为1
     */
    private Byte author;

    /**
     * 主队id
     */
    private Integer homeTeam;

    private Date createAt;

    private Date updateAt;


}

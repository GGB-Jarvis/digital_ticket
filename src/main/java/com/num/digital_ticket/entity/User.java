package com.num.digital_ticket.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 999999999999999L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private String openid;
    private String avatar;
    private Integer age;
    private Integer sex;
    private String realName;
    private String phone;
    private String idNumber;
    private Boolean authenticationFlag;
    private Date registerTime;
    private Date authenticationTime;
    private String blockAddress;
    private String blockPrivateKey;
    private String blockPassword;
    private String blockToken;
    private Integer status;
    private Boolean deleteFlag;
    private Date editedTime;


}


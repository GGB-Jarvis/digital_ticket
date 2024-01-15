package com.num.digital_ticket.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
public class Role implements Serializable {
    private static final long serialVersionUID = 555555555555555L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String roleName;
    private String intro;
    private Date createdTime;
    private String creator;
    private Date editedTime;
    private String editor;
    private Boolean deleteFlag;
}


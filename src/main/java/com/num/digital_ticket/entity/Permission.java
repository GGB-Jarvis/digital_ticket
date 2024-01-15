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
@TableName("permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 123456789876543L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private String permissionName;
    private String intro;
    private Integer category;
    private String url;
    private Date createdTime;
    private String creator;
    private Date editedTime;
    private String editor;
    private Boolean deleteFlag;
}


package com.weiziplus.muteki.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.muteki.common.base.BaseColumn;
import com.weiziplus.muteki.common.base.BaseId;
import com.weiziplus.muteki.common.base.BaseTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

/**
 * sys_user_dept
 * @author 王龙伟
 * @date 2020-11-26 15:41:42
 */
@BaseTable("sys_user_dept")
@Alias("SysUserDept")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
public class SysUserDept implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 用户表id
     */
    @BaseColumn("user_id")
    @ApiModelProperty("用户表id")
    private Integer userId;

    /**
     * 部门表id
     */
    @BaseColumn("dept_id")
    @ApiModelProperty("部门表id")
    private Integer deptId;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_DEPT_ID = "dept_id";
}
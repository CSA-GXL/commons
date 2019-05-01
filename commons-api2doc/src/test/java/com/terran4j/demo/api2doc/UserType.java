package com.terran4j.demo.api2doc;

import com.terran4j.commons.api2doc.annotations.ApiComment;
@ApiComment(value = "用户类型")
public enum UserType {

    @ApiComment("管理员")
    admin,

    @ApiComment("普通用户")
    user
}
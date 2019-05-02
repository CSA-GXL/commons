package com.terran4j.demo.api2doc;

import com.terran4j.commons.api2doc.annotations.ApiComment;
import com.terran4j.commons.api2doc.other.RestPackIgnore;

import java.time.LocalDateTime;
import java.util.Date;

@ApiComment(value = "用户表2")
public class User2 {

    @ApiComment(value = "用户id2", sample = "123")
    private Long id2;

    @ApiComment(value = "用户名2", sample = "terran4j")
    private String name2;

    @ApiComment(value = "账号密码2", sample = "sdfi23skvs")
    private String password2;

    @ApiComment(value = "用户所在的组2", sample = "研发组")
    private String group2;

    @ApiComment(value = "用户类型2", sample = "admin")
    private UserType type2;

    @ApiComment(value = "是否已删除2", sample = "true")
    @RestPackIgnore
    private LocalDateTime deleted2;

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getGroup2() {
        return group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public UserType getType2() {
        return type2;
    }

    public void setType2(UserType type2) {
        this.type2 = type2;
    }

    public LocalDateTime getDeleted2() {
        return deleted2;
    }

    public void setDeleted2(LocalDateTime deleted2) {
        this.deleted2 = deleted2;
    }
}
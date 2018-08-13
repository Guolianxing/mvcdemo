package com.test.dto;

import java.util.Objects;

/**
 * Create by Guolianxing on 2018/7/4.
 */
public class UserDto {
    private String userName;
    private String password;

    public UserDto() {
    }

    public UserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(userName, userDto.userName) &&
                Objects.equals(password, userDto.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userName, password);
    }
}

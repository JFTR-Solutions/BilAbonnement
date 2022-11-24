package com.example.bilabonnement.models.users;

public class Roles {

    private int roleId;
    private String roleName;

    public Roles(int role_id, String role_name) {
        this.roleId = role_id;
        this.roleName = role_name;
    }

    public Roles(){

    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }



}

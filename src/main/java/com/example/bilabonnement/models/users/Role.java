package com.example.bilabonnement.models.users;

public class Role {

    private int roleId;
    private String roleName;

    public Role(int role_id, String role_name) {
        this.roleId = role_id;
        this.roleName = role_name;
    }

    public Role() {

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

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

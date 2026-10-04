package com.example.chatychat.models;

import java.util.HashMap;
import java.util.Map;

import com.example.chatychat.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 
 * GroupModel
 */
public class GroupModel extends Model {

    @JsonView(Views.Private.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.Public.class)
    Map<String, Byte> userRole = new HashMap<>();

    /**
     * basic constructor to save file
     */
    public GroupModel() {
    }

    /**
     * constructor by id and user
     * 
     * @param id   to set
     * @param name of group
     * @param user create the group set as default like admin
     */
    public GroupModel(long id, String name, UserModel user) {
        this.id = id;
        this.name = name;
        this.userRole.put(user.getName(), new RoleModel((byte) 127).getRoles());
    }

    /**
     * set name
     * 
     * @param name new for group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get id
     * 
     * @return id of group
     */
    public Long getId() {
        return id;
    }

    /**
     * set roles to a user
     * 
     * @param user to set role
     * @param role new
     */
    public void setUserRoles(UserModel user, RoleModel role) {
        if (user != null && role != null) {
            userRole.put(user.getName(), role.getRoles());
        }
    }

    /**
     * get name
     * 
     * @return name of group
     */
    public String getName() {
        return name;
    }

    /**
     * get user role
     * 
     * @return map of users roles
     */
    public Map<String, Byte> getUserRole() {
        return userRole;
    }

    /**
     * get user role
     * 
     * @return roles number
     */
    public Byte getUserRoles(UserModel user) {
        return userRole.get(user.getName());
    }

    /**
     * add user to user role
     * 
     * @param user  to add
     * @param roles to set
     */
    public void addUser(UserModel user, byte roles) {
        if (user != null) {
            this.userRole.put(user.getName(), new RoleModel(roles).getRoles());
        }
    }

    /**
     * add user role by default 3 (read and write)
     * 
     * @param user to add
     */
    public void addUser(UserModel user) {
        userRole.put(user.getName(), new RoleModel((byte) 3).getRoles());
    }

    /**
     * update user
     * 
     * @param user to update
     * @param role to set
     */
    public void updateUser(UserModel user, RoleModel role) {
        this.userRole.put(user.getName(), role.getRoles());
    }

    /**
     * delete user of group
     * 
     * @param user
     */
    public void deleteUser(UserModel user) {
        this.userRole.remove(user.getName());
    }

    /**
     * (non-Javadoc)
     * return string of object
     * 
     * @see com.example.chatychat.models.Model#toString()
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * compare 2 groups
     * 
     * @param obj goup
     * @return if id is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GroupModel other)) {
            return false;
        }
        return this.id.equals(other.id);
    }

    /**
     * (non-Javadoc)
     * set funtional
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

}

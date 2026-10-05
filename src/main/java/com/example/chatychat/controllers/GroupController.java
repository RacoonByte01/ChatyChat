package com.example.chatychat.controllers;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.chatychat.models.UserModel;
import com.example.chatychat.utils.Files4Models;
import com.example.chatychat.models.GroupModel;
import com.example.chatychat.models.MessageModel;
import com.example.chatychat.models.RoleModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 
 * GroupController
 */
@RestController
public class GroupController {

    // # Begin Group
    // ## Begin GETs

    /**
     * return all users
     * 
     * @return all users only name an public pgp
     */
    @GetMapping("/groups")
    public List<GroupModel> all(@RequestHeader("Authorization") String authorization) {
        UserModel user = UserController.getUserByAuthorization(UserController.getAllUsers(), authorization);
        return getAllGroups().stream()
                .filter(g -> g.getUserRole().containsKey(user.getName()))
                .toList();
    }

    /**
     * return all users
     * 
     * @return all users only name an public pgp
     */
    @GetMapping("/group/{id}")
    public GroupModel oneGroup(@PathVariable long id, @RequestHeader("Authorization") String authorization) {
        UserController.getUserByAuthorization(UserController.getAllUsers(), authorization);
        return isUserInGroup(getUserLogin(authorization), groupById(getAllGroups(), id)) ? groupById(getAllGroups(), id)
                : null;
    }

    // ## End GETs
    // ## Begin POSTs

    /**
     * create new group return id and user
     * 
     * @param name          of gorup
     * @param authorization of user
     * @return group created
     */
    @PostMapping("/group")
    public GroupModel createGroup(@RequestParam(required = true) String name,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        Long id = (long) (groups.size() == 0 ? 0 : assignId());

        GroupModel newGroup = new GroupModel(id, name,
                UserController.getUserByAuthorization(UserController.getAllUsers(), authorization));
        groups.add(newGroup);
        saveGroups(groups);
        return newGroup;
    }

    /**
     * add user to a group
     * 
     * @param id            of group
     * @param name          of new user (if has bit 2)
     * @param roles         of new user by default set 3
     * @param authorization of user
     * @return group with new user
     */
    @PostMapping("/group/{id}")
    public GroupModel addUser(@PathVariable long id,
            @RequestParam(required = true) String name,
            @RequestParam(required = false) Byte roles,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group)
                && RoleModel.isBitOn((byte) 2, group.getUserRoles(user))) {
            if (roles == null) {
                group.addUser(UserController.getUserByName(UserController.getAllUsers(), name));
            } else {
                group.addUser(UserController.getUserByName(UserController.getAllUsers(), name), roles);
            }
            saveGroups(groups);
            return group;
        }
        return null;
    }

    // ## End POSTs
    // ## Begin PUTs

    /**
     * update group
     * 
     * @param id            of group
     * @param name          of user to update
     * @param authorization of user
     * @return group updated
     */
    @PutMapping("/group/{id}")
    public GroupModel updateRole(@PathVariable long id,
            @RequestParam(required = true) String name,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group)
                && RoleModel.isBitOn((byte) 5, group.getUserRoles(user))) {
            group.setName(name);
            saveGroups(groups);
            return group;
        }
        return null;
    }

    /**
     * update role of user
     * 
     * @param id            of group
     * @param name          new to set
     * @param authorization of user
     * @return group updated
     */
    @PutMapping("/group/{id}/{name}")
    public GroupModel updateGroup(@PathVariable long id,
            @PathVariable String name,
            @RequestParam(required = true) Byte roles,
            @RequestHeader("Authorization") String authorization) {
        UserModel userAuth = getUserLogin(authorization),
                userUpdate = UserController.getUserByName(UserController.getAllUsers(), name);
        List<GroupModel> groups = getAllGroups();
        GroupModel group = groupById(groups, id);
        if ((isUserInGroup(userAuth, group) && isUserInGroup(userUpdate, group))
                && RoleModel.isBitOn((byte) 3, group.getUserRoles(userAuth))
                && userUpdate != null) {
            group.setUserRoles(userUpdate, new RoleModel(roles));
            saveGroups(groups);
            return group;
        }
        return null;
    }

    // ## End PUTs
    // ## Begin Delete

    /**
     * delete specific group
     * 
     * @param id            of group
     * @param authorization of user
     * @return if is deleted or not
     */
    @DeleteMapping("/group/{id}")
    public boolean deleteGroup(@PathVariable long id,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group) && RoleModel.isBitOn((byte) 6,
                group.getUserRoles(user))) {
            groups.remove(group);
            saveGroups(groups);
            return true;
        }
        return false;
    }

    /**
     * delete your user of specific group
     * 
     * @param id            of group
     * @param authorization of user
     * @return if is deleted or not
     */
    @DeleteMapping("/exit/{id}")
    public boolean exitGroup(@PathVariable long id,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group)) {
            group.deleteUser(user);
            saveGroups(groups);
            return true;
        }
        return false;
    }

    /**
     * delete a user of specific group
     * 
     * @param id            of group
     * @param name          of user to delete
     * @param authorization of user
     * @return if is deleted or not
     */
    @DeleteMapping("/group/{id}/{name}")
    public boolean deleteUserOfGroup(@PathVariable long id,
            @PathVariable String name,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization),
                userDelete = UserController.getUserByName(UserController.getAllUsers(), name);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group) && RoleModel.isBitOn((byte) 4,
                group.getUserRoles(user))) {
            group.deleteUser(userDelete);
            saveGroups(groups);
            return true;
        }
        return false;
    }

    // ## End Delete
    // # End Group

    // # Begin Messages
    // ## Begin GETs

    /**
     * get all messages
     * 
     * @param id            of gorup
     * @param authorization of user
     * @return all messages
     */
    @GetMapping("/messages/{id}")
    public List<MessageModel> getMessages(
            @PathVariable long id,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group) && RoleModel.isBitOn((byte) 0,
                group.getUserRoles(user))) {
            return getAllMessages(id);
        }
        return null;
    }

    // ## End GETs
    // ## Begin POSTs

    /**
     * post new messages
     * 
     * @param id            of group
     * @param message       to post
     * @param authorization of user
     */
    @PostMapping("/send/{id}")
    public void postMessages(
            @PathVariable long id,
            @RequestParam(required = true) String message,
            @RequestHeader("Authorization") String authorization) {
        List<GroupModel> groups = getAllGroups();
        UserModel user = getUserLogin(authorization);
        GroupModel group = groupById(groups, id);
        if (isUserInGroup(user, group) && RoleModel.isBitOn((byte) 1,
                group.getUserRoles(user))) {
            List<MessageModel> messages = null;
            MessageModel newMessage = new MessageModel(message);
            if (newMessage.getContent() != null) {
                messages = getAllMessages(id);
                messages.add(newMessage);
                saveMessages(messages, id);
            }
        }
    }

    // ## End POSTs
    // # End Messages

    // # Begin Misc

    /**
     * assign id
     * 
     * @return next id
     */
    private Long assignId() {
        return getAllGroups().stream()
                .mapToLong(GroupModel::getId)
                .max()
                .orElse(-1) + 1;
    }

    /**
     * get user by autoritation
     * 
     * @param authorization to check
     * @return
     */
    private UserModel getUserLogin(String authorization) {
        return UserController.getUserByAuthorization(UserController.getAllUsers(), authorization);
    }

    /**
     * check if a user is in the group
     * 
     * @param user  to chek
     * @param group to chek
     * @return if is in or not
     */
    private boolean isUserInGroup(UserModel user, GroupModel group) {
        return group.getUserRole().containsKey(user.getName());
    }

    /**
     * get group by id
     * 
     * @param id to search
     * @return group
     */
    private GroupModel groupById(List<GroupModel> groups, Long id) {
        return groups.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<GroupModel> getAllGroups() {
        return Files4Models.loadFile(
                Files4Models.MAPER,
                Files4Models.GROUPSFILE,
                new TypeReference<List<GroupModel>>() {
                });
    }

    /**
     * get all messages of group
     * 
     * @param id of group
     * @return all messages of gorup
     */
    public static List<MessageModel> getAllMessages(long id) {
        return Files4Models.loadFile(
                Files4Models.MAPER,
                Files4Models.MESSAGESPATH + id + ".json",
                new TypeReference<List<MessageModel>>() {
                });
    }

    public static void saveGroups(List<GroupModel> groups) {
        Files4Models.saveFile(Files4Models.MAPER, groups, Files4Models.GROUPSFILE);
    }

    /**
     * save messages
     * 
     * @param messages to save
     * @param id       of gorup
     */
    public static void saveMessages(List<MessageModel> messages, long id) {
        Files4Models.saveFile(Files4Models.MAPER, messages, Files4Models.MESSAGESPATH + id + ".json");
    }

    // # End Misc

}

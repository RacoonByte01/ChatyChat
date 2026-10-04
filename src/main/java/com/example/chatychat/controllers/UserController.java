package com.example.chatychat.controllers;

import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import java.security.SecureRandom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.chatychat.models.UserModel;
import com.example.chatychat.utils.Files4Models;
import com.example.chatychat.utils.Views;

/**
 * 
 * UserController
 * 
 */
@RestController
public class UserController {

    // # Begin User
    // ## Begin GETs

    /**
     * return all users
     * 
     * @return all users only name an public pgp
     */

    @GetMapping("/users")
    @JsonView(Views.Public.class)
    List<UserModel> all() {
        return getAllUsers();
    }

    /**
     * return only information of especific user
     * 
     * @param name specific name of user to search
     * @return the specific user only name an public pgp
     */
    @GetMapping("/user/{name}")
    @JsonView(Views.Public.class)
    UserModel oneUser(@PathVariable String name) {
        return getAllUsers().stream()
                .filter(u -> name.equals(u.getName()))
                .findFirst()
                .orElse(null);
    }

    // ## End GETs
    // ## Begin POSTs

    /**
     * create new user
     * 
     * @param name     of the new user
     * @param password of the new user
     * @return the new user
     */
    @PostMapping("/user")
    UserModel newUser(@RequestParam String name, @RequestParam String password) {
        List<UserModel> users = getAllUsers();
        UserModel newUser = null;
        if (isUserNameUse(name)) {
            newUser = new UserModel(name, password, false);
            users.add(newUser);
        }
        saveUsers(users);
        return newUser;
    }

    // ## End POSTs
    // ## Begin PUTs

    /**
     * update params of a specific user
     * 
     * @param name          name of user to update
     * @param new_name      new name user
     * @param password      new password user
     * @param pgp           new pgp user
     * @param pgp_null      if true set pgp to null
     * @param authorization token of user
     * @return the update user
     */
    @PutMapping("/user")
    UserModel updateUser(@RequestParam(required = false) String name,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String pgp,
            @RequestParam(required = false) boolean pgp_null,
            @RequestHeader("Authorization") String authorization) {
        List<UserModel> users = getAllUsers();
        UserModel user = getUserByAuthorization(users, authorization);
        if (name != null) {
            if (isUserNameUse(name)) {
                user.setName(name);
            }
        }
        if (password != null) {
            user.setPassword(password, false);
        }
        if (pgp != null) {
            user.setPgp(pgp);
        }
        if (pgp_null) {
            user.setPgpNull();
        }
        saveUsers(users);
        return user;
    }

    // ## End PUTs
    // ## Begin DELETEs

    /**
     * delete especific user
     * 
     * @param authorization token of delete user
     * @return the delete user
     */
    @DeleteMapping("/user")
    boolean deleteUser(@RequestHeader("Authorization") String authorization) {
        List<UserModel> users = getAllUsers();
        UserModel user = getUserByAuthorization(users, authorization);
        users.remove(user);
        saveUsers(users);
        return true;
    }

    // ## End DELETEs
    // # End User

    // # Begin token
    // ## Begin GETs

    /**
     * get all tokens of user
     * 
     * @param authorization token of user
     * @return
     */
    @GetMapping("/tokens")
    public List<String> getAllTokens(@RequestHeader("Authorization") String authorization) {
        UserModel user = getUserByAuthorization(getAllUsers(), authorization);
        return user.getToken();
    }

    // ## End GETs
    // ## Begin POSTs

    /**
     * create new token for user
     * 
     * @param name     of user to login
     * @param password of password to login
     * @return token
     */
    @PostMapping("/login")
    public String login(@RequestParam(required = true) String name,
            @RequestParam(required = true) String password) {
        List<UserModel> users = getAllUsers();
        String token = "a";
        UserModel loginUser = users.stream()
                .filter(u -> name.equals(u.getName()))
                .findFirst().orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
        if (loginUser.isMyPassword(password)) {
            SecureRandom random = new SecureRandom();

            byte[] bytes = new byte[32];
            random.nextBytes(bytes);

            token = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(bytes);

            loginUser.addToken(token);
        }
        saveUsers(users);
        return token;
    }

    // ## End POSTSs
    // ## Begin DELETEs

    /**
     * delete specific token
     * 
     * @param authorization token of user
     * @param token         token to delete
     * @return if is delete
     */
    @DeleteMapping("/token")
    public boolean deleteToken(@RequestHeader("Authorization") String authorization,
            @RequestParam(required = true) String token) {
        List<UserModel> users = getAllUsers();
        UserModel user = getUserByAuthorization(users, token);
        boolean isDeleted = user.deleteToken(token);
        saveUsers(users);
        return isDeleted;
    }

    /**
     * delete the token of autentication
     * 
     * @param authorization toke to delete
     * @return if is delete
     */
    @DeleteMapping("/logout")
    public boolean deleteToken(@RequestHeader("Authorization") String authorization) {
        List<UserModel> users = getAllUsers();
        UserModel user = getUserByAuthorization(users, authorization);
        boolean isDeleted = user.deleteToken(authorization);
        saveUsers(users);
        return isDeleted;
    }

    // ## End DELETEs
    // # End token

    // # Begin MISC

    /**
     * is user name in use
     * 
     * @param name name to search
     * @return if is in use
     */
    private static boolean isUserNameUse(String name) {
        return getAllUsers().stream()
                .filter(u -> name.equals(u.getName()))
                .toList()
                .size() == 0;
    }

    /**
     * get user by token
     * 
     * @param users all
     * @param token to search
     * @return user find or null
     */
    public static UserModel getUserByAuthorization(List<UserModel> users, String token) {
        return users.stream()
                .filter(u -> u.getToken().contains(token))
                .findFirst()
                .orElse(null);
    }

    /**
     * get user by name
     * 
     * @param users list where search
     * @param name  name of user to search
     * @return the user with the name
     */
    public static UserModel getUserByName(List<UserModel> users, String name) {
        UserModel result = null;
        if (!isUserNameUse(name)) {
            result = users.stream()
                    .filter(u -> u.getName().equals(name))
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }

    /**
     * get all users
     * 
     * @return get all users save in file
     */
    public static List<UserModel> getAllUsers() {
        return (List<UserModel>) Files4Models.loadFile(Files4Models.MAPER, Files4Models.USERSFILE,
                new TypeReference<List<UserModel>>() {
                });
    }

    /**
     * save list of users in a file
     * 
     * @param users list to save
     */
    public static void saveUsers(List<UserModel> users) {
        Files4Models.saveFile(Files4Models.MAPER, users, Files4Models.USERSFILE);
    }

    // # End MISC

}

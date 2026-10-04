package com.example.chatychat.models;

import java.util.ArrayList;
import java.util.List;

import com.example.chatychat.utils.Hash;;

/**
 * 
 * UserModel
 * 
 */
public class UserModel extends Model {

    private String name;
    private String password;
    private String pgp;
    private List<String> token;

    /**
     * basic constructor to save file
     */
    public UserModel() {
    }

    /**
     * constructor with pgp
     * 
     * @param name     name of user
     * @param password password of user
     * @param pgp      pgp of user
     * @param isHash   if password is hash no rehash
     */
    public UserModel(String name, String password, String pgp, boolean isHash) {
        setName(name);
        setPassword(password, isHash);
        setPgp(pgp);
    }

    /**
     * constructor without pgp
     * 
     * @param name     name of user
     * @param password password of user
     * @param isHash   if password is hash no rehash
     */
    public UserModel(String name, String password, boolean isHash) {
        setName(name);
        setPassword(password, isHash);
    }

    /**
     * set name
     * 
     * @param name new name of user not null
     */
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    /**
     * set new password if is hash no rehash the password
     * 
     * @param password new password
     * @param isHash   if true no rehash false hash plain text
     */
    public void setPassword(String password, boolean isHash) {
        if (password != null) {
            if (isHash) {
                this.password = password;
            } else {
                this.password = Hash.get_SHA512(password);
            }
        }
    }

    /**
     * set new pgp not null
     * 
     * @param pgp new pgp
     */
    public void setPgp(String pgp) {
        if (pgp != null) {
            this.pgp = pgp;
        }
    }

    /**
     * delete pgp (value to null)
     */
    public void setPgpNull() {
        this.pgp = null;
    }

    /**
     * add new token to the list of the user
     * 
     * @param token
     */
    public void addToken(String token) {
        if (this.token == null) {
            this.token = new ArrayList<String>();
        }
        this.token.add(token);
    }

    /**
     * delete token of a user
     * 
     * @param token
     * @return
     */
    public boolean deleteToken(String token) {
        boolean response;
        if (this.token != null) {
            this.token.remove(token);
            response = true;
        } else {
            response = false;
        }
        return response;
    }

    /**
     * get name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get password
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * get pgp
     * 
     * @return pgp
     */
    public String getPgp() {
        return pgp;
    }

    /**
     * get token list
     * 
     * @return token list
     */
    public List<String> getToken() {
        return token;
    }

    /**
     * check if a plain text is the hash password
     * 
     * @param password password to check
     * @return if hash equal or not
     */
    public boolean isMyPassword(String password) {
        return Hash.get_SHA512(password).equals(this.password);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.example.chatychat.models.Model#toString()
     * @return string with all information
     */
    @Override
    public String toString() {
        return getName() + " " + getPassword() + " " + getPgp();
    }

}

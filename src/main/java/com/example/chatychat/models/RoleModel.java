package com.example.chatychat.models;

import com.example.chatychat.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 
 * RoleModel
 */
public class RoleModel extends Model {

    static byte MAXBYTES = 7;

    @JsonView(Views.Public.class)
    private Byte roles;

    /**
     * basic constructor to save file
     */
    public RoleModel() {
    }

    /**
     * constructor with especific roles
     * 
     * @param roles
     */
    public RoleModel(byte roles) {
        if (!setRoles(roles)) {
            this.roles = 3;
        }
    }

    /**
     * set roles and check if it is within range
     * 
     * @param roles new roles to set
     * @return if can set or not
     */
    public boolean setRoles(byte roles) {
        boolean result = isDecInRange(roles);
        if (result) {
            this.roles = roles;
        }
        return result;
    }

    /**
     * get roles
     * 
     * @return roles dec number
     */
    public Byte getRoles() {
        return roles;
    }

    /**
     * check if specific bit is on or off
     * 
     * @param bit to chek
     * @return value of bit and null if bit is not in range
     */
    public Boolean isBitOn(byte bit) {
        boolean[] bits = decToBinary(this.roles);
        Boolean result = null;
        if (bits != null && isBitInRange(bit)) {
            result = bits[bit];
        }
        return result;
    }

    /**
     * check if specific bit is on or off
     * 
     * @param bit to chek
     * @return value of bit and null if bit is not in range
     */
    public static Boolean isBitOn(byte bit, byte roles) {
        boolean[] bits = decToBinary(roles);
        Boolean result = null;
        if (bits != null && isBitInRange(bit)) {
            result = bits[bit];
        }
        return result;
    }

    /**
     * translate decimal number to binary number
     * 
     * @param dec to translate
     * @return array of bytes
     */
    private static boolean[] decToBinary(byte dec) {
        boolean[] bits = new boolean[MAXBYTES];
        for (int i = 0; i < MAXBYTES; i++) {
            bits[i] = (dec & (1 << i)) != 0;
        }
        return bits;
    }

    /**
     * check if the given decimal is within the range
     * 
     * @param dec to chek
     * @return if chek is correct
     */
    private static boolean isDecInRange(byte dec) {
        return dec >= 0 && dec < Math.pow(2, MAXBYTES);
    }

    /**
     * check if the given bit is within the range
     * 
     * @param bit to chek
     * @return if chek is correct
     */
    private static boolean isBitInRange(byte bit) {
        return bit >= 0 && bit < MAXBYTES;
    }

    /**
     * (non-Javadoc)
     * return string of object
     * 
     * @see com.example.chatychat.models.Model#toString()
     */
    @Override
    public String toString() {
        return this.roles.toString();
    }

    /**
     * compare 2 roles
     * 
     * @param obj role
     * @return if roles is equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RoleModel other)) {
            return false;
        }
        return this.roles.equals(other.roles);
    }

}

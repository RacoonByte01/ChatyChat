package com.example.chatychat.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Savemodels
 */
public class Files4Models {

    public static ObjectMapper MAPER = new ObjectMapper();
    public static String USERSFILE = "data/users.json";
    public static String GROUPSFILE = "data/groups.json";
    public static String MESSAGESPATH = "data/groups/";

    /**
     * save the var in a file
     * 
     * @param mapper object to file
     * @param object to save
     * @param file   where save
     * @param view   view can see
     */
    public static <T> void saveFile(ObjectMapper mapper, T object, String file, Class<?> view) {
        try {
            mapper.writerWithView(view)
                    .writeValue(new File(file), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * save the var in a file
     * 
     * @param mapper object to file
     * @param boject to save
     * @param file   where save
     */
    public static <T> void saveFile(ObjectMapper mapper, T object, String file) {
        try {
            mapper.writeValue(new File(file), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * load the file and set in var
     * 
     * @param mapper        file to object
     * @param file          to read
     * @param typeReference type of data
     * @return var in file
     */
    public static <T> List<T> loadFile(
            ObjectMapper mapper,
            String file,
            TypeReference<List<T>> typeReference) {

        try {
            return mapper.readValue(new File(file), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}

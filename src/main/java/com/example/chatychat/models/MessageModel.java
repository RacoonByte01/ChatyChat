package com.example.chatychat.models;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 
 * MessageModel
 */
public class MessageModel extends Model {

    private String content;
    private LocalDateTime date_post;

    /**
     * basic constructor to save file
     */
    public MessageModel() {
    }

    /**
     * create message by content
     * 
     * @param content
     */
    public MessageModel(String content) {
        setContent(content);
        date_post = LocalDateTime.now();
    }

    /**
     * get content
     * 
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * get date post
     * 
     * @return date
     */
    public String getDate_post() {
        return date_post.toString();
    }

    /**
     * set content
     * 
     * @param content
     */
    public void setContent(String content) {
        if (isPGP(content)) {
            this.content = content;
        }
    }

    /**
     * get content and check if is pgp block
     * 
     * @param content to ceck
     * @return if is or not
     */
    private boolean isPGP(String content) {
        Set<String> indicators = Set.of(
                "-----BEGIN PGP PUBLIC KEY BLOCK-----",
                "-----BEGIN PGP PRIVATE KEY BLOCK-----");

        return indicators.stream()
                .anyMatch(content::contains);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.example.chatychat.models.Model#toString()
     */
    @Override
    public String toString() {
        return content + ", " + date_post.toString() + "\n";
    }

}
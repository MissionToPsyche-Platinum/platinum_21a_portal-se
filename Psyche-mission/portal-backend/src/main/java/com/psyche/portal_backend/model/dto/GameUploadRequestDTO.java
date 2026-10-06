package com.psyche.portal_backend.model.dto;

public class GameUploadRequestDTO {
    private String title;
    private String thumbnail;
    private String genre;
    private String difficulty;
    private String age;
    private String className;
    private String credits;
    private String gtype;
    private String engine;
    private String description;
    private String src;
    private String video;

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getGenre() {
        return genre;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getAge() {
        return age;
    }

    public String getClassName() {
        return className;
    }

    public String getCredits() {
        return credits;
    }

    public String getGtype() {
        return gtype;
    }

    public String getEngine() {
        return engine;
    }

    public String getDescription() {
        return description;
    }

    public String getSrc() {
        return src;
    }

    public String getVideo() {
        return video;
    }
}
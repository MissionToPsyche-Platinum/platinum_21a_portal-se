package com.psyche.portal_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "games")
public class Game {

    @Id
    private String id;
    private String title;
    private String genre;
    private String difficulty;
    private String age;
    private String className;
    private String credits;
    private String gtype;
    private String engine;

    // Annotations needed to allow more than the 255 character limit
    // JPA maps String to VARCHAR(255)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String thumbnail;   //URL

    @Column(columnDefinition = "TEXT")
    private String video;       //URL

    @Column(columnDefinition = "TEXT")
    private String src;     //URL

    

    public Game() {}

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public String getAge() { return age; }
    public String getClassName() { return className; }
    public String getCredits() { return credits; }
    public String getGtype() { return gtype; }
    public String getThumbnail() { return thumbnail; }
    public String getVideo() { return video; }
    public String getSrc() { return src; }
    public String getEngine() { return engine; }


    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setDescription(String description) { this.description = description; }
    public void setAgeGroup(String age) { this.age = age; }
    public void setClassName(String className) { this.className = className; }
    public void setCredits(String credits) { this.credits = credits; }
    public void setGtype(String gtype) { this.gtype = gtype; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public void setVideo(String video) { this.video = video; }
    public void setGameSrc(String src) { this.src = src; }
    public void setEngine(String engine) { this.engine = engine; }

}

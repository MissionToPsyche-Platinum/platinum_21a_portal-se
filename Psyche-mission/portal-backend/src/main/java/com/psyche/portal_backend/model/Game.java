package com.psyche.portal_backend.model;

<<<<<<< HEAD
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private String difficulty;
    private String description;
    private String age;
    private String className;
    private String credits;
    private String gtype;
    private String thumbnail;   //URL
    private String video;       //URL
    private String src;     //URL

    public Game() {}

    public Long getId() { return id; }
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


    public void setId(Long id) { this.id = id; }
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
=======
public class Game {
    private String id;  // will probably want to change to a number once DB is running
    private String title;
    private String genre;
    private String difficulty;
    private String description;
    private String ageGroup;
    private String className;
    private String credits;
    private String gtype;
    private String thumbnail;   //URL
    private String video;       //URL
    private String gameSrc;     //URL

    public Game() {}
>>>>>>> dev-backend
}

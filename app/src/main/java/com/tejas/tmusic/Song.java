package com.tejas.tmusic;

import java.io.Serializable;
import java.util.ArrayList;

public class Song implements Serializable {
    String name;
    String duration;
    String path;

//    Constructor
    public Song(String path, String name, String duration) {
        this.name = name;
        this.duration = duration;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public void setSize(String path) {
        this.path = path;
    }

}

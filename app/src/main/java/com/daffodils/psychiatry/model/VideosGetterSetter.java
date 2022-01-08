package com.daffodils.psychiatry.model;

public class VideosGetterSetter {

    String CourseName, ModuleName, VideoName, VideoPath;

    public VideosGetterSetter(String CourseName, String ModuleName, String VideoName, String VideoPath){
        this.CourseName = CourseName;
        this.ModuleName = ModuleName;
        this.VideoName = VideoName;
        this.VideoPath = VideoPath;

    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }
}

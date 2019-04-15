package com.example.InClass_06;

import java.util.Date;

public class article
{

    String  title,description,urlToImage,publishedAt;


    public article() {
    }

    @Override
    public String toString() {
        return "article{title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }
}

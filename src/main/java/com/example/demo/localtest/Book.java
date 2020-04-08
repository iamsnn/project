package com.example.demo.localtest;

import net.minidev.json.JSONObject;

import java.util.Map;


public class Book {
  private String alert = "red";
  private String isbn;
  private String description; //V
  private String[] genre;
  private String[] review;
  private String recommend;
  private String author; //V
  private String title;
  private String score;
  private String year;
  private String rating;
  private String reviewers;
  private String img; //V
  private String url; //V

  public Book() {
  }

  public Book(Map<String,Object> map){
    for(Map.Entry<String,Object> e:map.entrySet()){
    }
  }

  public Book(String alert, String isbn, String description, String[] genre, String[] review, String recommend, String author, String title, String score, String year, String rating, String reviewers, String img, String url) {
    this.alert = alert;
    this.isbn = isbn;
    this.description = description;
    this.genre = genre;
    this.review = review;
    this.recommend = recommend;
    this.author = author;
    this.title = title;
    this.score = score;
    this.year = year;
    this.rating = rating;
    this.reviewers = reviewers;
    this.img = img;
    this.url = url;
  }

  public JSONObject get(){
    JSONObject jsonObj = new JSONObject();
    jsonObj.put("alert",this.alert);
    jsonObj.put("isbn",this.isbn);
    jsonObj.put("description", this.description);
    jsonObj.put("genre", this.genre);
    jsonObj.put("review", this.review);
    jsonObj.put("recommend", this.recommend);
    jsonObj.put("author", this.author);
    jsonObj.put("title", this.title);
    jsonObj.put("score", this.score);
    jsonObj.put("year", this.year);
    jsonObj.put("rating", this.rating);
    jsonObj.put("reviewers",this.reviewers);
    jsonObj.put("img",this.img);
    jsonObj.put("url",this.url);

    return jsonObj;
  }

  public void setAlert(String alert) {
    this.alert = alert;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setGenre(String[] genre) {
    this.genre = genre;
  }

  public void setReview(String[] review) {
    this.review = review;
  }

  public void setRecommend(String recommend) {
    this.recommend = recommend;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public void setReviewers(String reviewers) {
    this.reviewers = reviewers;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

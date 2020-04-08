package com.example.demo.Controller;

import com.example.demo.localtest.Data;
import com.example.demo.localtest.ESearch;
import com.example.demo.localtest.Params;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
public class HelloAngularController {
  private Params p;

  public HelloAngularController() {
    Params p =new Params();
  }

  @GetMapping(value = "search")
  public JSONArray fetch(@RequestParam(name = "id", required = true) String id) throws IOException {

    System.out.println(id);
    JSONArray jsonArray = new JSONArray();
    jsonArray = process(id);

    return jsonArray;
  }

  // searchString#RADIOBUTTON#author#year#title#startPage
  // desc rev
  // return
  private JSONArray process(String id) throws IOException {

    JSONArray jsonArray = new JSONArray();
    ESearch search = new ESearch();

    String[] income = id.split(":");

    String input = income[0].trim();
    String radioSelection = income[1];
    String author = income[2].trim();
    String year = income[3].trim();
    String searchTitle = income[4];
    int startPage = Integer.valueOf(income[5]);

    if (searchTitle.equals("withTitle")) {
      if (!input.equals("undefined") && input.length() >= 2) {
        //search whole content with input
        jsonArray = ESearch.customSearch(input, "tit", "", "", startPage);
      }
      else {
        //search with all
        jsonArray = ESearch.allSearch(startPage);

      }
    }
    else {
      if (!input.equals("undefined") && input.length() >= 2) {
        if (radioSelection.equals("rev")) {
          //search with selected review
          //maybe with author and year
          jsonArray = ESearch.customSearch(input, "rev", author, year, startPage);
        }
        else {
          //search with selected description
          //maybe with author and year
          jsonArray = ESearch.customSearch(input, "desc", author, year, startPage);
        }
      }
      else {
        if (author.equals("") && year.equals("")) {
          //search with all
          jsonArray = ESearch.allSearch(startPage);
        }
        else {
          jsonArray = ESearch.customSearch("undefined", "", author, year, startPage);
        }
      }
    }

    return jsonArray;

  }

  @GetMapping(value = "feedback")
  public void myFeedBack(@RequestParam(name = "id", required = true) String id) throws IOException {

    String[] income = id.split(":");

    String input = income[0].trim();
    String radioSelection = income[1];
    String author = income[2].trim();
    String year = income[3].trim();
    String searchTitle = income[4];
    String search = input+"#"+radioSelection+"#"+author+"#"+year+"#"+searchTitle;
      int vote = Integer.valueOf(income[5]);
      p.point(search,vote);
  }

  @GetMapping(value = "trend")
  public JSONArray trend(@RequestParam(name = "id") String id){
    //top 3
    //TODO
    Data d = new Data();
    //URL + clickNumber + "img" + "author" + "title" + "description"
    d.addRes(new String[]{"https://www.goodreads.com/book/show/20578940-the-iron-trial","5",
            "https://i.gr-assets.com/images/S/compressed.photo.goodreads" +
                    ".com/books/1526511151l/20578940._SY475_.jpg","Holly Black  Cassandra Clare",
            "The Iron Trial","All his life, Call has been warned by his father to stay away from magic. To succeed at the Iron Trial and be admitted into the vaunted Magisterium school would bring bad things. But he fails at failing. Only hard work, loyal friends, danger, and a puppy await..."});
    d.addRes(new String[]{"https://www.goodreads.com/book/show/31233.The_Holcroft_Covenant","4","https://i.gr-assets.com/images/S/compressed.photo.goodreads" +
            ".com/books/1348888874l/31233.jpg","Robert Ludlum","The Holcroft Covenant","The " +
            "Second World War is over. Three top Nazis have embezzled $780 million from Nazi " +
            "funds - and then committed suicide. Their Covenant, to be inherited by their three eldest children, is to redistribute this vast wealth amongst the survivors of the Holocaust. Reparation must be made..."});
    d.addRes(new String[]{"https://www.goodreads.com/book/show/142750.The_Client","2","https://i" +
            ".gr-assets.com/images/S/compressed.photo.goodreads.com/books/1172144487l/142750" +
            "._SY475_.jpg","John Grisham","The Client","Mark Sway is 11 and knows where a body has been hidden. If the FBI can find the body, then they can prove that it was a Mafia murder, but Mark is too scared to tell the truth, so he hires a lawyer who must protect him both from the law and from the killers..."});
    System.out.println(d.top3().size());
    return d.top3();
  }

}

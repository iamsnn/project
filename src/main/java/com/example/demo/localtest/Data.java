package com.example.demo.localtest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.PriorityQueue;

public class Data {
  PriorityQueue pq = new PriorityQueue<String[]>((a,b)->{
    return Integer.valueOf(b[1])-Integer.valueOf(a[1]);
  });

  public Data(){
  }
  // URL + clickNumber + "img" + "author" + "title" + "description" +
  public void addRes(String[] uri){
    pq.add(uri);
  }
  public JSONArray top3(){
    JSONArray jsonArray = new JSONArray();
    if(pq.size()<3){
      return jsonArray;
    }
    else{
      String[][] tmp = new String[3][6];
      for(int i=0;i<3;i++){
        JSONObject jsonObject = new JSONObject();
        String[] t = (String[]) pq.poll();
        tmp[i] = t;
        jsonObject.put("url",t[0]);
        jsonObject.put("img",t[2]);
        jsonObject.put("author",t[3]);
        jsonObject.put("title",t[4]);
        jsonObject.put("description",t[5]);
        jsonArray.add(jsonObject);
      }
      for(int i=0;i<3;i++){
        pq.add(tmp[i]);
      }
    }

    return jsonArray;
  }
}

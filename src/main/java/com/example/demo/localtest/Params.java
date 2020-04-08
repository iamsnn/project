package com.example.demo.localtest;

import java.util.Map;

public class Params {
  private int accuraccy = 0;
  private Map<String,Integer> result;

  public Params() {
  }

  public int getAccuraccy() {
    return accuraccy;
  }

  public void setAccuraccy(int accuraccy) {
    this.accuraccy = accuraccy;
  }

  public void point(String search,int vote){

    if(result.containsKey(search)){
      result.put(search,result.get(search)+vote);
    }
    else{
      result.put(search,0);
      result.put(search,result.get(search)+vote);
    }

  }
}

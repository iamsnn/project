package com.example.demo.localtest;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ESearch {

  public static RestHighLevelClient init() throws IOException {

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http")));
    return client;
  }

  public static void main(String[] args) throws IOException {
  }

  public static JSONArray customSearch(String input,String signal,String author,String year,
                                       int page) throws IOException {
    RestHighLevelClient client = init();
    SearchRequest searchRequestAll = new SearchRequest();
    searchRequestAll.indices("book");
    searchRequestAll.types("_doc");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    if(input.equals("undefined")){
      BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
      if(!year.equals("")){
        boolQueryBuilder.should(new MatchQueryBuilder("year",year));
      }
      if(!author.equals("")){
        boolQueryBuilder.should(new MatchQueryBuilder("author",author).boost(50f).operator(Operator.OR));
      }
      searchSourceBuilder.query(boolQueryBuilder);
    }

    else{
      //based on the title
      if(signal.equals("tit")){
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", input)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(5)
                .maxExpansions(5)
                .minimumShouldMatch("85%")
                .zeroTermsQuery(MatchQuery.ZeroTermsQuery.ALL);

        searchSourceBuilder.query(matchQueryBuilder);
      }
      //based on the review, use nest
      //String path, QueryBuilder query, ScoreMode scoreMode
      else if(signal.equals("rev")){
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.nestedQuery("review",
                boolQueryBuilder1.must(QueryBuilders.matchQuery("review.r", input).minimumShouldMatch("75%").fuzziness(Fuzziness.AUTO).maxExpansions(15).operator(Operator.AND).zeroTermsQuery(MatchQuery.ZeroTermsQuery.ALL)), ScoreMode.Max).innerHit(new InnerHitBuilder()));

        if(!year.equals("")){
          boolQueryBuilder.should(new MatchQueryBuilder("year",year));
        }
        if(!author.equals("")){
          boolQueryBuilder.should(new MatchQueryBuilder("author",author).boost(50f).operator(Operator.OR));
        }
        searchSourceBuilder.query(boolQueryBuilder);
      }
      //based on the decription
      else{
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();


        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("description", input)
                .fuzziness(Fuzziness.AUTO)
                .maxExpansions(20)
                .minimumShouldMatch("75%")
                .prefixLength(5);
        boolQueryBuilder.must(matchQueryBuilder);


        if(!year.equals("")){
          boolQueryBuilder.should(new MatchQueryBuilder("year",year));
        }
        if(!author.equals("")){
          boolQueryBuilder.should(new MatchQueryBuilder("author",author).boost(50f).operator(Operator.OR));
        }

        searchSourceBuilder.query(boolQueryBuilder);
      }
    }

    searchSourceBuilder.from((page-1)*8);
    searchSourceBuilder.size(8);
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
    searchRequestAll.source(searchSourceBuilder);
    SearchResponse searchResponseAll = client.search(searchRequestAll, RequestOptions.DEFAULT);

    JSONArray jsonArray = new JSONArray();
    for (SearchHit hit : searchResponseAll.getHits()) {
      Map<String, Object> source = hit.getSourceAsMap();
      JSONObject jsonObject = new JSONObject();
      if (!source.isEmpty()) {
        for (Iterator<Map.Entry<String, Object>> it = source.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry<String, Object> entry = it.next();

          if(entry.getKey().equals("title")||entry.getKey().equals("author")||entry.getKey().equals(
                  "img")||entry.getKey().equals("url")||entry.getKey().equals("description")){

            if(entry.getKey().equals("description")){
              if(entry.getValue().toString().length()>200){
                jsonObject.put(entry.getKey(),entry.getValue().toString().substring(0,201)+"...");
              }
              else{
                jsonObject.put(entry.getKey(),entry.getValue());
              }
            }
            else{
              jsonObject.put(entry.getKey(),entry.getValue());
            }

          }

        }
      }
      else{
        break;
      }
      jsonArray.add(jsonObject);
    }

    client.close();
    return jsonArray;
  }

  public static JSONArray allSearch(int page) throws IOException {

    RestHighLevelClient client = init();

    SearchRequest searchRequestAll = new SearchRequest();
    searchRequestAll.indices("book");
    searchRequestAll.types("_doc");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.from((page-1)*8);
    searchSourceBuilder.size(8);
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    //绑定
    searchRequestAll.source(searchSourceBuilder);
    // 同步查询
    SearchResponse searchResponseAll = client.search(searchRequestAll, RequestOptions.DEFAULT);

    JSONArray jsonArray = new JSONArray();

    for (SearchHit hit : searchResponseAll.getHits()) {
      Map<String, Object> source = hit.getSourceAsMap();
      JSONObject jsonObject = new JSONObject();
      System.out.println(1);
      if (!source.isEmpty()) {
        for (Iterator<Map.Entry<String, Object>> it = source.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry<String, Object> entry = it.next();

          if(entry.getKey().equals("title")||entry.getKey().equals("author")||entry.getKey().equals(
                  "img")||entry.getKey().equals("url")||entry.getKey().equals("description")){
            if(entry.getKey().equals("description")){
              if(entry.getValue().toString().length()>200){
                jsonObject.put(entry.getKey(),entry.getValue().toString().substring(0,201)+"...");
              }
              else{
                jsonObject.put(entry.getKey(),entry.getValue());
              }
            }
            else{
              jsonObject.put(entry.getKey(),entry.getValue());
            }
          }

        }
      }
      else{
        break;
      }
      jsonArray.add(jsonObject);
    }

    client.close();
    return jsonArray;
  }
}

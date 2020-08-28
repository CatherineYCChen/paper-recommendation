// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.PaperConfig;


import java.io.IOException;
import java.io.FileReader;
import java.io.Reader;

import java.util.List;



import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import com.google.gson.Gson;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/config")
public class DataServlet extends HttpServlet {
  @Override
  public void init(){
      Gson gson = new Gson();
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      
      Query query = new Query("nconfig").setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, "0704.2083"));
      PreparedQuery results = datastore.prepare(query);
      if (results.countEntities() > 0){
          return;
      }
      ClassLoader classLoader = getClass().getClassLoader();
      try (Reader a = new FileReader(classLoader.getResource("nlp_bert_new.json").getFile())){
                PaperConfig[] myconfig = gson.fromJson(a, PaperConfig[].class);
                for (PaperConfig i:myconfig){
                    Entity configEntity = new Entity("nconfig");
                    configEntity.setProperty("id", i.getId());
                    configEntity.setProperty("authors",i.getAuthors());
                    configEntity.setProperty("submitter",i.getSubmitter());
                    configEntity.setProperty("title", i.getTitle());
                    configEntity.setProperty("comments", i.getComments());
                    configEntity.setProperty("journalRef", i.getJournalRef());
                    configEntity.setProperty("doi", i.getDoi());
                    configEntity.setProperty("abstract", new Text(i.getAbstract()));
                    configEntity.setProperty("reportNo", i.getReportNo());
                    configEntity.setProperty("categories", i.getCategories());
                    configEntity.setProperty("recommend", i.getRecommend());

                    datastore.put(configEntity);
                }
      }catch (IOException e) {
            e.printStackTrace();
    }
    
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String id = request.getParameter("id");    
      PaperConfig p_config = query(id);  
      response.setContentType("text/html;");
      String pageHtml = "";
      pageHtml += "<!DOCTYPE html>";
      pageHtml += "<html>";
      pageHtml +=  "<head>";
      pageHtml+= "<meta charset=\"UTF-8\">";
      pageHtml+="<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">";
      pageHtml+="<link rel=\"stylesheet\" href=\"style.css\">";
      pageHtml+="<link rel=\"stylesheet\" href=\"w3.css\">";
      pageHtml+="<link rel=\"stylesheet\" href=\"t1.css\">";


      pageHtml+=String.format("<title>%s</title>", p_config.getTitle());
      pageHtml+="<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>";
      pageHtml+="<script src=\"https://www.gstatic.com/charts/loader.js\"></script>";
      pageHtml+="<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">";
      pageHtml+="<script src=\"myscript.js\"></script>"; 
      pageHtml+= "</head>";
      pageHtml+="<body onload=\"samesite()\">";
      pageHtml+="<div class=\"header1\">";
      pageHtml+="<i class=\"fa fa-newspaper-o\" style=\"font-size:40px\"></i>";
      pageHtml+="<h1>Paper Recommendation System</h1>";
      pageHtml+="<a href='index.html' style =\"color:white\"><i class=\"fa fa-home\" style=\"font-size:60px\"></i></a>";
      pageHtml+="</div>";
      
      pageHtml+="<h2 style=\"padding: 0px 15px\">Paper Information</h2>";
      pageHtml+="<div class=\"w3-container\">";
      pageHtml+=String.format("<h3><span>%s</span></h3>", p_config.getTitle());
      pageHtml+=String.format("<p><b>Author:</b> %s</p>", p_config.getSubmitter());
      pageHtml+=String.format("<p><b>DOI:</b>  %s</p>", p_config.getDoi());
      pageHtml+=String.format("<p><b>Comments:</b>  %s</p>", p_config.getComments());
      pageHtml+=String.format("<p>Get from <a href=https://arxiv.org/abs/%s>airXiv</a></p>", p_config.getId());
      pageHtml+=String.format("<p><b>Abstract:</b> %s</p>", p_config.getAbstract());
      pageHtml+="</div>";


      pageHtml+="<h2 style=\"padding: 0px 15px\">Papers You Might Like</h2>";

      pageHtml+="<div class=\"full hide-scroll\">";
      pageHtml+="<ul class='hs'>";

      for(String rec:p_config.getRecommend()){
         PaperConfig rec_config = query(rec);  
         pageHtml+="<li class=\"item\">";
         pageHtml+="<div class=\"w3-container\">";
         pageHtml+=String.format("<h3>%s</h3>", rec_config.getTitle());
         pageHtml+=String.format("<p class=\"w3-opacity\">%s</p>", rec_config.getCategories());
         pageHtml+=String.format("<p>%s", rec_config.getAbstract().substring(0,500))+"...</p>";
         pageHtml+="<p><button class=\"w3-button w3-light-grey w3-block\"><a href=/config?id="+rec+">Read More</a></button></p>";
         pageHtml+="</div></li>";
      }

      pageHtml+="</ul>";
      pageHtml+="</div>";

      pageHtml+="<h2 style=\"padding: 0px 15px\">Tell Us How Useful is This Paper?</h2>";
      pageHtml+="<div class=\"rate\">";
      pageHtml+="<input type=\"radio\" id=\"star5.0\" name=\"rate\" value=\"5\" />";
      pageHtml+="<label for=\"star5.0\" title=\"text\">5 stars</label>";
      pageHtml+="<input type=\"radio\" id=\"star4.0\" name=\"rate\" value=\"4\" />";
      pageHtml+="<label for=\"star4\" title=\"text\">4 stars</label>";
      pageHtml+="<input type=\"radio\" id=\"star3.0\" name=\"rate\" value=\"3\" />";
      pageHtml+="<label for=\"star3.0\" title=\"text\">3 stars</label>";
      pageHtml+="<input type=\"radio\" id=\"star2.0\" name=\"rate\" value=\"2\" />";
      pageHtml+="<label for=\"star2.0\" title=\"text\">2 stars</label>";
      pageHtml+="<input type=\"radio\" id=\"star1.0\" name=\"rate\" value=\"1\" />";
      pageHtml+="<label for=\"star1.0\" title=\"text\">1 star</label>";
      pageHtml+="</div>";


      pageHtml+="<h2 style=\"padding: 0px 15px\">Contents</h2>";
      pageHtml+="</div>";
      pageHtml+= String.format("<p align='center'><embed src=https://drive.google.com/viewerng/viewer?embedded=true&url=https://arxiv.org/pdf/%s.pdf ", p_config.getId())+"frameborder=0 width=80% height=1000></embed></p>";
      pageHtml+="</body>";
      pageHtml+="</html>";
    response.getWriter().println(pageHtml);
    
  }
  private PaperConfig query(String id){
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      Query query = new Query("nconfig").setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
      PreparedQuery results = datastore.prepare(query);
      Entity configEntity = results.asSingleEntity();
      Text abs = (Text) configEntity.getProperty("abstract");
      String abss = abs.getValue();
      PaperConfig p_config = new PaperConfig((String) configEntity.getProperty("id"), (String)configEntity.getProperty("submitter"), 
                                             (String)configEntity.getProperty("authors"),  (String)configEntity.getProperty("title"), (String)configEntity.getProperty("comments"),
                                             (String)configEntity.getProperty("doi"), (String) configEntity.getProperty("categories"), abss, 
                                             (String)configEntity.getProperty("journalRef"), (String)configEntity.getProperty("reportNo"), (List<String>)configEntity.getProperty("recommend"));

      return p_config;
  }
}

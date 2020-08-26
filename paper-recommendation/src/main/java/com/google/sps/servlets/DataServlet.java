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
import com.google.gson.JsonElement;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/config")
public class DataServlet extends HttpServlet {
//private ArrayList<String> quotes = new ArrayList<String>();
  @Override
  public void init(){
      Gson gson = new Gson();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query("config").setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, "0704.0394"));
      PreparedQuery results = datastore.prepare(query);
      if (results.countEntities() > 0){
          return;
      }
      ClassLoader classLoader = getClass().getClassLoader();
      try (Reader a = new FileReader(classLoader.getResource("configById.json").getFile())){
                PaperConfig[] myconfig = gson.fromJson(a, PaperConfig[].class);
                for (PaperConfig i:myconfig){
                    Entity configEntity = new Entity("config");
                    configEntity.setProperty("id", i.getId());
                    configEntity.setProperty("submitter",i.getSubmitter());
                    configEntity.setProperty("title", i.getTitle());
                    configEntity.setProperty("comments", i.getComments());
                    configEntity.setProperty("journalRef", i.getJournalRef());
                    configEntity.setProperty("doi", i.getDoi());
                    configEntity.setProperty("abstract", i.getAbstract());
                    configEntity.setProperty("reportNo", i.getReportNo());
                    configEntity.setProperty("categories", i.getCategories());
                    configEntity.setProperty("versions", i.getVersions());
                    datastore.put(configEntity);
                }
      }catch (IOException e) {
            e.printStackTrace();
    }
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      //System.out.println(System.getProperty("user.dir")+"/../configById.json");
      Gson gson = new Gson();
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

      String id = request.getParameter("id");
      Query query = new Query("config").setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
      PreparedQuery results = datastore.prepare(query);
      Entity configEntity = results.asSingleEntity();
      PaperConfig p_config = new PaperConfig((String) configEntity.getProperty("id"), (String)configEntity.getProperty("submitter"), (String)configEntity.getProperty("title"), (String)configEntity.getProperty("comments"), (String)configEntity.getProperty("journalRef"), (String)configEntity.getProperty("doi"), (String)configEntity.getProperty("abstract"),
       (String)configEntity.getProperty("reportNo"),(List<String>)configEntity.getProperty("categories"), (List<String>)configEntity.getProperty("versions"));
      String jsonInString = gson.toJson(p_config);

      //response.setContentType("application/json;");
      //response.getWriter().println(jsonInString);
      response.setContentType("text/html;");
      String pageHtml = "";
      pageHtml += "<!DOCTYPE html>";
      pageHtml += "<html>";
      pageHtml +=  "<head>";
      pageHtml+= "<meta charset=\"UTF-8\">";
      pageHtml+="<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">";
      pageHtml+="<link rel=\"stylesheet\" href=\"style.css\">";
      pageHtml+=String.format("<title>%s</title>", p_config.getTitle());
      pageHtml+="<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>";
      pageHtml+="<script src=\"https://www.gstatic.com/charts/loader.js\"></script>";
      pageHtml+="<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">";
      pageHtml+="<script src=\"myscript.js\"></script>"; 
      pageHtml+= "</head>";
      pageHtml+="<body>";
      pageHtml+="<div class=\"header1\">";
      pageHtml+="<i class=\"fa fa-newspaper-o\" style=\"font-size:40px\"></i>";
      pageHtml+="<h1>Paper Recommendation System</h1>";
      pageHtml+="<a href='index.html' style =\"color:white\"><i class=\"fa fa-home\" style=\"font-size:60px\"></i></a>";
      pageHtml+="</div>";
      
      pageHtml+="<h2 style=\"padding: 0px 15px\">Paper Information</h2>";
      pageHtml+="<div class=\"dropdown\">";
      pageHtml+=String.format("<span>%s</span>", p_config.getTitle());
      pageHtml+="<div class=\"dropdown-content\">";
      pageHtml+=String.format("<p><b>Author:</b> %s</p>", p_config.getSubmitter());
      pageHtml+=String.format("<p><b>DOI:</b>  %s</p>", p_config.getDoi());
      pageHtml+=String.format("<p><b>Comments:</b>  %s</p>", p_config.getComments());
      pageHtml+=String.format("<p>Get from <a href=https://arxiv.org/abs/%s>airXiv</a></p>", p_config.getId());
      pageHtml+=String.format("<p><b>Abstract:</b> %s</p>", p_config.getAbstract());
      pageHtml+="</div>";
      pageHtml+="<br>";

      pageHtml+="<h2 style=\"padding: 0px 15px\">Related Topics</h2>";
      pageHtml+="<br>";

      pageHtml+=String.format("<embed src='https://arxiv.org/pdf/%s.pdf' width=\"800px\" height=\"2100px\" style=\"margin:auto\" style = \"display:block\" />", p_config.getId());
      pageHtml+="</div>";
      pageHtml+="</body>";
      pageHtml+="</html>";
    response.getWriter().println(pageHtml);

  }
}

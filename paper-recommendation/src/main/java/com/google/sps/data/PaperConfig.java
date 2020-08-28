package com.google.sps.data;

import java.util.List;

public final class PaperConfig {

  private final String id;
  private final String submitter;
  private final String authors;
  private final String title;
  private final String comments;
  private final String doi;
  private final String categories;
  private final String abstract_;
  private final String journalRef;
  private final String reportNo;
  private final List<String> recommend;

  public PaperConfig(String id, String submitter, String authors, String title, String comments, String doi, String categories,String abstract_,String journalRef,   String reportNo,  List<String> recommend) {
    this.id = id;
    this.authors = authors;
    this.submitter = submitter;
    this.title = title;
    this.comments = comments;
    this.journalRef = journalRef;
    this.doi = doi;
    this.abstract_ = abstract_;
    this.reportNo = reportNo;
    this.categories = categories;
    this.recommend = recommend;

  }
  public String getId() {
    return id;
  }
  public String getSubmitter() {
    return submitter;
  }
  public String getAuthors() {
    return authors;
  }
  
  public String getTitle() {
    return title;
  }
  public String getComments() {
    return comments;
  }
   public String getDoi() {
    return doi;
  }
  public String getCategories() {
    return categories;
  }
  public String getAbstract() {
    return abstract_;
  }
  
  public String getJournalRef() {
    return journalRef;
  }
  public String getReportNo() {
    return reportNo;
  }
  public List<String> getRecommend() {
    return recommend;
  }
  
}

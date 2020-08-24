package com.google.sps.data;

import java.util.List;

public final class PaperConfig {

  private final String id;
  private final String submitter;
  private final String title;
  private final String comments;
  private final String journalRef;
  private final String doi;
  private final String abstract_;
  private final String reportNo;
  private final List<String> categories;
  private final List<String> versions;

  public PaperConfig(String id, String submitter, String title, String comments, String journalRef, String doi, String abstract_, String reportNo, List<String> categories, List<String> versions) {
    this.id = id;
    this.submitter = submitter;
    this.title = title;
    this.comments = comments;
    this.journalRef = journalRef;
    this.doi = doi;
    this.abstract_ = abstract_;
    this.reportNo = reportNo;
    this.categories = categories;
    this.versions = versions;
  }
  public String getId() {
    return id;
  }

  public String getSubmitter() {
    return submitter;
  }
  public String getTitle() {
    return title;
  }
  public String getComments() {
    return comments;
  }
  public String getJournalRef() {
    return journalRef;
  }
  public String getDoi() {
    return doi;
  }
  public String getAbstract() {
    return abstract_;
  }
  public String getReportNo() {
    return reportNo;
  }
  public List<String> getCategories() {
    return categories;
  }
  public List<String> getVersions() {
    return versions;
  }
}

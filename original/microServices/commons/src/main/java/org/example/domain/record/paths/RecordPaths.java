package org.example.domain.record.paths;

public final class RecordPaths {
  public static final String BASE = "/api/v1/record";
  public static final String GET_ALL = BASE;
  public static final String GET_BY_ID = BASE + "/{id}";
  public static final String CREATE = BASE;
  public static final String DELETE = BASE + "/{id}";

  private RecordPaths() {}
}
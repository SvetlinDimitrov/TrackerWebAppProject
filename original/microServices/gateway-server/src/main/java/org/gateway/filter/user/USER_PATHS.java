package org.gateway.filter.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum USER_PATHS {
  MAIN_PATH("/api/v1/user"),
  ME("/api/v1/user"),
  CREATE("/api/v1/user"),
  UPDATE("/api/v1/user/{id}"),
  DELETE("/api/v1/user/{id}"),
  GET("/api/v1/user/{id}/");

  public final String path;
}

package org.trackerwebapp.trackerwebapp.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionMessages {
  INVALID_STRING_LENGTH_MESSAGE("Invalid length. Length must be between 1 and 255 characters "),
  INVALID_TEXT_LENGTH_MESSAGE("Invalid length. Length must be between 1 and 65 535 characters "),
  INVALID_NUMBER_MESSAGE("Invalid number "),
  ;
  private final String message;
}

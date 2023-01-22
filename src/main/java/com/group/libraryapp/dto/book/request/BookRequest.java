package com.group.libraryapp.dto.book.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

  private String name;

  public String getName() {
    return name;
  }

}

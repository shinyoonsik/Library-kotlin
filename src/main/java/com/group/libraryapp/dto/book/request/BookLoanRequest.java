package com.group.libraryapp.dto.book.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanRequest {

  private String userName;
  private String bookName;

  public String getUserName() {
    return userName;
  }

  public String getBookName() {
    return bookName;
  }

}

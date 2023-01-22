package com.group.libraryapp.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class UserLoanHistory {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne
  private User user;

  private String bookName;

  private boolean isReturn; // false: 반납X, true: 반납O

  public UserLoanHistory() { }

  public UserLoanHistory(User user, String bookName, boolean isReturn) {
    this.user = user;
    this.bookName = bookName;
    this.isReturn = isReturn;
  }

  public String getBookName() {
    return this.bookName;
  }

  public void doReturn() {
    this.isReturn = true;
  }

  public boolean isReturn() { return this.isReturn; }

}

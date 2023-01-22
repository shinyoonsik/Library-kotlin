package com.group.libraryapp.repository;

import com.group.libraryapp.entity.user.UserLoanHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoanHistoryRepository extends JpaRepository<UserLoanHistory, Long> {

  UserLoanHistory findByBookNameAndIsReturn(String bookName, boolean isReturn);

}

package com.group.libraryapp.service.book;

import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import com.group.libraryapp.entity.book.Book;
import com.group.libraryapp.entity.user.User;
import com.group.libraryapp.entity.user.UserLoanHistory;
import com.group.libraryapp.repository.BookRepository;
import com.group.libraryapp.repository.UserLoanHistoryRepository;
import com.group.libraryapp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class BookServiceTest {

    final String USER_NAME = "leo";
    final String BOOK_NAME = "이것이 자바다";

    BookService bookService;
    BookRepository bookRepository;

    UserRepository userRepository;
    UserLoanHistoryRepository userLoanHistoryRepository;

    @Autowired
    public BookServiceTest(BookRepository bookRepository, BookService bookService, UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
    }

    @AfterEach
    void cleanDataBase() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("도서 저장기능 테스트")
    void testSaveBook() {
        // given
        BookRequest request = new BookRequest(BOOK_NAME);

        // when
        bookService.saveBook(request);

        // then
        List<Book> results = bookRepository.findAll();
        assertThat(results.size()).isGreaterThan(0);
        assertThat(results.get(0).getName()).isEqualTo(BOOK_NAME);
    }

    @Test
    @DisplayName("도서 대출 기능 테스트")
    void testLoanBook(){
        // given
        BookLoanRequest request = new BookLoanRequest(USER_NAME, BOOK_NAME);
        bookRepository.save(new Book(BOOK_NAME));
        userRepository.save(new User(USER_NAME, null));

        // when
        bookService.loanBook(request);

        // then
        List<UserLoanHistory> results = userLoanHistoryRepository.findAll();
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getBookName()).isEqualTo(BOOK_NAME);
        assertThat(results.get(0).isReturn()).isFalse();
    }

    @Test
    @DisplayName("도서 대출 Exception 테스트; 이미 대출되어 있다")
    void testExceptionForLoanBook(){
        // given
        BookLoanRequest request = new BookLoanRequest(USER_NAME, BOOK_NAME);
        User savedUser = User.builder()
                .age(32)
                .name(USER_NAME)
                .build();

        userRepository.save(savedUser);
        bookRepository.save(new Book(BOOK_NAME));
        userLoanHistoryRepository.save(new UserLoanHistory(savedUser, BOOK_NAME, false));

        // when & then
        assertThatThrownBy(() -> bookService.loanBook(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 대출되어 있는 책입니다");
    }

    @Test
    @Disabled
    @DisplayName("mocking & stubbing 도서 대출기능 테스트")
    void testLoanBookWithMockingAndStubbing() {
        // mocking & stubbing 실패...
        // 원인 spring data jpa의 이해 부족
        // bookRepository, userLoanHistoryRepository, userRepository

        // given
        Book book = new Book(BOOK_NAME);
        User user = new User(USER_NAME, null);
        BookLoanRequest request = BookLoanRequest.builder()
                .bookName(BOOK_NAME)
                .userName(USER_NAME)
                .build();

        given(bookRepository.findByName(request.getBookName())).willReturn(Optional.of(book));
        given(userLoanHistoryRepository.findByBookNameAndIsReturn(request.getBookName(), false)).willReturn(null);
        given(userRepository.findByName(request.getUserName())).willReturn(Optional.of(user));

        // when
        bookService.loanBook(request);

        // then
        assertThat(userLoanHistoryRepository.findAll().get(0).getBookName()).isEqualTo(BOOK_NAME);
    }

    @Test
    @DisplayName("도서 반납 테스트")
    void testReturnBook(){
        // given
        BookReturnRequest request = new BookReturnRequest(USER_NAME, BOOK_NAME);
        User user = User.builder()
                .age(33)
                .name(USER_NAME)
                .build();
        userRepository.save(user);
        userLoanHistoryRepository.save(new UserLoanHistory(user, BOOK_NAME, false));

        // when
        bookService.returnBook(request);

        // then
        List<UserLoanHistory> results = userLoanHistoryRepository.findAll();
        assertThat(results.get(0).getBookName()).isEqualTo(BOOK_NAME);
        assertThat(results.get(0).isReturn()).isTrue();
    }
}
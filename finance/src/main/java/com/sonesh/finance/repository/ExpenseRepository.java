package com.sonesh.finance.repository;

import com.sonesh.finance.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId")
    Double getTotalByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :start AND :end")
    Double getTotalByUserBetweenDates(@Param("userId") Long userId,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end);

    List<Expense> findByUserIdAndCategory(Long userId, String category);

    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId GROUP BY e.category")
    List<Object[]> getCategoryTotals(@Param("userId") Long userId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.email = :email
          AND YEAR(e.date) = :year
          AND MONTH(e.date) = :month
    """)
    Double getMonthlyExpenseTotal(@Param("email") String email,
                                  @Param("year") int year,
                                  @Param("month") int month);

    @Query("""
        SELECT e.date, COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.email = :email
          AND YEAR(e.date) = :year
          AND MONTH(e.date) = :month
        GROUP BY e.date
        ORDER BY e.date
    """)
    List<Object[]> getDailyExpenseHeatmap(@Param("email") String email,
                                          @Param("year") int year,
                                          @Param("month") int month);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.email = :email
          AND YEAR(e.date) = :year
          AND MONTH(e.date) = :month
    """)
    Double getMonthlyTotal(@Param("email") String email,
                           @Param("year") int year,
                           @Param("month") int month);
}
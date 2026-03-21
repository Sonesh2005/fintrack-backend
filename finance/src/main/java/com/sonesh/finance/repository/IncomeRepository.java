package com.sonesh.finance.repository;

import com.sonesh.finance.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserId(Long userId);

    List<Income> findByUserIdAndCategory(Long userId, String category);

    Optional<Income> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    // ✅ Total income
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user.id = :userId")
    Double getTotalByUser(@Param("userId") Long userId);

    // ✅ Monthly total (date range)
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user.id = :userId AND i.date BETWEEN :start AND :end")
    Double getTotalByUserBetweenDates(@Param("userId") Long userId,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end);

    // ✅ NEW: Monthly total income (used for Insights / Health Score)
    @Query("""
        SELECT COALESCE(SUM(i.amount), 0)
        FROM Income i
        WHERE i.user.email = :email
          AND YEAR(i.date) = :year
          AND MONTH(i.date) = :month
    """)
    Double getMonthlyIncomeTotal(@Param("email") String email,
                                 @Param("year") int year,
                                 @Param("month") int month);
}
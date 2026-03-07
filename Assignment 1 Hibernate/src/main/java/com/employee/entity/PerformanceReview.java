package com.employee.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entity class mapped to the 'performance_review' table.
 * employeeCode is stored as a normal column (NO foreign key mapping).
 */
@Entity
@Table(name = "performance_review")
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @Column(name = "rating", nullable = false)
    private int rating; // 1 to 5

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Column(name = "comments", length = 500)
    private String comments;

    // ========================
    // Constructors
    // ========================

    public PerformanceReview() {
    }

    public PerformanceReview(String employeeCode, int rating,
                             LocalDate reviewDate, String comments) {
        this.employeeCode = employeeCode;
        setRating(rating); // Validates rating range
        this.reviewDate = reviewDate;
        this.comments = comments;
    }

    // ========================
    // Getters and Setters
    // ========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public int getRating() {
        return rating;
    }

    /**
     * Sets rating with validation (must be between 1 and 5).
     */
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5. Given: " + rating);
        }
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // ========================
    // toString
    // ========================

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-12s | %-6d | %-12s | %-30s |",
            id, employeeCode, rating, reviewDate, comments
        );
    }

    /**
     * Returns a detailed string representation.
     */
    public String toDetailString() {
        return "--------------------------------------------\n" +
               "  ID            : " + id + "\n" +
               "  Employee Code : " + employeeCode + "\n" +
               "  Rating        : " + rating + " / 5" + "\n" +
               "  Review Date   : " + reviewDate + "\n" +
               "  Comments      : " + comments + "\n" +
               "--------------------------------------------";
    }
}

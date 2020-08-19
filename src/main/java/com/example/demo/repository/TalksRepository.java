package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TalksRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Boolean exists(String talkId) {
        List<Boolean> results = jdbcTemplate.query(
                "SELECT 1 FROM talks WHERE id = ?",
                (row, i) -> true,
                talkId
        );
        return !results.isEmpty();
    }
}

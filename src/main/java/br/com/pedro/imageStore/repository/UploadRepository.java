package br.com.pedro.imageStore.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UploadRepository  {

    private final JdbcClient jdbcClient;

    public UploadRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void insert(String objectId){
        jdbcClient.sql("""
            INSERT INTO images (object_id) VALUES (:id)
        """)
        .param("id",objectId).update();
    }

}

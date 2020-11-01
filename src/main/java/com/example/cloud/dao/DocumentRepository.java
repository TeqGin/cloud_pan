package com.example.cloud.dao;

import com.example.cloud.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

public interface DocumentRepository extends JpaRepository<Document, String> {
    @Query(value = "select * from document where uper_id = ?1",nativeQuery = true)
    List<Document> findDocumentsByUserId(String account);

    @Query(value = "select * from document where type_name = ?1",nativeQuery = true)
    List<Document> findDocumentsByTypeName(String typeName);

    @Query(value = "select * from document where file_name like %?1%",nativeQuery = true)
    List<Document> findDocumentsLikeFileName(String key);
}

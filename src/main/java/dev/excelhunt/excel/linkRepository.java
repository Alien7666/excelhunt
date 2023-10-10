package dev.excelhunt.excel;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface linkRepository extends MongoRepository<link, String> {
    List<link> findAll();  // 這個方法將返回所有的 link 文檔，但因為只有一個，所以可以用它來取得那個唯一的 link
}

package dev.excelhunt.excel;

import org.springframework.data.mongodb.repository.MongoRepository;



public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByPwd(String pwd);
}

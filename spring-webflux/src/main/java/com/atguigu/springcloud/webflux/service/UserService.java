package com.atguigu.springcloud.webflux.service;

import com.atguigu.springcloud.webflux.dao.entity.User;
import com.atguigu.springcloud.webflux.dao.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<User> insertUser(User user) {
        return userRepository.insert(user);
    }

    public Mono<User> updateUser(User user) {
        return userRepository.save(user);
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public Flux<User> findByUserNameAndNote(String userName, String note) {
        return userRepository.findByUserNameLikeAndNoteLike(userName, note);
    }

}

package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final List<Post> posts = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public List<Post> all() {
        return posts;
    }

    public Optional<Post> getById(long id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findAny();
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(idCounter.incrementAndGet());
            posts.add(post);
            return post;
        } else {
            posts.stream()
                    .filter(p -> p.getId() == post.getId())
                    .findFirst()
                    .ifPresent(p -> p.setContent(post.getContent()));
            return post;
        }
    }

    public void removeById(long id) {
        posts.removeIf(post -> post.getId() == id);
    }
}

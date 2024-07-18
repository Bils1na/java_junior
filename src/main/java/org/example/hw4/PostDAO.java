package org.example.hw4;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PostDAO {

    public void savePost(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Post> getAllPosts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Post", Post.class).list();
        }
    }

    public Post getPostById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Post.class, id);
        }
    }

    public void deletePost(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Post post = session.get(Post.class, id);
            if (post != null) {
                session.delete(post);
                System.out.println("Post is deleted");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Post> getPostsByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Post where user.id = :userId", Post.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}

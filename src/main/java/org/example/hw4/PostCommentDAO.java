package org.example.hw4;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PostCommentDAO {

    public void savePostComment(PostComment postComment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(postComment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<PostComment> getAllPostComments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PostComment", PostComment.class).list();
        }
    }

    public PostComment getPostCommentById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(PostComment.class, id);
        }
    }

    public void deletePostComment(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            PostComment postComment = session.get(PostComment.class, id);
            if (postComment != null) {
                session.delete(postComment);
                System.out.println("PostComment is deleted");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<PostComment> getCommentsByPostId(Long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PostComment where post.id = :postId", PostComment.class)
                    .setParameter("postId", postId)
                    .list();
        }
    }

    public List<PostComment> getCommentsByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from PostComment where user.id = :userId", PostComment.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public List<User> getUsersCommentedByUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select distinct p.user from PostComment pc join pc.post p where pc.user.id = :userId", User.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }
}

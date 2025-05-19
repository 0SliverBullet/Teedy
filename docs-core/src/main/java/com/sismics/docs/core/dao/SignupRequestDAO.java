/**
 * Reference: Inspired by implementation in the repository:
 *            https://github.com/Jayfeather233/Teedy
 */
package com.sismics.docs.core.dao;

import com.sismics.docs.core.constant.AuditLogType;
import com.sismics.docs.core.model.jpa.SignupRequest;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.util.AuditLogUtil;
import com.sismics.util.context.ThreadLocalContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class SignupRequestDAO {
    private static final Logger log = LoggerFactory.getLogger(SignupRequestDAO.class);

    private void insertUser(SignupRequest sr) throws Exception {
        UserDao ud = new UserDao();
        if (ud.findUser(sr.getUserName())) {
            throw new IllegalArgumentException("Username exists!");
        }
        User u = new User();
        u.setCreateDate(new Date());
        u.setUsername(sr.getUserName());
        u.setPassword(sr.getPassword());
        u.setEmail("");
        u.setRoleId("user");
        u.setStorageQuota(1024L*1024*1024);  // default StorageQuota

        try {
            ud.create(u, sr.getRequestId());
        } catch (Exception e) {
            log.error("Error create user", e);
            throw e;
        }
    }

    public List<SignupRequest> getSignupRequest() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from SignupRequest u where u.acceptDate is null and u.rejectDate is null");

        @SuppressWarnings("unchecked")
        List<SignupRequest> results = q.getResultList();
        return results;

        // 只返回处于“待审核”状态的注册请求
    }

    public List<SignupRequest> getAllSignupRequest() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from SignupRequest u");

        @SuppressWarnings("unchecked")
        List<SignupRequest> results = q.getResultList();
        return results;

        // 查询数据库中所有注册请求，不管状态
    }

    public void addRequest(String userName, String password) {
        UserDao ud = new UserDao();
        if (ud.findUser(userName)) {
            throw new IllegalArgumentException("Username exists!");
        }
        SignupRequest sr = new SignupRequest(userName, new Date());
        sr.setPassword(password);
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(sr);
        AuditLogUtil.create(sr, AuditLogType.CREATE, sr.getRequestId());
        
        // 添加一个新的注册请求
    }

    public void acceptRequest(String id) throws Exception {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from SignupRequest u where u.id = :id and u.acceptDate is null and u.rejectDate is null");
        q.setParameter("id", id);
        try {
            SignupRequest sr = (SignupRequest) q.getSingleResult();
            sr.setAcceptDate(new Date());

            insertUser(sr);
        } catch (Exception e) {
            log.error("Error sign up user", e);
            throw e;
        }

        /*
         审核通过某个注册请求，执行两个操作：

            设置 acceptDate 表示请求完成

            调用 insertUser(sr) 创建真正的系统用户
         */
    }

    public void rejectRequest(String id){
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select u from SignupRequest u where u.id = :id and u.acceptDate is null and u.rejectDate is null");
        q.setParameter("id", id);

        SignupRequest sr = (SignupRequest) q.getSingleResult();
        sr.setRejectDate(new Date());

        // 拒绝注册请求
    }


}
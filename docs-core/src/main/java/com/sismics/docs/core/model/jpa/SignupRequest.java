package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "T_SIGNUP_USER_REQUEST")
public class SignupRequest implements Loggable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "SIG_ID_C", nullable = false, unique = true)
    private String requestId;

    @Column(name = "SIG_USERNAME_C", nullable = false, unique = true)
    private String userName;

    @Column(name = "SIG_PASSWORD_C", nullable = false)
    private String password;

    @Column(name = "SIG_CREATEDATE_D", nullable = false)
    private Date createDate;   // create

    @Column(name = "SIG_ACCEPTDATE_D")
    private Date acceptDate;   // accept

    @Column(name = "SIG_REJECTDATE_D")
    private Date rejectDate;   // reject

    @Column(name = "SIG_DELETEDATE_D")
    private Date deleteDate;   // delete


    public SignupRequest() {}

    public SignupRequest(String userName, Date createDate) {
        this.userName = userName;
        this.createDate = createDate;
    }

    public String getRequestId() { return requestId; }
    public SignupRequest setRequestId(String id) { this.requestId = id; return this; }

    public String getUserName() { return userName; }
    public SignupRequest setUserName(String userName) { this.userName = userName; return this; }

    public String getPassword() { return password; }
    public SignupRequest setPassword(String password) { this.password = password; return this; }

    public Date getCreateDate() { return createDate;}
    public SignupRequest setCreateDate(Date createDate) { this.createDate = createDate; return this; }

    public Date getAcceptDate() { return acceptDate; }
    public SignupRequest setAcceptDate(Date acceptDate) { this.acceptDate = acceptDate; return this; }

    public Date getRejectDate() { return rejectDate; }
    public SignupRequest setRejectDate(Date rejectDate) { this.rejectDate = rejectDate; return this; }

    @Override
    public Date getDeleteDate() { return deleteDate; }
    public SignupRequest setDeleteDate(Date deleteDate) { this.deleteDate = deleteDate; return this; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requestId", requestId)
                .add("username", userName)
                .toString();
    }

    @Override
    public String toMessage() {
        return this.toString();
    }
}

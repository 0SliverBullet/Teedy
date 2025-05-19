package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.SignupRequestDAO;
import com.sismics.docs.core.model.jpa.SignupRequest;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/signup")
public class UserSignupResource extends BaseResource {

    @GET
    @Path("/list")
    public Response GetSignupList() {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        checkBaseFunction(BaseFunction.ADMIN);

        SignupRequestDAO srd = new SignupRequestDAO();
        List<SignupRequest> lsr = srd.getSignupRequest();
        JsonArrayBuilder ret = Json.createArrayBuilder();
        for(SignupRequest sr : lsr) {
            ret.add(Json.createObjectBuilder()
                    .add("id", sr.getRequestId())
                    .add("username", sr.getUserName())
                    .add("create_date", sr.getCreateDate().toString()));
        }

        return Response.ok().entity(ret.build()).build();
    }

    @GET
    @Path("/all")
    public Response GetAllSignupList() {

        if (!authenticate()) {
            throw new ForbiddenClientException();
        }

        checkBaseFunction(BaseFunction.ADMIN);

        SignupRequestDAO srd = new SignupRequestDAO();
        List<SignupRequest> lsr = srd.getAllSignupRequest();
        JsonArrayBuilder ret = Json.createArrayBuilder();
        for(SignupRequest sr : lsr) {
            ret.add(Json.createObjectBuilder()
                    .add("id", sr.getRequestId() == null ? "null" : sr.getRequestId())
                    .add("username", sr.getUserName() == null ? "null" : sr.getUserName())
                    .add("create_date", sr.getCreateDate() == null ? "null" : sr.getCreateDate().toString())
                    .add("accept_date", sr.getAcceptDate() == null ? "null" : sr.getAcceptDate().toString())
                    .add("reject_date", sr.getRejectDate() == null ? "null" : sr.getRejectDate().toString()));
        }

        return Response.ok().entity(ret.build()).build();
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("username") String username,
                           @FormParam("password") String password) {

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username and password are required.")
                    .build();
        }

        SignupRequestDAO srd = new SignupRequestDAO();
        try {
            srd.addRequest(username, password);
        } catch (Exception e) {
            throw new ServerException("Signup Error", "failed to create the signup request" + e.getMessage(), e);
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("/accept")
    public Response accept(@QueryParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        SignupRequestDAO srd = new SignupRequestDAO();
        try {
            srd.acceptRequest(id);
        } catch (Exception e) {
            throw new ServerException("Signup Error", "failed to accept the signup request", e);
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("/reject")
    public Response deny(@QueryParam("id") String id) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        SignupRequestDAO srd = new SignupRequestDAO();
        try {
            srd.rejectRequest(id);
        } catch (Exception e) {
            throw new ServerException("Signup Error", "failed to reject the signup request", e);
        }
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "ok");
        return Response.ok().entity(response.build()).build();
    }
}
package web.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import web.infrastructure.entity.Grade;
import web.service.ReactiveGradeTestService;

import java.util.List;

/**
 * @author : Mario.Yu
 * @description : Simple Test Usage
 * @date : 2023/10/6 上午 11:06
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/GradeTest")
public class GradeTestController {

    @Inject
    ReactiveGradeTestService reactiveGradeTestService;

    @GET
    @Path("Grades")
    public Uni<List<Grade>> list() {
        return reactiveGradeTestService.list();
    }

    @POST
    @Path("CreateGrade")
    public Uni<List<Grade>> addGrade(Grade grade){

        return reactiveGradeTestService.add(grade)
                .onItem()
                .ignore()
                .andSwitchTo(this::list);
    }
}

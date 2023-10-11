package web.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import web.infrastructure.entity.Grade;
import web.service.GradeTestService;
import web.service.ReactiveGradeTestService;

import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/11 下午 03:35
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/GradeTestController")
public class GradeTestController {

    @Inject
    GradeTestService gradeTestService;


    @GET
    @Path("Grades")
    public List<Grade> list() {
        return gradeTestService.list();
    }
}

package io.github.manoelpiovesan.resources;

import io.github.manoelpiovesan.entities.Paper;
import io.github.manoelpiovesan.repositories.PaperRepository;
import io.github.manoelpiovesan.utils.Role;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * @author Manoel Rodrigues
 */
@RequestScoped
@Path("/paper")
public class PaperResource {


    @Inject
    JsonWebToken jwt;


    @Inject
    PaperRepository paperRepository;

    /**
     * List all papers
     *
     * @param term String
     * @param page int
     * @param size int
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response listAll(
            @QueryParam("term") String term,
            @QueryParam("author") String author,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("15") int size,
            @QueryParam("projected") @DefaultValue("true") boolean projected

    ) {

        if (projected) {
            return Response.ok(paperRepository.listAllProjected(term, author, page, size)).build();
        }

        return Response.ok(paperRepository.listAll(term, page, size)).build();
    }


    /**
     * Count all papers
     *
     * @param term String
     * @return Response
     */
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response countAll(@QueryParam("term") String term) {
        return Response.ok(paperRepository.count(term)).build();
    }


    /**
     * Create a new paper
     *
     * @param paper Paper
     * @return Response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Role.ADMIN, Role.USER})
    public Response create(Paper paper) {
        return Response.ok(paperRepository.create(paper, jwt)).build();
    }

    /**
     * Get a paper by id
     *
     * @param id Long
     * @return Response
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(paperRepository.findById(id)).build();
    }

    /**
     * Delete a paper by id
     *
     * @param id Long
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed({Role.ADMIN, Role.USER})
    public Response delete(@PathParam("id") Long id) {
        return paperRepository.delete(id, jwt) ? Response.ok().build() : Response.noContent().build();
    }

}
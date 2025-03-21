package io.github.manoelpiovesan.projections;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.sql.Timestamp;

/**
 * @author Manoel Rodrigues
 */
@RegisterForReflection
public record PaperProjection(String title, String summary, String author, String email, Timestamp submittedAt) {
}

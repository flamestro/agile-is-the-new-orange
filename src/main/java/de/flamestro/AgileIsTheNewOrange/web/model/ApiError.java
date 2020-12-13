package de.flamestro.AgileIsTheNewOrange.web.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiError {
    Status status;
}

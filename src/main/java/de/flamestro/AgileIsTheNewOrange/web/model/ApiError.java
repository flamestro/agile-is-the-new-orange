package de.flamestro.AgileIsTheNewOrange.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {
    Status status;
}

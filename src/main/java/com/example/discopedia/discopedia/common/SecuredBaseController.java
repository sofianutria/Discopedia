package com.example.discopedia.discopedia.common;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("IsAuthenticated()")
public abstract class SecuredBaseController {
}

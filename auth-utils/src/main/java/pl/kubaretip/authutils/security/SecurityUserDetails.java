package pl.kubaretip.authutils.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityUserDetails extends UserDetails {

    String getId();

}

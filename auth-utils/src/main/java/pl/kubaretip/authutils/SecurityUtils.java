package pl.kubaretip.authutils;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.kubaretip.authutils.security.SecurityUserDetails;

public class SecurityUtils {

    public static String getCurrentUser() {
        var principal = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    public static String getCurrentUserPreferredUsername(){
        var principal = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();
    }

}

package pizzaShop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		// Get the role of logged in user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String role = auth.getAuthorities().toString();

		String targetUrl = "";

		if(role.contains("ADMIN")) targetUrl = "/sAdmin";
		else if(role.contains("SELLER")) targetUrl = "/sSeller";
		else if (role.contains("DELIVERER")) targetUrl = "/sDeliverer";
		else targetUrl = "/Baker";
		return targetUrl;
	}
}

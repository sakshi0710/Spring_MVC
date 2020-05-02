package com.project.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;



@Controller

public class LogoutController 
{
	
  
   @RequestMapping(value="/logout" , method=RequestMethod.GET)
   public String logout(HttpServletRequest request, HttpServletResponse response)
   
   {
	   Authentication autho = SecurityContextHolder.getContext()
				.getAuthentication();


		if (autho != null)
		{
			new SecurityContextLogoutHandler().logout(request, response,autho);
            request.getSession().invalidate();
		}
	   return "redirect:/";
   }
   
}

package com.project.todo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@SessionAttributes("name")
public class ToDoController {
	
   @Autowired
   ToDoService service;
   
   @InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}
   
   @RequestMapping(value="/list-todos" , method=RequestMethod.GET)
   public String showTodoList(ModelMap model)
   {
	   model.addAttribute("todos", service.retrieveTodos(retrievedLoggedinUserName()));
	   return "list-todos";
   }
   
   
private String retrievedLoggedinUserName() {
	Object principal = SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();

	if (principal instanceof UserDetails)
		return ((UserDetails) principal).getUsername();

	return principal.toString();
}
   
   @RequestMapping(value="/add-todo" , method=RequestMethod.GET)
   public String addTodoList(ModelMap model)
   {
	   model.addAttribute("todo", new Todo(0, retrievedLoggedinUserName(), "", new Date(), false)); 
	  return "todos";
   }
   
   
   @RequestMapping(value = "/add-todo", method = RequestMethod.POST)
	public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

		if (result.hasErrors())
			return "todos";

		service.addTodo((String) model.get("name"), todo.getDesc(), new Date(),
				false);
		model.clear();// to prevent request parameter "name" to be passed
		return "redirect:/list-todos";
	}

   @RequestMapping(value = "/update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model,@Valid Todo todo, BindingResult result) {
		
       if (result.hasErrors())
			return "todos";

       todo.setUser(retrievedLoggedinUserName());
		service.updateTodo(todo);
		model.clear();// to prevent request parameter "name" to be passed
		return "redirect:/list-todos";
	}

   
   @RequestMapping(value = "/update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage(ModelMap model, @RequestParam int id) {
		model.addAttribute("todo", service.retrieveTodo(id));
		return "todos";
	}
  
   @RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
	public String deleteTodo(@RequestParam int id) {
		service.deleteTodo(id);

		return "redirect:/list-todos";
	}
   
}

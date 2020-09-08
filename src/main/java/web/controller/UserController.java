package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/")
    public String getHomePage() {
        return "index";
    }
    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }
    @GetMapping("/admin")
    public ModelAndView allUsers(){
        List<User> users = userService.listAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("users",users);
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
    @GetMapping("/admin/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") int id){
        User user = userService.get(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
    @PostMapping("/admin/edit")
    public ModelAndView editUser(@ModelAttribute("user") User user){
        user.setRoles(userService.get(user.getId()).getRoles());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin");
        userService.save(user);
        return modelAndView;
    }
    @GetMapping("/admin/add")
    public ModelAndView addPage(){
        ModelAndView  modelAndView = new ModelAndView();
        modelAndView.setViewName("addPage");
        modelAndView.addObject("user",new User());
        return modelAndView;
    }

    @PostMapping("/admin/add")
    public  ModelAndView addUser(@ModelAttribute("user") User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin");
        userService.save(user);
        return modelAndView;
    }
//    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)//вместо модул анд вью
//    public String addUser(@ModelAttribute("user") User user){
//        System.out.println(user);
//        userService.save(user);
//
//        return "redirect:/admin";
//}
    @RequestMapping("/admin/delete/{id}")
    public String removeUser(@PathVariable("id") long id) {
        userService.delete(id);

        return "redirect:/admin";
    }

//    @GetMapping("/admin/delete/{id}")
//    public ModelAndView deleteUser(@PathVariable("id") long id){
//        ModelAndView modelAndView = new ModelAndView();
//
//        System.out.println(id);
//        userService.delete(id);
//        modelAndView.setViewName("redirect:/admin");
//
//        return modelAndView;
//    }
    @GetMapping("/user")
    public ModelAndView userPage(@AuthenticationPrincipal User user){
        ModelAndView model = new ModelAndView();
        model.setViewName("user");
        model.addObject("user", userService.get(user.getId()));
        return model;
    }
}

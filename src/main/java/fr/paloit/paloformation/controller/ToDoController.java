package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.Session;
import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ToDoController {

    @Autowired
    ToDoService toDoService;

    @Autowired
    SessionService sessionService;

    @GetMapping({"/todo-fait"})
    public String etatFait(@RequestParam Long id) {
        ToDo todo = toDoService.trouverToDoById(id);
        if (todo != null) {
            toDoService.etatFait(todo);
        }
        assert todo != null;
        return "redirect:/session/detail-session?id=" + todo.getSession().getId();
    }


}

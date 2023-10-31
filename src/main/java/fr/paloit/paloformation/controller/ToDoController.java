package fr.paloit.paloformation.controller;

import fr.paloit.paloformation.model.ToDo;
import fr.paloit.paloformation.service.SessionService;
import fr.paloit.paloformation.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ToDoController {

    @Autowired
    ToDoService toDoService;

    @Autowired
    SessionService sessionService;

    @GetMapping({"/todo-modifier"})
    @ResponseBody
    public String modifierEtatToDo(@RequestParam Long id) {
        ToDo todo = toDoService.trouverToDoById(id);
        if (todo != null) {
            toDoService.modifierEtat(todo);
        }
        assert todo != null;

        return todo.getEtat().toString();
    }


}

package nl.transposeit.cloudgrid.boincservice.rest;

import nl.transposeit.cloudgrid.boincservice.entity.Tasks;
import nl.transposeit.cloudgrid.boincservice.repository.TasksRepository;
import nl.transposeit.cloudgrid.boincservice.rest.dto.TasksDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class TasksRestController {
    @Autowired
    private TasksRepository tasksRepository;

    @GetMapping("/tasks")
    public ResponseEntity<TasksDto> findAll() {
        List<Tasks> tasks = tasksRepository.findAll();
        TasksDto tasksDto = new TasksDto(tasks);
        tasksDto.setResults(tasks.size());

        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Tasks> addTask(@Valid @RequestBody Tasks project) {
        project.setId(0);
        tasksRepository.save(project);

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{workerId}")
    public ResponseEntity<TasksDto> findTask(@PathVariable int workerId) {
        Optional<Tasks> result = tasksRepository.findById(workerId);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new TasksDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TasksDto(result.get()), HttpStatus.OK);
    }

    @PutMapping("/tasks/{projectId}")
    public ResponseEntity<TasksDto> updateTask(@RequestBody Tasks project) {
        tasksRepository.save(project);
        return new ResponseEntity<>(new TasksDto(project), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{projectId}")
    public ResponseEntity<TasksDto> deleteTask(@PathVariable int workerId) {
        Optional<Tasks> result = tasksRepository.findById(workerId);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new TasksDto(), HttpStatus.NOT_FOUND);
        }

        tasksRepository.delete(result.get());
        return new ResponseEntity<>(new TasksDto(), HttpStatus.OK);
    }
    
    
}

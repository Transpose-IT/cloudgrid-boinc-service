package nl.transposeit.cloudgrid.boincservice.rest;

/*
 # (c) 2019 Jorn Argelo, <jorn@transpose-it.nl>
 #
 # Cloudgrid is free software: you can redistribute it and/or modify
 # it under the terms of the GNU General Public License as published by
 # the Free Software Foundation, either version 3 of the License, or
 # (at your option) any later version.
 #
 # Cloudgrid is distributed in the hope that it will be useful,
 # but WITHOUT ANY WARRANTY; without even the implied warranty of
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 # GNU General Public License for more details.
 #
 # You should have received a copy of the GNU General Public License
 # along with Cloudgrid.  If not, see <http://www.gnu.org/licenses/>.
*/

import nl.transposeit.cloudgrid.boincservice.entity.Projects;
import nl.transposeit.cloudgrid.boincservice.entity.Tasks;
import nl.transposeit.cloudgrid.boincservice.repository.ProjectRepository;
import nl.transposeit.cloudgrid.boincservice.repository.TasksRepository;
import nl.transposeit.cloudgrid.boincservice.rest.dto.ProjectsDto;
import nl.transposeit.cloudgrid.boincservice.rest.dto.TasksDto;
import nl.transposeit.cloudgrid.boincservice.rest.dto.WorkersDto;
import nl.transposeit.cloudgrid.boincservice.entity.Workers;
import nl.transposeit.cloudgrid.boincservice.repository.WorkersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class BoincClientRestController {

    @Autowired
    private WorkersRepository workersRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private ProjectRepository projectsRepository;

    @GetMapping("/client/workers/{hostCPid}")
    public ResponseEntity<WorkersDto> findWorkerByHostCPid(@PathVariable String hostCPid) {
        Optional<Workers> result = workersRepository.getWorkerByHostCPid(hostCPid);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new WorkersDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new WorkersDto(result.get()), HttpStatus.OK);
    }
    @PutMapping("/client/workers")
    public ResponseEntity<WorkersDto> updateWorkerByHostCPid(@RequestBody Workers worker) {
        Optional<Workers> result = workersRepository.getWorkerByHostCPid(worker.getHostCPid());

        if (result.isEmpty()) {
            return new ResponseEntity<>(new WorkersDto(), HttpStatus.NOT_FOUND);
        }

        Workers dbResult = result.get();
        worker.setId(dbResult.getId());
        workersRepository.save(worker);

        return new ResponseEntity<>(new WorkersDto(result.get()), HttpStatus.OK);
    }

    @GetMapping("/client/tasks/{wuName}")
    public ResponseEntity<TasksDto> findTaskByWuName(@PathVariable String wuName) {
        Optional<Tasks> result = tasksRepository.getTaskByWuName(wuName);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new TasksDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TasksDto(result.get()), HttpStatus.OK);
    }
    @PutMapping("/client/tasks")
    public ResponseEntity<TasksDto> updateTaskByWuName(@RequestBody Tasks task) {
        Optional<Tasks> result = tasksRepository.getTaskByWuName(task.getWuName());

        if (result.isEmpty()) {
            return new ResponseEntity<>(new TasksDto(), HttpStatus.NOT_FOUND);
        }

        Tasks dbResult = result.get();
        task.setId(dbResult.getId());
        tasksRepository.save(task);

        return new ResponseEntity<>(new TasksDto(result.get()), HttpStatus.OK);
    }

    @GetMapping("/client/projects/{name}")
    public ResponseEntity<ProjectsDto> findProjectByName(@PathVariable String projectName) {
        Optional<Projects> result = projectsRepository.getProjectByProjectName(projectName);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new ProjectsDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ProjectsDto(result.get()), HttpStatus.OK);
    }
    @PutMapping("/client/projects")
    public ResponseEntity<ProjectsDto> updateProjectByName(@RequestBody Projects project) {
        Optional<Projects> result = projectsRepository.getProjectByProjectName(project.getProjectName());

        if (result.isEmpty()) {
            return new ResponseEntity<>(new ProjectsDto(), HttpStatus.NOT_FOUND);
        }

        Projects dbResult = result.get();
        dbResult.setProjectCredits(project.getProjectCredits());
        projectsRepository.save(dbResult);

        return new ResponseEntity<>(new ProjectsDto(result.get()), HttpStatus.OK);
    }
    
    
}

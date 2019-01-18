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

import nl.transposeit.cloudgrid.boincservice.rest.dto.ProjectsDto;
import nl.transposeit.cloudgrid.boincservice.entity.Projects;
import nl.transposeit.cloudgrid.boincservice.repository.ProjectRepository;
import nl.transposeit.cloudgrid.boincservice.utility.Logging;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/v1")
public class ProjectRestController {

    private Logging logging = new Logging(ProjectRestController.class);

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/projects")
    public ResponseEntity<ProjectsDto> findAll() {
        logging.info("Retrieving all projects:");
        List<Projects> projects = projectRepository.findAll();
        ProjectsDto projectsDto = new ProjectsDto(projects);
        projectsDto.setResults(projects.size());
        logging.info("Project amount found: " + projectsDto.getResults());

        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }

    @PostMapping("/projects")
    public ResponseEntity<Projects> addProject(@Valid @RequestBody Projects project, HttpServletRequest request) {
        logging.info("Creating new project");
        logging.debug(project.toString());

        project.setId(0);
        projectRepository.save(project);
        logging.info("Save successful under id: " + project.getId());

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectsDto> findById(@PathVariable int id) {
        logging.info("Retrieving project object: " + id);

        Optional <Projects> result = projectRepository.findById(id);

        if (result.isEmpty()) {
            logging.info("Retrieving project result: Not found ");
            return new ResponseEntity<>(new ProjectsDto(), HttpStatus.NOT_FOUND);
        }
        logging.debug(result.get().toString());
        logging.info("Retrieving project result: OK ");
        return new ResponseEntity<>(new ProjectsDto(result.get()), HttpStatus.OK);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectsDto> updateProject(@RequestBody Projects project) {
        logging.info("Saving project object: " + project.getId());
        logging.debug(project.toString());

        projectRepository.save(project);

        logging.info("Saving project result: OK");
        return new ResponseEntity<>(new ProjectsDto(project), HttpStatus.OK);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectsDto> deleteProject(@PathVariable int id) {
        logging.info("Retrieving project object: " + id);
        Optional<Projects> result = projectRepository.findById(id);

        if (result.isEmpty()) {
            logging.info("Retrieving project result: Not found ");
            return new ResponseEntity<>(new ProjectsDto(), HttpStatus.NOT_FOUND);
        }

        logging.info("Deleting project object: " + id);
        projectRepository.delete(result.get());
        logging.info("Deleting project result: OK");
        return new ResponseEntity<>(new ProjectsDto(), HttpStatus.OK);
    }
}

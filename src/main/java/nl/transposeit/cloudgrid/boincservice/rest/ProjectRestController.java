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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class ProjectRestController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/projects")
    public ResponseEntity<ProjectsDto> findAll() {
        List<Projects> projects = projectRepository.findAll();
        ProjectsDto projectsDto = new ProjectsDto(projects);
        projectsDto.setResults(projects.size());

        return new ResponseEntity<>(projectsDto, HttpStatus.OK);
    }

    @PostMapping("/projects")
    public ResponseEntity<Projects> addProject(@Valid @RequestBody Projects project) {
        project.setId(0);
        projectRepository.save(project);

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectsDto> findById(@PathVariable int id) {
        Optional <Projects> result = projectRepository.findById(id);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new ProjectsDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ProjectsDto(result.get()), HttpStatus.OK);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectsDto> updateProject(@RequestBody Projects project) {
        projectRepository.save(project);
        return new ResponseEntity<>(new ProjectsDto(project), HttpStatus.OK);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectsDto> deleteProject(@PathVariable int id) {
        Optional<Projects> result = projectRepository.findById(id);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new ProjectsDto(), HttpStatus.NOT_FOUND);
        }

        projectRepository.delete(result.get());
        return new ResponseEntity<>(new ProjectsDto(), HttpStatus.OK);
    }
}

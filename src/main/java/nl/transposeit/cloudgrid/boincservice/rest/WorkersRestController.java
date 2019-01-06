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

import nl.transposeit.cloudgrid.boincservice.rest.dto.WorkersDto;
import nl.transposeit.cloudgrid.boincservice.entity.Workers;
import nl.transposeit.cloudgrid.boincservice.repository.WorkersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class WorkersRestController {
    @Autowired
    private WorkersRepository workersRepository;

    @GetMapping("/workers")
    public ResponseEntity<WorkersDto> findAll() {
        List<Workers> workers = workersRepository.findAll();
        WorkersDto workersDto = new WorkersDto(workers);
        workersDto.setResults(workers.size());

        return new ResponseEntity<>(workersDto, HttpStatus.OK);
    }

    @PostMapping("/workers")
    public ResponseEntity<Workers> addWorker(@Valid @RequestBody Workers worker) {
        worker.setId(0);
        workersRepository.save(worker);

        return new ResponseEntity<>(worker, HttpStatus.CREATED);
    }

    @GetMapping("/workers/{id}")
    public ResponseEntity<WorkersDto> findWorker(@PathVariable int id) {
        Optional<Workers> result = workersRepository.findById(id);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new WorkersDto(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new WorkersDto(result.get()), HttpStatus.OK);
    }

    @PutMapping("/workers/{id}")
    public ResponseEntity<WorkersDto> updateWorker(@RequestBody Workers worker) {
        workersRepository.save(worker);
        return new ResponseEntity<>(new WorkersDto(worker), HttpStatus.OK);
    }

    @DeleteMapping("/workers/{id}")
    public ResponseEntity<WorkersDto> deleteWorker(@PathVariable int id) {
        Optional<Workers> result = workersRepository.findById(id);

        if (result.isEmpty()) {
            return new ResponseEntity<>(new WorkersDto(), HttpStatus.NOT_FOUND);
        }

        workersRepository.delete(result.get());
        return new ResponseEntity<>(new WorkersDto(), HttpStatus.OK);
    }
}

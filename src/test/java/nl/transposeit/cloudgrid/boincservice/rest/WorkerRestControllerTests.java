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

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.transposeit.cloudgrid.boincservice.entity.Workers;
import nl.transposeit.cloudgrid.boincservice.repository.WorkersRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkersRestController.class)
public class WorkerRestControllerTests {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WorkersRepository workerRepository;


    private JacksonTester<Workers> workersJSON;

    // Test objects
    private Workers worker1;
    private Workers worker2;
    private Workers worker3;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        worker1 = new Workers("http://proj1", "abcd-123", "hostname-1",
                4, 913123, "Windows 10");
        worker1.setId(1);

        worker2 = new Workers("http://proj2", "def-123", "hostname-2",
                4, 773123, "Windows 10");
        worker2.setId(2);

        worker3 = new Workers("http://proj3", "efg-123", "hostname-3",
                4, 413123, "Windows 10");
        worker3.setId(3);


    }

    @Test
    public void givenValidTask_whenPostOnTaskAPI_thenCreateTask() throws Exception {
        given(workerRepository.save(Mockito.any())).willReturn(worker1);

        mvc.perform(post("/v1/workers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(workersJSON.write(worker1).getJson())
        ).andExpect(status().isCreated());

        verify(workerRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(workerRepository);
    }

    @Test
    public void givenValidTaskId_whenDeleteOnTaskAPI_thenDeleteTask() throws Exception {
        given(workerRepository.findById(worker1.getId())).willReturn(Optional.ofNullable(worker1));

        mvc.perform(delete("/v1/workers/" + worker1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(workersJSON.write(worker1).getJson())
        ).andExpect(status().isOk());

        verify(workerRepository, VerificationModeFactory.times(1)).delete(worker1);
        reset(workerRepository);
    }

    @Test
    public void givenUnknownTaskId_whenDeleteOnTaskAPI_thenReturnNotFound() throws Exception {
        int unknownId = 99999;
        given(workerRepository.findById(unknownId)).willReturn(Optional.ofNullable(null));

        mvc.perform(delete("/v1/workers/" + unknownId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(workersJSON.write(worker1).getJson())
        ).andExpect(status().isNotFound());

        verify(workerRepository, VerificationModeFactory.times(1)).findById(unknownId);
        reset(workerRepository);
    }

    @Test
    public void givenValidTaskId_whenGetTaskById_thenReturnTaskAsJsonArray() throws Exception {
        given(workerRepository.findById(worker1.getId())).willReturn(Optional.ofNullable(worker1));

        mvc.perform(get("/v1/workers/" + worker1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workers", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.workers[0].id", is(worker1.getId())))
                .andExpect(jsonPath("$.workers[0].cpus", is(worker1.getCpus())))
                .andExpect(jsonPath("$.workers[0].hostName", is(worker1.getHostName())))
                .andExpect(status().isOk());

        verify(workerRepository, VerificationModeFactory.times(1)).findById(worker1.getId());
        reset(workerRepository);
    }

    @Test
    public void givenUnknownTaskId_whenGetTaskById_thenReturnNotFoundAsJsonArray() throws Exception {
        int unknownTaskId = 9999999;

        mvc.perform(get("/v1/workers/" + unknownTaskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", is(0)))
                .andExpect(status().isNotFound());
        verify(workerRepository, VerificationModeFactory.times(1)).findById(unknownTaskId);
        reset(workerRepository);
    }

    @Test
    public void givenWrongInput_whenGetTaskById_thenReturnException() throws Exception {
        String invalidInput = "Oh, yes little Bobby Tables we call him.";
        mvc.perform(get("/v1/workers/" + invalidInput)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidTask_whenPutTask_thenUpdateTask() throws Exception {

        Workers updatedWorker = new Workers();
        updatedWorker.setId(1);
        updatedWorker.setCpus(4);
        updatedWorker.setProjectName(worker2.getProjectName());
        updatedWorker.setHostName(worker2.getHostName());

        given(workerRepository.save(Mockito.any())).willReturn(updatedWorker);

        mvc.perform(put("/v1/workers/" + worker1.getId())
                .content(workersJSON.write(updatedWorker).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workers", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.workers[0].id", is(worker1.getId())))
                .andExpect(jsonPath("$.workers[0].cpus", is(worker2.getCpus())))
                .andExpect(jsonPath("$.workers[0].hostName", is(worker2.getHostName())))
                .andExpect(status().isOk()).andReturn();

        verify(workerRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(workerRepository);
    }

    @Test
    public void whenGetAllWorkers_thenReturnAllWorkersAsJsonArray() throws Exception {
        List<Workers> allWorkers = Arrays.asList(worker1, worker2, worker3);

        given(workerRepository.findAll()).willReturn(allWorkers);

        mvc.perform(get("/v1/workers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workers", hasSize(3)))
                .andExpect(jsonPath("$.workers[0].projectName", is(worker1.getProjectName())))
                .andExpect(jsonPath("$.workers[1].projectName", is(worker2.getProjectName())))
                .andExpect(jsonPath("$.workers[2].projectName", is(worker3.getProjectName())))
                .andExpect(jsonPath("$.results", is(3)))
                .andExpect(status().isOk());

        verify(workerRepository, VerificationModeFactory.times(1)).findAll();
        reset(workerRepository);
    }
}

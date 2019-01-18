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
import nl.transposeit.cloudgrid.boincservice.entity.Projects;
import nl.transposeit.cloudgrid.boincservice.entity.Tasks;
import nl.transposeit.cloudgrid.boincservice.entity.Workers;
import nl.transposeit.cloudgrid.boincservice.repository.ProjectRepository;
import nl.transposeit.cloudgrid.boincservice.repository.TasksRepository;
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
@WebMvcTest(BoincClientRestController.class)
public class BoincClientRestControllerTests {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private WorkersRepository workerRepository;

    @MockBean
    private TasksRepository taskRepository;

    @MockBean
    private ProjectRepository projectRepository;

    private JacksonTester<Workers> workersJSON;

    // Test objects
    private Workers worker1;
    private Tasks task1;
    private Projects project1;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        worker1 = new Workers("http://project-url", "abcd-123", "hostname-1",
                4, 913123, "Windows 10");
        worker1.setId(1);

        task1 = new Tasks(worker1,"http://project-url","wuName1",
                1, 1, 4213412, 2311323, 41231,
                3123213, 231231, 12312321, true
        );
        task1.setId(1);

        project1 = new Projects("project1", "http://project-url.com", "apikey123");
        project1.setId(1);
    }


    @Test
    public void givenHostCPid_whenGetOnBoincClientAPI_thenReturnWorker() throws Exception {
        given(workerRepository.getWorkerByHostCPid(worker1.getHostCPid())).willReturn(Optional.ofNullable(worker1));

        mvc.perform(get("/v1/client/workers/" + worker1.getHostCPid())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workers", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.workers[0].id", is(worker1.getId())))
                .andExpect(status().isOk());

        verify(workerRepository, VerificationModeFactory.times(1))
                .getWorkerByHostCPid(worker1.getHostCPid());
        reset(workerRepository);
    }


    @Test
    public void givenValidWorkerObject_whenPutOnBoincClientAPI_thenUpdateWorker() throws Exception {

        Workers updatedWorker = new Workers();
        updatedWorker.setId(worker1.getId());
        updatedWorker.setHostCPid(worker1.getHostCPid());
        updatedWorker.setCpus(2);
        updatedWorker.setProjectName(worker1.getProjectName());
        updatedWorker.setHostName(worker1.getHostName());

        given(workerRepository.getWorkerByHostCPid(worker1.getHostCPid())).willReturn(Optional.of(updatedWorker));

        mvc.perform(put("/v1/client/workers/" + worker1.getHostCPid())
                .content(workersJSON.write(updatedWorker).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workers", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.workers[0].id", is(worker1.getId())))
                .andExpect(jsonPath("$.workers[0].cpus", is(2)))
                .andExpect(status().isOk());

        verify(workerRepository, VerificationModeFactory.times(1))
                .getWorkerByHostCPid(worker1.getHostCPid());
        reset(workerRepository);
    }

    @Test
    public void givenValidTaskObject_whenGetOnBoincClientAPIbyWuName_thenReturnTaskAsJsonArray() throws Exception {
        given(taskRepository.getTaskByWuName(task1.getWuName())).willReturn(Optional.ofNullable(task1));

        mvc.perform(get("/v1/client/tasks/" + task1.getWuName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.tasks[0].wuName", is(task1.getWuName())))
                .andExpect(status().isOk());
        verify(taskRepository, VerificationModeFactory.times(1))
                .getTaskByWuName(task1.getWuName());
        reset(taskRepository);
    }


    @Test
    public void givenValidTaskObject_whenPutOnBoincClientAPI_thenUpdateTask() throws Exception {

        Tasks updatedTask = new Tasks();
        updatedWorker.setId(worker1.getId());
        updatedWorker.setHostCPid(worker1.getHostCPid());
        updatedWorker.setCpus(2);
        updatedWorker.setProjectName(worker1.getProjectName());
        updatedWorker.setHostName(worker1.getHostName());

        given(workerRepository.getWorkerByHostCPid(worker1.getHostCPid())).willReturn(Optional.of(updatedWorker));

        mvc.perform(put("/v1/client/workers/" + worker1.getHostCPid())
                .content(workersJSON.write(updatedWorker).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.workers", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.workers[0].id", is(worker1.getId())))
                .andExpect(jsonPath("$.workers[0].cpus", is(2)))
                .andExpect(status().isOk());

        verify(workerRepository, VerificationModeFactory.times(1))
                .getWorkerByHostCPid(worker1.getHostCPid());
        reset(workerRepository);
    }
}

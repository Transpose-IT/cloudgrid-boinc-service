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
import nl.transposeit.cloudgrid.boincservice.entity.Tasks;
import nl.transposeit.cloudgrid.boincservice.entity.Workers;
import nl.transposeit.cloudgrid.boincservice.repository.TasksRepository;
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
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TasksRestController.class)
public class TaskRestControllerTests {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean

    private TasksRepository taskRepository;
    private JacksonTester<Tasks> tasksJSON;

    // Test objects
    private Tasks task1;
    private Tasks task2;
    private Tasks task3;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        Workers worker = new Workers();
        worker.setId(1);

        task1 = new Tasks(worker,"http://project-url","wuName1",
                1, 1, 4213412, 2311323, 41231,
                3123213, 231231, 12312321, true
        );
        task1.setId(1);

        task2 = new Tasks(worker,"http://project-url2","wuName2",
                1, 1, 4213412, 119323, 412391,
                312553213, 4431231, 1231232221, true
        );
        task2.setId(2);

        task3 = new Tasks(worker,"http://project-url3","wuName3",
                1, 1, 4213412, 23911323, 418231,
                316623213, 26631231, 1231231121, true
        );
        task1.setId(3);

    }

    @Test
    public void givenValidTasks_whenPostOnTasksAPI_thenCreateTasks() throws Exception {
        given(taskRepository.save(Mockito.any())).willReturn(task1);

        mvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tasksJSON.write(task1).getJson())
        ).andExpect(status().isCreated());

        verify(taskRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(taskRepository);
    }

    @Test
    public void givenValidTasksId_whenDeleteOnTasksAPI_thenDeleteTasks() throws Exception {
        given(taskRepository.findById(task1.getId())).willReturn(Optional.ofNullable(task1));

        mvc.perform(delete("/v1/tasks/" + task1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(tasksJSON.write(task1).getJson())
        ).andExpect(status().isOk());

        verify(taskRepository, VerificationModeFactory.times(1)).delete(task1);
        reset(taskRepository);
    }

    @Test
    public void givenUnknownTasksId_whenDeleteOnTasksAPI_thenReturnNotFound() throws Exception {
        int unknownId = 99999;
        given(taskRepository.findById(unknownId)).willReturn(Optional.ofNullable(null));

        mvc.perform(delete("/v1/tasks/" + unknownId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tasksJSON.write(task1).getJson())
        ).andExpect(status().isNotFound());

        verify(taskRepository, VerificationModeFactory.times(1)).findById(unknownId);
        reset(taskRepository);
    }

    @Test
    // TODO: Check why taskCredits is returning an int in the test and not a long
    public void givenValidTasksId_whenGetTasksById_thenReturnTasksAsJsonArray() throws Exception {
        given(taskRepository.findById(task1.getId())).willReturn(Optional.ofNullable(task1));

        mvc.perform(get("/v1/tasks/" + task1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.tasks[0].id", is(task1.getId())))
                .andExpect(jsonPath("$.tasks[0].projectUrl", is(task1.getProjectUrl())))
                .andExpect(jsonPath("$.tasks[0].wuName", is(task1.getWuName())))
                .andExpect(jsonPath("$.tasks[0].completedTime", closeTo(task1.getCompletedTime(), 0.1)))
                .andExpect(jsonPath("$.tasks[0].estimatedRemainingTime",
                        closeTo(task1.getEstimatedRemainingTime(), 0.1)))
                .andExpect(status().isOk());

        verify(taskRepository, VerificationModeFactory.times(1)).findById(task1.getId());
        reset(taskRepository);
    }

    @Test
    public void givenUnknownTasksId_whenGetTasksById_thenReturnNotFoundAsJsonArray() throws Exception {
        int unknownTasksId = 9999999;

        mvc.perform(get("/v1/tasks/" + unknownTasksId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", is(0)))
                .andExpect(status().isNotFound());
        verify(taskRepository, VerificationModeFactory.times(1)).findById(unknownTasksId);
        reset(taskRepository);
    }

    @Test
    public void givenWrongInput_whenGetTasksById_thenReturnException() throws Exception {
        String invalidInput = "Oh, yes little Bobby Tables we call him.";
        mvc.perform(get("/v1/tasks/" + invalidInput)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidTasks_whenPutTasks_thenUpdateTasks() throws Exception {

        Tasks updatedTasks = new Tasks();
        updatedTasks.setProjectUrl(task1.getProjectUrl());
        updatedTasks.setWuName(task2.getWuName());
        updatedTasks.setCompletedTime(task3.getCompletedTime());
        updatedTasks.setId(task1.getId());


        given(taskRepository.save(Mockito.any())).willReturn(updatedTasks);

        mvc.perform(put("/v1/tasks/" + task1.getId())
                .content(tasksJSON.write(updatedTasks).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.tasks[0].id", is(task1.getId())))
                .andExpect(jsonPath("$.tasks[0].projectUrl", is(task1.getProjectUrl())))
                .andExpect(jsonPath("$.tasks[0].wuName", is(task2.getWuName())))
                .andExpect(jsonPath("$.tasks[0].completedTime", closeTo(task3.getCompletedTime(), 0.1)))
                .andExpect(status().isOk()).andReturn();

        verify(taskRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(taskRepository);
    }

    @Test
    public void whenGetAllTasks_thenReturnAllTasksAsJsonArray() throws Exception {
        List<Tasks> allTasks = Arrays.asList(task1, task2, task3);

        given(taskRepository.findAll()).willReturn(allTasks);

        mvc.perform(get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tasks", hasSize(3)))
                .andExpect(jsonPath("$.tasks[0].wuName", is(task1.getWuName())))
                .andExpect(jsonPath("$.tasks[1].wuName", is(task2.getWuName())))
                .andExpect(jsonPath("$.tasks[2].wuName", is(task3.getWuName())))
                .andExpect(jsonPath("$.results", is(3)))
                .andExpect(status().isOk());

        verify(taskRepository, VerificationModeFactory.times(1)).findAll();
        reset(taskRepository);
    }
}

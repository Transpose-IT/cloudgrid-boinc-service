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
import nl.transposeit.cloudgrid.boincservice.repository.ProjectRepository;
import nl.transposeit.cloudgrid.boincservice.rest.dto.ProjectsDto;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectRestController.class)
public class ProjectRestControllerTests {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProjectRepository projectRepository;


    private JacksonTester<Projects> projectsJSON;

    // Test objects
    private Projects project1;
    private Projects project2;
    private Projects project3;


    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        project1 = new Projects("project1", "http://test-url.com", "apikey123");
        project1.setId(1);

        project2 = new Projects("project2", "http://test-url2.com", "apikey456");
        project2.setId(2);

        project3 = new Projects("project3", "http://test-url2.com", "apikey789");
        project3.setId(3);
    }

    @Test
    public void givenValidProject_whenPostOnProjectAPI_thenCreateProject() throws Exception {
        given(projectRepository.save(Mockito.any())).willReturn(project1);

        mvc.perform(post("/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectsJSON.write(project1).getJson())
        ).andExpect(status().isCreated());

        verify(projectRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(projectRepository);
    }

    @Test
    public void givenValidProjectId_whenDeleteOnProjectAPI_thenDeleteProject() throws Exception {
        given(projectRepository.findById(project1.getId())).willReturn(java.util.Optional.ofNullable(project1));

        mvc.perform(delete("/v1/projects/" + project1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectsJSON.write(project1).getJson())
        ).andExpect(status().isOk());

        verify(projectRepository, VerificationModeFactory.times(1)).delete(project1);
        reset(projectRepository);
    }

    @Test
    public void givenUnknownProjectId_whenDeleteOnProjectAPI_thenReturnNotFound() throws Exception {
        int unknownId = 99999;
        given(projectRepository.findById(unknownId)).willReturn(java.util.Optional.ofNullable(null));

        mvc.perform(delete("/v1/projects/" + unknownId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectsJSON.write(project1).getJson())
        ).andExpect(status().isNotFound());

        verify(projectRepository, VerificationModeFactory.times(1)).findById(unknownId);
        reset(projectRepository);
    }

    @Test
    // TODO: Check why projectCredits is returning an int in the test and not a long
    public void givenValidProjectId_whenGetProjectById_thenReturnProjectAsJsonArray() throws Exception {
        given(projectRepository.findById(project1.getId())).willReturn(java.util.Optional.ofNullable(project1));

        mvc.perform(get("/v1/projects/" + project1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.projects[0].id", is(project1.getId())))
                .andExpect(jsonPath("$.projects[0].projectName", is(project1.getProjectName())))
                .andExpect(jsonPath("$.projects[0].projectUrl", is(project1.getProjectUrl())))
                .andExpect(jsonPath("$.projects[0].apiKey", is(project1.getApiKey())))
                .andExpect(jsonPath("$.projects[0].projectCredits", is(0)))
                .andExpect(status().isOk());

        verify(projectRepository, VerificationModeFactory.times(1)).findById(project1.getId());
        reset(projectRepository);
    }

    @Test
    public void givenUnknownProjectId_whenGetProjectById_thenReturnNotFoundAsJsonArray() throws Exception {
        int unknownProjectId = 9999999;

        mvc.perform(get("/v1/projects/" + unknownProjectId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.results", is(0)))
                .andExpect(status().isNotFound());
        verify(projectRepository, VerificationModeFactory.times(1)).findById(unknownProjectId);
        reset(projectRepository);
    }

    @Test
    public void givenWrongInput_whenGetProjectById_thenReturnException() throws Exception {
        String invalidInput = "Oh, yes little Bobby Tables we call him.";
        mvc.perform(get("/v1/projects/" + invalidInput)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidProject_whenPutProject_thenUpdateProject() throws Exception {

        Projects updatedProject = new Projects(
                project1.getProjectName(), project1.getProjectUrl(), project1.getApiKey());
        updatedProject.setId(project1.getId());
        updatedProject.setProjectName(project2.getProjectName());
        updatedProject.setProjectUrl(project2.getProjectUrl());

        given(projectRepository.save(Mockito.any())).willReturn(updatedProject);

        mvc.perform(put("/v1/projects/" + project1.getId())
                .content(projectsJSON.write(updatedProject).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projects", hasSize(1)))
                .andExpect(jsonPath("$.results", is(1)))
                .andExpect(jsonPath("$.projects[0].id", is(project1.getId())))
                .andExpect(jsonPath("$.projects[0].projectName", is(project2.getProjectName())))
                .andExpect(jsonPath("$.projects[0].projectUrl", is(project2.getProjectUrl())))
                .andExpect(jsonPath("$.projects[0].apiKey", is(project1.getApiKey())))
                .andExpect(jsonPath("$.projects[0].projectCredits", is(0)))
                .andExpect(status().isOk()).andReturn();

        verify(projectRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(projectRepository);
    }

    @Test
    public void whenGetAllProjects_thenReturnAllProjectsAsJsonArray() throws Exception {
        List<Projects> allProjects = Arrays.asList(project1, project2, project3);

        given(projectRepository.findAll()).willReturn(allProjects);

        mvc.perform(get("/v1/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projects", hasSize(3)))
                .andExpect(jsonPath("$.projects[0].projectName", is(project1.getProjectName())))
                .andExpect(jsonPath("$.projects[1].projectName", is(project2.getProjectName())))
                .andExpect(jsonPath("$.projects[2].projectName", is(project3.getProjectName())))
                .andExpect(jsonPath("$.results", is(3)))
                .andExpect(status().isOk());

        verify(projectRepository, VerificationModeFactory.times(1)).findAll();
        reset(projectRepository);
    }
}

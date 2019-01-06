package nl.transposeit.cloudgrid.boincservice.entity;

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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "projects")
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", updatable = false, nullable = false)
    private int id;

    @Column(name="project_name")
    @NotNull(message="Field projectName missing")
    @Size(min=3, max=128)
    private String projectName;

    @Column(name="project_url")
    @NotNull(message="Field projectUrl missing")
    @Size(min=6, max=192)
    private String projectUrl;

    @Column(name="project_credits")
    private long projectCredits = 0;

    @Column(name="api_key")
    @NotNull(message="Field apiKey missing")
    @Size(min=6, max=192)
    private String apiKey;

    public Projects () {
        // Default constructor needed for DI
    }

    public Projects(String projectName, String projectUrl, String apiKey) {
        this.projectName = projectName;
        this.projectUrl = projectUrl;
        this.apiKey = apiKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public long getProjectCredits() {
        return projectCredits;
    }

    public void setProjectCredits(long projectCredits) {
        this.projectCredits = projectCredits;
    }
}

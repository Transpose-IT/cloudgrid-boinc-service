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

@Entity
@Table(name = "workers_projects")
public class WorkerProjects {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", updatable = false, nullable = false)
    private int id;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "workers_id")
    private Workers worker;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "projects_id")
    private Projects project;


    @Column(name="host_id")
    private String hostId;


    public WorkerProjects() {

    }

    public WorkerProjects(String hostId) {
        this.hostId = hostId;
    }

    public Workers getWorker() {
        return worker;
    }

    public void setWorker(Workers worker) {
        this.worker = worker;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}

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
@Table(name = "workers")
public class Workers {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", updatable = false, nullable = false)
    private int id;

    @Column(name="project_name")
    private String projectName;

    @Column(name="host_cpid")
    private String hostCPid;

    @Column(name="host_name")
    private String hostName;

    @Column(name="cpus")
    private int cpus;

    @Column(name="memory")
    private long memory;

    @Column(name="os_name")
    private String osName;

    public Workers() {
    }

    public Workers(String projectName, String hostCPid, String hostName, int cpus, int memory, String osName) {
        this.projectName = projectName;
        this.hostCPid = hostCPid;
        this.hostName = hostName;
        this.cpus = cpus;
        this.memory = memory;
        this.osName = osName;
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

    public String getHostCPid() {
        return hostCPid;
    }

    public void setHostCPid(String hostCPid) {
        this.hostCPid = hostCPid;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getCpus() {
        return cpus;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}

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
import java.util.Date;

@Entity
@Table(name="tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", updatable = false, nullable = false)
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "workers_id")
    private Workers worker;

    @Column(name="project_url")
    private String projectUrl;

    @Column(name="wu_name")
    private String wuName;

    @Column(name="state")
    private int state;

    @Column(name="scheduler_state")
    private int schedulerSate;

    @Column(name="received_time")
    private Date receivedTime;

    @Column(name="report_deadline")
    private Date reportDeadline;

    @Column(name="elapsed_time")
    private float elapsedTime;

    @Column(name="estimated_time")
    private float estimatedRemainingTime;

    @Column(name="completed_time")
    private float completedTime;

    @Column(name="ready")
    private boolean readyToReport;

    public Tasks() {
        // Needed for DI
    }

    public Tasks(
            int id, Workers worker, String projectUrl, int state, int schedulerSate, Date receivedTime,
            Date reportDeadline, float elapsedTime, float estimatedRemainingTime, float completedTime,
            boolean readyToReport) {
        this.id = id;
        this.worker = worker;
        this.projectUrl = projectUrl;
        this.state = state;
        this.schedulerSate = schedulerSate;
        this.receivedTime = receivedTime;
        this.reportDeadline = reportDeadline;
        this.elapsedTime = elapsedTime;
        this.estimatedRemainingTime = estimatedRemainingTime;
        this.completedTime = completedTime;
        this.readyToReport = readyToReport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Workers getWorker() {
        return worker;
    }

    public void setWorker(Workers worker) {
        this.worker = worker;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSchedulerSate() {
        return schedulerSate;
    }

    public void setSchedulerSate(int schedulerSate) {
        this.schedulerSate = schedulerSate;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Date getReportDeadline() {
        return reportDeadline;
    }

    public void setReportDeadline(Date reportDeadline) {
        this.reportDeadline = reportDeadline;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public float getEstimatedRemainingTime() {
        return estimatedRemainingTime;
    }

    public void setEstimatedRemainingTime(float estimatedRemainingTime) {
        this.estimatedRemainingTime = estimatedRemainingTime;
    }

    public float getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(float completedTime) {
        this.completedTime = completedTime;
    }

    public boolean isReadyToReport() {
        return readyToReport;
    }

    public void setReadyToReport(boolean readyToReport) {
        this.readyToReport = readyToReport;
    }

    public String getWuName() {
        return wuName;
    }

    public void setWuName(String wuName) {
        this.wuName = wuName;
    }
}

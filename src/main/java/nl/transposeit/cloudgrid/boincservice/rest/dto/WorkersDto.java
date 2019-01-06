package nl.transposeit.cloudgrid.boincservice.rest.dto;

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

import nl.transposeit.cloudgrid.boincservice.entity.Workers;

import java.util.ArrayList;
import java.util.List;

public class WorkersDto {
    private List<Workers> workers = new ArrayList<>();

    private int results = 0;

    private long generationTime = System.currentTimeMillis();

    public WorkersDto() {

    }
    public WorkersDto(List<Workers> workers) {
        this.workers = workers;
    }

    public WorkersDto(Workers workers) {
        List<Workers> workersList = new ArrayList<>();
        workersList.add(workers);

        this.workers = workersList;
        this.results = workersList.size();
    }

    public List<Workers> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Workers> workers) {
        this.workers = workers;
    }

    public long getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

}
